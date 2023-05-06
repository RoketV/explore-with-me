package com.explore.mainservice.event.mapper;

import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.jpa.CategoryPersistService;
import com.explore.mainservice.event.dto.*;
import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.location.dto.LocationDto;
import com.explore.mainservice.location.mapper.LocationMapper;
import com.explore.mainservice.user.dto.UserShortDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LocationMapper.class, CategoryPersistService.class})
public interface EventMapper {

    @Mapping(target = "id", source = "event.id")
    EventFullDto toFullEventDto(Event event, CategoryDto category,
                                UserShortDto initiator, LocationDto location);

    @Mapping(target = "id", source = "event.id")
    EventShortDto toEventShortDto(Event event, CategoryDto category,
                                  UserShortDto initiator);

    @Mapping(target = "initiatorId", source = "userId")
    @Mapping(target = "category", source = "eventDto.category")
    @Mapping(target = "location", source = "eventDto.location")
    Event toEventWithUserId(Long userId, NewEventDto eventDto);

    @Mapping(target = "category", source = "updateEventDto.categoryId")
    void mergeToEvent(UpdateEventUserRequestDto updateEventDto, @MappingTarget Event event);

    void mergeToEventAdmin(UpdateEventAdminRequestDto updateEventAdminDto, @MappingTarget Event event);

}
