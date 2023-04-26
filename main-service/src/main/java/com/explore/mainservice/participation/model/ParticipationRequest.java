package com.explore.mainservice.participation.model;

import com.explore.mainservice.participation.enums.ParticipationStatus;
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

    private Long requesterId;

    private Long eventId;

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
        return Objects.equals(getId(), that.getId()) && Objects.equals(getRequesterId(), that.getRequesterId()) && Objects.equals(getEventId(), that.getEventId()) && getStatus() == that.getStatus() && Objects.equals(getCreated(), that.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRequesterId(), getEventId(), getStatus(), getCreated());
    }
}