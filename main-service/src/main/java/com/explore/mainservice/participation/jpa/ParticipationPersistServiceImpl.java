package com.explore.mainservice.participation.jpa;

import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.participation.model.ParticipationRequest;
import com.explore.mainservice.participation.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParticipationPersistServiceImpl implements ParticipationPersistService {

    private final ParticipationRepository participationRepository;

    @Override
    public List<ParticipationRequest> getEventParticipantsByEventId(Long eventId) {
        return participationRepository.findAllByEventId(eventId);
    }

    @Override
    public Optional<ParticipationRequest> getEventParticipantsById(Long id) {
        return participationRepository.findById(id);
    }

    @Override
    @Transactional
    public ParticipationRequest save(ParticipationRequest request) {
        return participationRepository.save(request);
    }

    @Override
    public Optional<ParticipationRequest> getEventParticipantsByUserId(Long userId) {
        return participationRepository.findParticipationRequestByRequesterId(userId);
    }

    @Override
    @Transactional
    public ParticipationRequest addParticipationRequest(ParticipationRequest participation) {
        return participationRepository.save(participation);
    }

    @Override
    public ParticipationRequest findParticipationByRequesterIdAndEventId(Long requesterId, Long eventId) {
        return participationRepository.findParticipationByRequesterIdAndEventId(requesterId, eventId)
                .orElseThrow(() -> new NotFoundException("Participation Request not found",
                        "check requester id or event id"));
    }
}
