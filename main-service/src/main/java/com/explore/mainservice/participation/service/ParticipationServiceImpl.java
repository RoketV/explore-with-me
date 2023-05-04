package com.explore.mainservice.participation.service;

import com.explore.mainservice.event.dto.EventFullDto;
import com.explore.mainservice.event.enums.StateEvent;
import com.explore.mainservice.event.jpa.EventPersistService;
import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.event.service.EventService;
import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.participation.dto.EventRequestStatusUpdateRequestDto;
import com.explore.mainservice.participation.dto.EventRequestStatusUpdateResultDto;
import com.explore.mainservice.participation.dto.ParticipationRequestDto;
import com.explore.mainservice.participation.enums.ParticipationStatus;
import com.explore.mainservice.participation.jpa.ParticipationPersistService;
import com.explore.mainservice.participation.mapper.ParticipationMapper;
import com.explore.mainservice.participation.model.ParticipationRequest;
import com.explore.mainservice.user.jpa.UserPersistService;
import com.explore.mainservice.user.model.User;
import com.explore.mainservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.explore.mainservice.participation.enums.ParticipationStatus.*;

@RequiredArgsConstructor
@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationPersistService participationPersistService;
    private final ParticipationMapper participationMapper;
    private final EventService eventService;
    private final UserService userService;

    private final UserPersistService userPersistService;

    private final EventPersistService eventPersistService;

    @Override
    public List<ParticipationRequestDto> getEventParticipantsByEventId(Long userId, Long eventId) {

        return participationPersistService.getEventParticipantsByEventId(eventId)
                .stream()
                .map(participationMapper::toParticipationDto)
                .collect(Collectors.toList());

    }

    @Override
    public EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequestDto requestStatusUpdateRequestDto) {

        EventFullDto event = eventService.getUserEventById(userId, eventId);

        if (event.getConfirmedRequests() != null && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }

        EventRequestStatusUpdateResultDto result = new EventRequestStatusUpdateResultDto();

       List<Long> requestIds = requestStatusUpdateRequestDto.getRequestIds();
        for (Long requestId : requestIds) {

            Optional<ParticipationRequest> requestOpt = participationPersistService.getEventParticipantsById(requestId);

            if (requestOpt.isPresent()) {
                ParticipationRequest request = requestOpt.get();

                if (request.getStatus() != PENDING) {
                    throw new BadRequestException("Incorrectly made request.", "Request must have status PENDING");
                }

                if (REJECTED.equals(requestStatusUpdateRequestDto.getStatus())) {
                    request.setStatus(REJECTED);
                    result.getRejectedRequests().add(participationMapper.toParticipationDto(request));

                } else {
                    request.setStatus(getStatus(userId, eventId));
                }

                request = participationPersistService.save(request);

                if (CONFIRMED.equals(request.getStatus())) {
                    eventService.increment(eventId);
                    result.getConfirmedRequests().add(participationMapper.toParticipationDto(request));
                }
            }
        }
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipantsRequests(Long userId) {

        userService.getUserShortById(userId);

        Optional<ParticipationRequest> requests = participationPersistService.getEventParticipantsByUserId(userId);

        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return participationPersistService.getEventParticipantsByUserId(userId)
                .stream()
                .map(participationMapper::toParticipationDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {

        ParticipationRequest participation;
        try {
            participation = participationPersistService.findParticipationByRequesterIdAndEventId(userId, eventId);
        } catch (NotFoundException e) {
            participation = null;
        }
        if (participation != null &&
                participation.getRequester().getId().equals(userId)
                && participation.getEvent().getId().equals(eventId)) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_request]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        EventFullDto eventDto = eventService.findEventById(eventId);


        if (eventDto.getInitiator().getId().equals(userId) || !StateEvent.PUBLISHED.equals(eventDto.getState())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_request]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        if (eventDto.getConfirmedRequests() != null &&
                Objects.equals(eventDto.getConfirmedRequests(), eventDto.getParticipantLimit())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }

        ParticipationRequest newParticipation = new ParticipationRequest();

        User user = userPersistService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("there is no user with id %s", userId),
                                "no user with this id"));
        Event event = eventPersistService.findEventById(eventId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("there is no event with id %s", userId),
                                "no user with this id"));

        newParticipation.setRequester(user);
        newParticipation.setEvent(event);

        if (eventDto.getRequestModeration() != null && !eventDto.getRequestModeration()) {
            newParticipation.setStatus(ParticipationStatus.CONFIRMED);
            eventService.increment(eventDto.getId());
        } else {
            newParticipation.setStatus(ParticipationStatus.PENDING);
        }

        return participationMapper.toParticipationDto(
                participationPersistService.addParticipationRequest(newParticipation));

    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        Optional<ParticipationRequest> requestOpt = participationPersistService.getEventParticipantsById(requestId);

        if (requestOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Request with id = %s was not found", requestId));
        }

        var request = requestOpt.get();

        request.setStatus(ParticipationStatus.CANCELED);

        request = participationPersistService.save(request);

        if (REJECTED.equals(request.getStatus())) {
            eventService.decrement(request.getEvent().getId());
        }

        return participationMapper.toParticipationDto(request);

    }


    private ParticipationStatus getStatus(Long userId, Long eventId) {

        EventFullDto event = eventService.getUserEventById(userId, eventId);

        if (event.getParticipantLimit() == 0 ||
                (event.getRequestModeration() != null && !event.getRequestModeration())) {
            return CONFIRMED;
        }

        if (event.getConfirmedRequests() != null &&
                event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            return ParticipationStatus.REJECTED;
        }
        return CONFIRMED;
    }

}

