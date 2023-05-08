package com.explore.mainservice.comment.controller;

import com.explore.mainservice.comment.dto.CommentDto;
import com.explore.mainservice.comment.dto.UpdateAdminCommentDto;
import com.explore.mainservice.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping(value = "/{commentId}")
    public CommentDto updateCommentByAdmin(@RequestBody UpdateAdminCommentDto updateAdminComment,
                                           @PathVariable("commentId") Long commentId) {
        log.info("Модерация комментария и его статуса.");

        return commentService.updateCommentByAdmin(updateAdminComment, commentId);
    }
}
