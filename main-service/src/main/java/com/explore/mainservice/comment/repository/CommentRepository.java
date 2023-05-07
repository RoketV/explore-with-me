package com.explore.mainservice.comment.repository;

import com.explore.mainservice.comment.enums.StateComment;
import com.explore.mainservice.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findCommentByIdAndWriterId(Long commentId, Long userId);

    @Query(value = "FROM Comment com WHERE com.state = :state ")
    Page<Comment> findAllByState(@Param("state") StateComment state, Pageable page);

    List<Comment> findAllByWriterId(Long writerId);
}
