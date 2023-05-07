package com.explore.mainservice.comment.controller;

import com.explore.mainservice.comment.dto.CommentDto;
import com.explore.mainservice.comment.dto.NewCommentDto;
import com.explore.mainservice.comment.dto.UpdateUserCommentDto;
import com.explore.mainservice.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-Explore-User-Id") @NotNull Long userId,
                                 @RequestParam Long eventId,
                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Добавление комментария от текущего пользователя с id " + userId + " к событию с id " + eventId);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestHeader("X-Explore-User-Id") @NotNull Long userId,
                                    @PathVariable("commentId") Long commentId,
                                    @RequestBody @Valid UpdateUserCommentDto userCommentDto) {
        log.info("Изменение не опубликованного комментария текущего пользователя с id " + userId);
        return commentService.updateComment(userId, commentId, userCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader("X-Explore-User-Id") @NotNull Long userId,
                              @RequestParam Long eventId,
                              @PathVariable("commentId") Long commentId) {
        log.info("Удаление не опубликованного комментария от текущего пользователя с id " + userId +
                " к событию с id " + eventId);
        commentService.deleteComment(userId, eventId, commentId);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@RequestHeader("X-Explore-User-Id") @NotNull Long userId,
                                 @PathVariable("commentId") Long commentId) {
        log.info("Получение комментария c id" + commentId + "для пользователя с id" + userId);
        return commentService.findUserCommentById(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@RequestHeader("X-Explore-User-Id") @NotNull Long userId) {
        log.info("Получение всех комментариев пользователя с id" + userId);
        return commentService.getUserComments(userId);
    }
}
