package com.explore.mainservice.comment.dto;

import com.explore.mainservice.admin.enums.StateAction;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateUserCommentDto {

    @NotNull
    @Length(max = 3000, min = 20)
    private String content;

    @Enumerated(EnumType.STRING)
    private StateAction stateAction;

}
