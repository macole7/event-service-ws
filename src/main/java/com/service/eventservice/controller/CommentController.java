package com.service.eventservice.controller;

import com.service.eventservice.model.Comment;
import com.service.eventservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("comments/user/{userId}/event/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createCommentForGivenUserForGivenEvent(@RequestBody Comment comment, @PathVariable int userId, @PathVariable int eventId) {
        return commentService.createUserCommentForGivenEvent(comment, userId, eventId);
    }

    @GetMapping("/user/{userId}/event/{eventId}/comments")
    public List<Comment> getAllUsersComentForEvent(@PathVariable int userId, @PathVariable int eventId) {
        return commentService.getAllUserCommentsForEvent(userId, eventId);
    }

    @GetMapping("event/{eventId}/comments")
    public List<Comment> getAllCommentsForEvent(@PathVariable int eventId) {
        return commentService.getAllCommentsForEvent(eventId);
    }

    @GetMapping("user/{userId}/comments")
    public List<Comment> getAllCommentsForUser(@PathVariable int userId) {
        return commentService.getAllCommentsForUser(userId);
    }

    @DeleteMapping("/comments/{id}")
    public Comment deleteComment(@PathVariable int id) {
        return commentService.deleteCommentById(id);
    }

    @GetMapping("/comments/{id}")
    public Comment findById(@PathVariable int id) {
        return commentService.findById(id);
    }
}
