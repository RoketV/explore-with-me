package com.explore.mainservice.event.dto;

import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.user.dto.UserShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventShortDto)) return false;
        EventShortDto that = (EventShortDto) o;
        return Objects.equals(getAnnotation(), that.getAnnotation())
                && Objects.equals(getCategory(), that.getCategory())
                && Objects.equals(getConfirmedRequests(), that.getConfirmedRequests())
                && Objects.equals(getEventDate(), that.getEventDate()) && Objects.equals(getId(), that.getId())
                && Objects.equals(getInitiator(), that.getInitiator()) && Objects.equals(getPaid(), that.getPaid())
                && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getViews(), that.getViews());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnnotation(), getCategory(), getConfirmedRequests(), getEventDate(), getId(),
                getInitiator(), getPaid(), getTitle(), getViews());
    }
}

