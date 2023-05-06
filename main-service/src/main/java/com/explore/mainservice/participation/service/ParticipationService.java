package com.explore.mainservice.participation.service;

import com.explore.mainservice.participation.dto.EventRequestStatusUpdateRequestDto;
import com.explore.mainservice.participation.dto.EventRequestStatusUpdateResultDto;
import com.explore.mainservice.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {

    List<ParticipationRequestDto> getEventParticipantsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequestDto requestStatusUpdateRequestDto);

    List<ParticipationRequestDto> getUserParticipantsRequests(Long userId);

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}

