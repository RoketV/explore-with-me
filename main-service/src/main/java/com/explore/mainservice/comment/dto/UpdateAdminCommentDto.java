package com.explore.mainservice.comment.dto;

import com.explore.mainservice.admin.enums.StateAction;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateAdminCommentDto {

    @Length(max = 3000, min = 20)
    private String content;

    @Enumerated(EnumType.STRING)
    private StateAction stateAction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateAdminCommentDto)) return false;
        UpdateAdminCommentDto that = (UpdateAdminCommentDto) o;
        return Objects.equals(getContent(), that.getContent()) && Objects.equals(getStateAction(), that.getStateAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent(), getStateAction());
    }
}
