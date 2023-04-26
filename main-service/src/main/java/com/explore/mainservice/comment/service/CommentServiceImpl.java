package com.explore.mainservice.comment.service;

import com.explore.mainservice.admin.enums.StateAction;
import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.service.CategoryService;
import com.explore.mainservice.comment.dto.CommentDto;
import com.explore.mainservice.comment.dto.NewCommentDto;
import com.explore.mainservice.comment.dto.UpdateAdminCommentDto;
import com.explore.mainservice.comment.dto.UpdateUserCommentDto;
import com.explore.mainservice.comment.enums.StateComment;
import com.explore.mainservice.comment.jpa.CommentPersistService;
import com.explore.mainservice.comment.mapper.CommentMapper;
import com.explore.mainservice.comment.model.Comment;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.event.enums.StateEvent;
import com.explore.mainservice.event.jpa.EventPersistService;
import com.explore.mainservice.event.mapper.EventMapper;
import com.explore.mainservice.event.model.Event;
import com.explore.mainservice.event.service.EventService;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import com.explore.mainservice.user.dto.UserShortDto;
import com.explore.mainservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentPersistService commentPersistService;

    private final CommentMapper commentMapper;

    private final EventMapper eventMapper;

    private final EventPersistService eventPersistService;

    private final EventService eventService;

    private final UserService userService;

    private final CategoryService categoryService;


    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {

        Optional<Event> eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", eventId));
        }

        Event event = eventOpt.get();

        if (!StateEvent.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Cannot publish the event because it's not in the right state: " +
                            "PENDING or CANCELED");
        }

        Comment comment = commentMapper.toCommentWithUserIdAndEventId(userId, eventId, newCommentDto);

        comment.setState(StateComment.PENDING);

        comment = commentPersistService.addComment(comment);

        UserShortDto user = userService.getUserShortById(event.getInitiatorId());
        CategoryDto category = categoryService.getCategoryById(event.getCategoryId());
        EventShortDto eventResult = eventMapper.toEventShortDto(event, category, user);

        return commentMapper.toCommentDto(comment, user, eventResult);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {

        CommentDto comment = findUserCommentById(userId, commentId);

        if (StateComment.PUBLISHED.equals(comment.getState())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Cannot delete the comment because it's not in the right state: PUBLISHED");
        } else {
            commentPersistService.deleteComment(commentId);
        }
    }

    @Override
    public List<CommentDto> getCommentsPublic(String[] sort, int from, int size) {

        Page<Comment> comments = commentPersistService.getCommentsPublic(sort, from, size);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(comment -> {
                    UserShortDto userShort = userService.getUserShortById(comment.getWriterId());
                    var eventShort = eventService.findEventShortById(comment.getEventId());
                    return commentMapper.toCommentDto(comment, userShort, eventShort);
                }).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentPublicById(Long id) {

        Optional<Comment> commentOpt = commentPersistService.findCommentById(id);

        if (commentOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Comment with id = %s was not found", id));
        }
        Comment comment = commentOpt.get();

        if (!StateComment.PUBLISHED.equals(comment.getState())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Cannot delete the comment because it's not in the right state: PENDING or CANCELED.");
        }

        UserShortDto userShort = userService.getUserShortById(comment.getWriterId());
        EventShortDto eventShort = eventService.findEventShortById(comment.getEventId());

        return commentMapper.toCommentDto(comment, userShort, eventShort);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateUserCommentDto userCommentDto) {

        Comment comment = commentPersistService.findUserCommentById(userId, commentId);
        StateComment stateComment = comment.getState();

        if (StateComment.PUBLISHED.equals(stateComment)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Only pending or canceled comment can be changed");
        }

        commentMapper.mergeToComment(userCommentDto, comment);
        comment.setState(getStateComment(userCommentDto.getStateAction()));
        comment.setWriterId(userId);

        commentPersistService.updateComment(comment);

        UserShortDto userShort = userService.getUserShortById(comment.getWriterId());
        EventShortDto eventShort = eventService.findEventShortById(comment.getEventId());

        return commentMapper.toCommentDto(comment, userShort, eventShort);
    }

    @Override
    public CommentDto updateCommentByAdmin(UpdateAdminCommentDto updateAdminComment, Long commentId) {

        Optional<Comment> commentOpt = commentPersistService.findCommentById(commentId);

        if (commentOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Comment with id = %s was not found", commentId));
        }

        Comment comment = commentOpt.get();

        if (StateAction.PUBLISH_COMMENT.equals(updateAdminComment.getStateAction())) {

            if (StateComment.PUBLISHED.equals(comment.getState()) || StateComment.CANCELED.equals(comment.getState())) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the comment because it's not in the right state: PUBLISHED");
            }

            commentMapper.mergeToCommentAdmin(updateAdminComment, comment);

            comment.setState(StateComment.PUBLISHED);
            commentPersistService.updateComment(comment);

            UserShortDto userShort = userService.getUserShortById(comment.getWriterId());
            EventShortDto eventShort = eventService.findEventShortById(comment.getEventId());

            return commentMapper.toCommentDto(comment, userShort, eventShort);
        }

        if (StateAction.REJECT_COMMENT.equals(updateAdminComment.getStateAction())) {

            if (StateComment.PUBLISHED.equals(comment.getState())) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the comment because it's not in the right state: PUBLISHED");
            }
            commentMapper.mergeToCommentAdmin(updateAdminComment, comment);
            comment.setState(StateComment.CANCELED);

            commentPersistService.updateComment(comment);

            UserShortDto userShort = userService.getUserShortById(comment.getWriterId());
            EventShortDto eventShort = eventService.findEventShortById(comment.getEventId());

            return commentMapper.toCommentDto(comment, userShort, eventShort);
        }

        throw new ConflictException("For the requested operation the conditions are not met.",
                "Cannot publish the comment because it's not in the right state: PUBLISHED");
    }

    @Override
    public CommentDto findUserCommentById(Long userId, Long commentId) {

        CommentDto comment = commentMapper.toCommentDto(
                commentPersistService.findUserCommentById(userId, commentId));
        if (comment == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Comment with id = %s was not found", commentId));
        }

        return comment;
    }

    @Override
    public CommentDto findCommentById(Long id) {

        Optional<Comment> commentOpt = commentPersistService.findCommentById(id);

        if (commentOpt.isPresent()) {

            Comment comment = commentOpt.get();
            EventShortDto eventShort = eventService.findEventShortById(comment.getEventId());
            UserShortDto userShort = userService.getUserShortById(comment.getWriterId());

            return commentMapper.toCommentDto(comment, userShort, eventShort);
        }

        throw new NotFoundException("The required object was not found.",
                String.format("Event with id = %s was not found ", id));

    }

    private StateComment getStateComment(StateAction stateAction) {

        switch (stateAction) {
            case CANCEL_REVIEW:
                return StateComment.CANCELED;
            case PUBLISH_COMMENT:
                return StateComment.PUBLISHED;
            case SEND_TO_REVIEW:
                return StateComment.PENDING;
            default:
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the comment because it's not in the right stateAction");

        }
    }
}

