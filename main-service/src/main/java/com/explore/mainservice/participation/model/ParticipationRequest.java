package com.explore.mainservice.participation.model;

import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.participation.enums.ParticipationStatus;
import com.explore.mainservice.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "participation_request_t")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "requester_Id")
    @ManyToOne
    private User requester;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    private LocalDateTime created;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipationRequest)) return false;
        ParticipationRequest that = (ParticipationRequest) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getRequester(), that.getRequester()) && Objects.equals(getEvent(), that.getEvent()) && getStatus() == that.getStatus() && Objects.equals(getCreated(), that.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRequester(), getEvent(), getStatus(), getCreated());
    }
}