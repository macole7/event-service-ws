package com.service.eventservice.service;

import com.service.eventservice.exception.CommentNotFoundException;
import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.Comment;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;
import com.service.eventservice.repository.CommentRepository;
import com.service.eventservice.repository.EventRepository;
import com.service.eventservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Comment createUserCommentForGivenEvent(Comment comment, long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event does not exist " + eventId));
        comment.setEvent(event);
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllUserCommentsForEvent(long userId, long eventId) {
        return commentRepository.findByEventIdAndUserId(userId, eventId);
    }

    @Override
    public List<Comment> getAllCommentsForEvent(long eventId) {
        return commentRepository.findByEventId(eventId);
    }

    @Override
    public List<Comment> getAllCommentsForUser(long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public Comment deleteCommentById(long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment does not exist " + id));
        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public Comment findById(long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Comment does not exist " + id));
    }
}
