package com.explore.mainservice.event.dto;

import com.explore.mainservice.event.enums.StateEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EventDto {

    private String annotation;

    private Long categoryId;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private Long initiatorId;

    private Long locationId;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private StateEvent state;

    private String title;

    private Integer views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDto)) return false;
        EventDto eventDto = (EventDto) o;
        return Objects.equals(annotation, eventDto.annotation) && Objects.equals(categoryId, eventDto.categoryId)
                && Objects.equals(confirmedRequests, eventDto.confirmedRequests)
                && Objects.equals(createdOn, eventDto.createdOn)
                && Objects.equals(description, eventDto.description)
                && Objects.equals(eventDate, eventDto.eventDate)
                && Objects.equals(id, eventDto.id) && Objects.equals(initiatorId, eventDto.initiatorId)
                && Objects.equals(locationId, eventDto.locationId) && Objects.equals(paid, eventDto.paid)
                && Objects.equals(participantLimit, eventDto.participantLimit)
                && Objects.equals(publishedOn, eventDto.publishedOn)
                && Objects.equals(requestModeration, eventDto.requestModeration)
                && Objects.equals(state, eventDto.state) && Objects.equals(title, eventDto.title)
                && Objects.equals(views, eventDto.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, categoryId, confirmedRequests, createdOn, description, eventDate, id,
                initiatorId, locationId, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }
}
