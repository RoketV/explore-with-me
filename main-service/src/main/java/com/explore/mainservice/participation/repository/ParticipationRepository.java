package com.explore.mainservice.participation.repository;

import com.explore.mainservice.participation.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Optional<ParticipationRequest> findParticipationRequestByRequesterId(Long requesterId);

    ParticipationRequest findParticipationByRequesterIdAndEventId(Long requesterId, Long eventId);
}
