package com.explore.mainservice.comment.mapper;

import com.explore.mainservice.comment.dto.CommentDto;
import com.explore.mainservice.comment.dto.NewCommentDto;
import com.explore.mainservice.comment.dto.UpdateAdminCommentDto;
import com.explore.mainservice.comment.dto.UpdateUserCommentDto;
import com.explore.mainservice.comment.model.Comment;
import com.explore.mainservice.event.dto.EventShortDto;
import com.explore.mainservice.location.mapper.LocationMapper;
import com.explore.mainservice.user.dto.UserShortDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LocationMapper.class})
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", source = "comment.id")
    CommentDto toCommentDto(Comment comment, UserShortDto writer, EventShortDto event);

    @Mapping(target = "writerId", source = "userId")
    @Mapping(target = "eventId", source = "eventId")
    Comment toCommentWithUserIdAndEventId(Long userId, Long eventId, NewCommentDto newCommentDto);

    void mergeToComment(UpdateUserCommentDto userCommentDto, @MappingTarget Comment comment);

    void mergeToCommentAdmin(UpdateAdminCommentDto adminCommentDto, @MappingTarget Comment comment);

}
