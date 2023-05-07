package com.explore.mainservice.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCommentDto {

    @NotBlank
    private String content;
}

