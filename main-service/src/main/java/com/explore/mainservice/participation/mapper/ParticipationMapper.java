package com.explore.mainservice.participation.mapper;

import com.explore.mainservice.participation.dto.ParticipationRequestDto;
import com.explore.mainservice.participation.model.ParticipationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toParticipationDto(ParticipationRequest entity);
}