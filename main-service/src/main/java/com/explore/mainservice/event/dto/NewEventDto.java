package com.explore.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class NewEventDto {

    @NotNull
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    private String description;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewEventDto)) return false;
        NewEventDto that = (NewEventDto) o;
        return Objects.equals(getAnnotation(), that.getAnnotation())
                && Objects.equals(getCategory(), that.getCategory())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getEventDate(), that.getEventDate())
                && Objects.equals(getLocation(), that.getLocation())
                && Objects.equals(getPaid(), that.getPaid())
                && Objects.equals(getParticipantLimit(), that.getParticipantLimit())
                && Objects.equals(getRequestModeration(), that.getRequestModeration())
                && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnnotation(), getCategory(), getDescription(), getEventDate(),
                getLocation(), getPaid(), getParticipantLimit(), getRequestModeration(), getTitle());
    }
}

