package com.explore.mainservice.event.model;

import com.explore.mainservice.event.enums.StateEvent;
import com.explore.mainservice.location.model.Location;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.mapstruct.Named;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "event_t")
public class Event {

    @NotNull
    @Length(max = 2000, min = 20)
    private String annotation;

    @JoinColumn(name = "category_id")
    private Long categoryId;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    @NotNull
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long initiatorId;

    @ManyToOne
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private StateEvent state;

    @NotNull
    @Length(max = 120, min = 3)
    private String title;

    private Integer views;

    @PrePersist
    public void preInit() {
        this.createdOn = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(annotation, event.annotation)
                && Objects.equals(categoryId, event.categoryId)
                && Objects.equals(confirmedRequests, event.confirmedRequests)
                && Objects.equals(createdOn, event.createdOn)
                && Objects.equals(description, event.description)
                && Objects.equals(eventDate, event.eventDate)
                && Objects.equals(id, event.id)
                && Objects.equals(initiatorId, event.initiatorId)
                && Objects.equals(location, event.location)
                && Objects.equals(paid, event.paid)
                && Objects.equals(participantLimit, event.participantLimit)
                && Objects.equals(publishedOn, event.publishedOn)
                && Objects.equals(requestModeration, event.requestModeration)
                && Objects.equals(state, event.state) && Objects.equals(title, event.title)
                && Objects.equals(views, event.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, categoryId, confirmedRequests, createdOn, description, eventDate, id, initiatorId, location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }
}
