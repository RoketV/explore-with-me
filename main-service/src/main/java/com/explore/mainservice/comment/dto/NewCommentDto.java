package com.explore.mainservice.comment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCommentDto {

    @NotNull
    private String content;
}

