package com.service.eventservice.service;

import com.service.eventservice.model.Comment;

import java.util.List;

public interface CommentService {

    Comment createUserCommentForGivenEvent(Comment comment, long userId, long eventId);

    List<Comment> getAllUserCommentsForEvent(long userId, long eventId);

    List<Comment> getAllCommentsForEvent(long eventId);

    List<Comment> getAllCommentsForUser(long userId);

    Comment deleteCommentById(long id);

    Comment findById(long id);
}
