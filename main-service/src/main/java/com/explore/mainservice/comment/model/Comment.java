package com.explore.mainservice.comment.model;

import com.explore.mainservice.comment.enums.StateComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment_t")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    private Long writerId;

    private Long eventId;

    @Enumerated(EnumType.STRING)
    private StateComment state;

    private LocalDateTime createdOn;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(content, comment.content) && Objects.equals(writerId, comment.writerId) && Objects.equals(eventId, comment.eventId) && state == comment.state && Objects.equals(createdOn, comment.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, writerId, eventId, state, createdOn);
    }
}
