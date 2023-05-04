package com.explore.mainservice.participation.repository;

import com.explore.mainservice.participation.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Optional<ParticipationRequest> findParticipationRequestByRequesterId(Long requesterId);

    @Query("from ParticipationRequest pr where pr.requester.id=:requesterId and pr.event.id=:eventId" )
    Optional<ParticipationRequest> findParticipationByRequesterIdAndEventId(Long requesterId, Long eventId);
}
