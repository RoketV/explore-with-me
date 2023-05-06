package com.explore.mainservice.comment.dto;

import com.explore.mainservice.comment.enums.StateComment;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.user.dto.UserShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentDto {

    private Long id;

    @Length(max = 3000, min = 20)
    private String content;

    @NotNull
    private UserShortDto writer;

    @NotNull
    private EventShortDto event;

    private StateComment state;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getWriter(), that.getWriter()) && Objects.equals(getEvent(), that.getEvent()) && getState() == that.getState() && Objects.equals(getCreatedOn(), that.getCreatedOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getWriter(), getEvent(), getState(), getCreatedOn());
    }
}
