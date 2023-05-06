package com.explore.mainservice.user.controller;

import com.explore.mainservice.comment.dto.CommentDto;
import com.explore.mainservice.comment.dto.NewCommentDto;
import com.explore.mainservice.comment.dto.UpdateUserCommentDto;
import com.explore.mainservice.comment.service.CommentService;
import com.explore.mainservice.event.dto.EventFullDto;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.event.dto.NewEventDto;
import com.explore.mainservice.event.dto.UpdateEventUserRequestDto;
import com.explore.mainservice.event.service.EventService;
import com.explore.mainservice.participation.dto.EventRequestStatusUpdateRequestDto;
import com.explore.mainservice.participation.dto.EventRequestStatusUpdateResultDto;
import com.explore.mainservice.participation.dto.ParticipationRequestDto;
import com.explore.mainservice.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final EventService eventService;
    private final ParticipationService participationService;
    private final CommentService commentService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable("userId") Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получение событий текущего пользователя с id = " + userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @RequestBody NewEventDto eventDto) {
        log.info("Добавление события.");
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable("userId") Long userId,
                                         @PathVariable("eventId") Long eventId) {
        log.info("Получение полной информации о событии с id = " + eventId +
                " текущего пользователя с id = " + userId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventById(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId,
                                        @RequestBody UpdateEventUserRequestDto updateEvent) {
        log.info("Обновление информации о событии с id = " + eventId + " текущего пользователя с id = " + userId);

        return eventService.updateEventById(userId, eventId, updateEvent);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipantsByEventId(@PathVariable("userId") Long userId,
                                                                       @PathVariable("eventId") Long eventId) {
        log.info("Получение информации о запросах на участие в событии c id " + eventId +
                "текущего пользователя с id " + userId);
        return participationService.getEventParticipantsByEventId(userId, eventId);
    }


    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeRequestStatus(@PathVariable("userId") Long userId,
                                                                 @PathVariable("eventId") Long eventId,
                                                                 @RequestBody EventRequestStatusUpdateRequestDto
                                                                         requestStatusUpdateRequestDto) {
        log.info("Изменение статуса заявок на участие в событии c id " + eventId +
                "текущего пользователя с id " + userId);
        return participationService.changeRequestStatus(userId, eventId, requestStatusUpdateRequestDto);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable("userId") Long userId) {
        log.info("Получение информации о заявках пользователя с id " + userId +
                " на участие в чужих событиях");
        return participationService.getUserParticipantsRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable("userId") Long userId,
                                                           @RequestParam Long eventId) {
        log.info("Добавление запроса от текущего пользователя с id " + userId + " на участие в событии");
        return participationService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Отмена запроса на участие в событии c id " + requestId + " пользователя с id " + userId);
        return participationService.cancelRequest(userId, requestId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable("userId") Long userId,
                                 @RequestParam Long eventId,
                                 @RequestBody NewCommentDto newCommentDto) {
        log.info("Добавление комментария от текущего пользователя с id " + userId + " к событию с id " + eventId);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable("userId") Long userId,
                                    @PathVariable("commentId") Long commentId,
                                    @RequestBody UpdateUserCommentDto userCommentDto) {
        log.info("Изменение не опубликованного комментария текущего пользователя с id " + userId);
        return commentService.updateComment(userId, commentId, userCommentDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("userId") Long userId,
                              @RequestParam Long eventId,
                              @PathVariable("commentId") Long commentId) {
        log.info("Удаление не опубликованного комментария от текущего пользователя с id " + userId +
                " к событию с id " + eventId);
        commentService.deleteComment(userId, eventId, commentId);
    }
}
