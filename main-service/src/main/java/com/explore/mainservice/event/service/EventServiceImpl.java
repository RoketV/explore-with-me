package com.explore.mainservice.event.service;

import com.explore.mainservice.admin.enums.StateAction;
import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.service.CategoryService;
import com.explore.mainservice.event.dto.*;
import com.explore.mainservice.event.enums.StateEvent;
import com.explore.mainservice.event.jpa.EventPersistService;
import com.explore.mainservice.event.mapper.EventMapper;
import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.location.dto.LocationDto;
import com.explore.mainservice.location.service.LocationService;
import com.explore.mainservice.statistics.StatisticService;
import com.explore.mainservice.user.dto.UserShortDto;
import com.explore.mainservice.user.service.UserService;
import com.explore.statdtos.dtos.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventPersistService eventPersistService;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;
    private final StatisticService statisticService;


    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {

        List<Event> events = eventPersistService.findUserEvents(userId, from, size).getContent();

        if (events.isEmpty()) {

            return Collections.emptyList();
        }

        return events.stream()
                .map(event -> {
                    CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
                    UserShortDto user = userService.getUserShortById(event.getInitiatorId());
                    return eventMapper.toEventShortDto(event, category, user);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        if (eventDto.getAnnotation() == null) {
            throw new BadRequestException("Bad request body", "Event annotation is empty");
        }
        LocationDto location = locationService.save(eventDto.getLocation());
        eventDto.setLocation(location);

        Event event = eventMapper.toEventWithUserId(userId, eventDto);

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + event.getEventDate());
        }

        event.setState(StateEvent.PENDING);
        event = eventPersistService.createEvent(event);

        CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
        UserShortDto user = userService.getUserShortById(event.getInitiatorId());

        return eventMapper.toFullEventDto(event, category, user, location);

    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {

        Event event = eventPersistService.findUserEventById(userId, eventId);

        if (event == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", eventId));
        }

        CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
        UserShortDto user = userService.getUserShortById(event.getInitiatorId());
        LocationDto location = locationService.getLocationById(event.getLocation().getId());

        return eventMapper.toFullEventDto(event, category, user, location);
    }

    @Override
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequestDto updateEventDto) {

        Event event = eventPersistService.findUserEventById(userId, eventId);
        StateEvent stateEvent = event.getState();
        LocalDateTime updateEventDate = updateEventDto.getEventDate();

        if (StateEvent.PUBLISHED.equals(stateEvent)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed");
        }

        if (event.getId() == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        if (updateEventDate != null && !updateEventDate.isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + updateEventDate);
        }

        eventMapper.mergeToEvent(updateEventDto, event);
        event.setState(getStateEvent(event, updateEventDto.getStateAction()));
        event.setInitiatorId(userId);

        CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
        UserShortDto user = userService.getUserShortById(event.getInitiatorId());
        LocationDto location = locationService.getLocationById(event.getLocation().getId());

        return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
    }

    @Override
    public List<EventFullDto> getFullEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, int from, int size) {

        List<Event> events = eventPersistService.getFullEvents(
                users, states, categories, rangeStart, rangeEnd, from, size).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .map(event -> {
                    CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
                    UserShortDto user = userService.getUserShortById(event.getInitiatorId());
                    LocationDto location = locationService.getLocationById(event.getLocation().getId());
                    return eventMapper.toFullEventDto(event, category, user, location);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventDto, Long eventId) {

        Optional<Event> eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", eventId));
        }

        Event event = eventOpt.get();

        LocalDateTime eventUpdateDate = updateEventDto.getEventDate();
        LocalDateTime createdOn = event.getCreatedOn();

        if (eventUpdateDate != null && !eventUpdateDate.isAfter(createdOn.plusHours(1))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + createdOn);
        }

        if (StateAction.PUBLISH_EVENT.equals(updateEventDto.getStateAction())) {

            if (StateEvent.PUBLISHED.equals(event.getState()) || StateEvent.CANCELED.equals(event.getState())) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            eventMapper.mergeToEventAdmin(updateEventDto, event);
            event.setState(StateEvent.PUBLISHED);

            CategoryDto  category = categoryService.getCategoryById(event.getCategoryId());
            UserShortDto user = userService.getUserShortById(event.getInitiatorId());
            LocationDto  location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
        }

        if (StateAction.REJECT_EVENT.equals(updateEventDto.getStateAction())) {
            if (event.getState().equals(StateEvent.PUBLISHED)) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            eventMapper.mergeToEventAdmin(updateEventDto, event);
            event.setState(StateEvent.CANCELED);

            CategoryDto  category = categoryService.getCategoryById(event.getCategoryId());
            UserShortDto user = userService.getUserShortById(event.getInitiatorId());
            LocationDto  location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
        }

        throw new ConflictException("For the requested operation the conditions are not met.",
                "Cannot publish the event because it's not in the right state: PUBLISHED");
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, int from, int size, HttpServletRequest request) {

        List<Event> eventsPublic = eventPersistService.getEventsPublic(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size).getContent();

        if (eventsPublic.isEmpty()) {
            return Collections.emptyList();
        }

        String end = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        return eventsPublic.stream()
                .map(event -> {
                    CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
                    UserShortDto user = userService.getUserShortById(event.getInitiatorId());

                    List<ViewStatsDto> stat =
                            statisticService.getViews("1900-01-01 00:00:00", end,
                                    List.of(request.getRequestURI() + "/" + event.getId()), true);
                    Long view = stat.isEmpty() ? 0 : stat.get(0).getHits();
                    event.setViews(view);


                    return eventMapper.toEventShortDto(event, category, user);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventPublicById(Long id, HttpServletRequest request) {

        EventFullDto event = findEventById(id);

        if (event == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", id));
        }
        if (!StateEvent.PUBLISHED.equals(event.getState())) {
            return null;
        }

        String end = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        List<ViewStatsDto> stat = statisticService.getViews("1900-01-01 00:00:00", end,
                List.of(request.getRequestURI() + "/" + event.getId()), true);

        Long view = stat.isEmpty() ? 0 : stat.get(0).getHits();
        event.setViews(view);

        return event;
    }

    @Override
    public EventFullDto findEventById(Long eventId) {

        Optional<Event> eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isPresent()) {

            Event event = eventOpt.get();

            CategoryDto  category = categoryService.getCategoryById(event.getCategoryId());
            UserShortDto user = userService.getUserShortById(event.getInitiatorId());
            LocationDto  location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(event, category, user, location);
        }

        throw new NotFoundException("The required object was not found.",
                String.format("Event with id = %s was not found", eventId));
    }

    @Override
    public EventShortDto findEventShortById(Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isPresent()) {

            Event event = eventOpt.get();
            CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
            UserShortDto user = userService.getUserShortById(event.getInitiatorId());

            return eventMapper.toEventShortDto(event, category, user);
        }

        throw new NotFoundException("The required object was not found.",
                String.format("Event with id = %s was not found ", eventId));

    }

    @Override
    public void increment(Long eventId) {

        Optional<Event> eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        Event event = eventOpt.get();

        Integer confirmedRequests =
                event.getConfirmedRequests() == null ||
                        event.getConfirmedRequests() == 0 ? 1
                        : event.getConfirmedRequests() + 1;
        event.setConfirmedRequests(confirmedRequests);
        eventPersistService.saveEvent(event);


    }

    @Override
    public void decrement(Long eventId) {

        Optional<Event> eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        Event event = eventOpt.get();

        Integer confirmedRequests =
                event.getConfirmedRequests() == null ||
                        event.getConfirmedRequests() == 0 ? 0
                        : event.getConfirmedRequests() - 1;
        event.setConfirmedRequests(confirmedRequests);
        eventPersistService.saveEvent(event);

    }


    private StateEvent getStateEvent(Event event, StateAction stateAction) {

        switch (stateAction) {
            case CANCEL_REVIEW:
                return StateEvent.CANCELED;
            case PUBLISH_EVENT:
                return StateEvent.PUBLISHED;
            case SEND_TO_REVIEW:
                return StateEvent.PENDING;
            default:
                return event.getState();

        }

    }

}
