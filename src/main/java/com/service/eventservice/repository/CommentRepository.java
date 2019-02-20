package com.service.eventservice.repository;

import com.service.eventservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventId(long eventId);

    List<Comment> findByEventIdAndUserId(long eventId, long userId);

    List<Comment> findByUserId(long userId);
}
