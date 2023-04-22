package com.explore.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequestDto {

    @Length(max = 2000, min = 20)
    private String annotation;

    private Long categoryId;

    @Length(max = 7000, min = 20)
    private String description;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long locationId;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(max = 120, min = 3)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateEventUserRequestDto)) return false;
        UpdateEventUserRequestDto that = (UpdateEventUserRequestDto) o;
        return Objects.equals(annotation, that.annotation) && Objects.equals(categoryId, that.categoryId) && Objects.equals(description, that.description) && Objects.equals(eventDate, that.eventDate) && Objects.equals(locationId, that.locationId) && Objects.equals(paid, that.paid) && Objects.equals(participantLimit, that.participantLimit) && Objects.equals(requestModeration, that.requestModeration) && Objects.equals(stateAction, that.stateAction) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, categoryId, description, eventDate, locationId, paid, participantLimit, requestModeration, stateAction, title);
    }
}

