package com.explore.mainservice.comment.jpa;

import com.explore.mainservice.comment.model.Comment;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Optional;

public interface CommentPersistService {

    Comment addComment(Comment comment);

    void deleteComment(Long commentId);

    Page<Comment> getCommentsPublic(String[] sort, int from, int size);

    Optional<Comment> findCommentById(Long id);

    Comment updateComment(Comment comment);

    Comment findUserCommentById(Long userId, Long commentId);

    List<Comment> findAllUserComments(Long userId);

}
