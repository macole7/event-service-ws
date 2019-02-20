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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Optional.*;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private User user;
    private Event event;
    private Comment comment;

    @Before
    public void setUp() {
        user = new User("password", "username", "m@gmail.com");
        event = new Event();
        comment = new Comment("Great!", user, event);
    }

    @Test
    public void shouldReturnCreatedComment() {
        //given
        when(userRepository.findById(1L)).thenReturn(of(user));
        when(eventRepository.findById(1L)).thenReturn(of(event));
        when(commentRepository.save(comment)).thenReturn(comment);
        //when
        Comment createdComment = commentService.createUserCommentForGivenEvent(comment, 1, 1);
        //when
        assertEquals(comment, createdComment);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenGivenUserDoesNotExist() {
        //given
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        //when
        commentService.createUserCommentForGivenEvent(new Comment(), 1, 1);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowEventNotFoundExceptionWhenGivenEventDoesNotExist() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(of(new User()));
        when(eventRepository.findById(anyLong())).thenThrow(EventNotFoundException.class);
        //when
        commentService.createUserCommentForGivenEvent(new Comment(), 1, 1);
    }

    @Test
    public void shouldReturnAllCommentsForGivenUser() {
        //given
        List<Comment> comments = Arrays.asList(new Comment("hello", user, event),
                new Comment("hi", user, event));
        when(commentRepository.findByEventIdAndUserId(1L, 1L)).thenReturn(comments);
        //when
        List<Comment> allUserCommentsForEvent = commentService.getAllUserCommentsForEvent(1L, 1L);
        assertEquals(comments, allUserCommentsForEvent);
    }

    @Test
    public void shouldReturnAllCommentsForEvent() {
        //given
        List<Comment> comments = Arrays.asList(new Comment("hello", user, event),
                new Comment("hi", user, event));
        when(commentRepository.findByEventId(1L)).thenReturn(comments);
        //when
        List<Comment> allCommentsForEvent = commentService.getAllCommentsForEvent(1L);
        assertEquals(comments, allCommentsForEvent);
    }

    @Test
    public void shouldReturnAllCommentsForUser() {
        //given
        List<Comment> comments = Arrays.asList(new Comment("hello", user, event),
                new Comment("hi", user, event));
        when(commentRepository.findByUserId(1L)).thenReturn(comments);
        //when
        List<Comment> allCommentsForUser = commentService.getAllCommentsForUser(1L);
        //then
        assertEquals(comments, allCommentsForUser);
    }

    @Test
    public void shouldReturnDeletedComment() {
        //given
        when(commentRepository.findById(1L)).thenReturn(ofNullable(comment));
        //when
        Comment deletedComment = commentService.deleteCommentById(1L);
        //then
        assertEquals(comment, deletedComment);
    }

    @Test(expected = CommentNotFoundException.class)
    public void shouldThrowCommentNotFoundExceptionDuringDeletingNotExistingComment() {
        when(commentRepository.findById(1L)).thenThrow(CommentNotFoundException.class);
        commentService.deleteCommentById(1);
    }

    @Test
    public void shouldReturnCommentByCommentId() {
        //given
        when(commentRepository.findById(1L)).thenReturn(ofNullable(comment));
        //when
        Comment commentById = commentService.findById(1L);
        //then
        assertEquals(comment, commentById);
    }

    @Test(expected = CommentNotFoundException.class)
    public void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExist() {
        when(commentRepository.findById(1L)).thenThrow(CommentNotFoundException.class);
        commentService.findById(1L);
    }
}