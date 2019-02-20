package com.service.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.eventservice.exception.CommentNotFoundException;
import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.Comment;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;
import com.service.eventservice.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CommentController.class, secure = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Comment comment;
    private ObjectMapper objectMapper;
    private static int OK_STATUS = 200;
    private static int NOT_FOUND_STATUS = 404;

    @Before
    public void setUp() {
        comment = new Comment("Party hard", new User(), new Event());
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnAllCommentsForGivenBothUserAndEventAndStatus201() throws Exception {
        //given
        int createdStatus = 201;
        when(commentService.createUserCommentForGivenEvent(comment, 1, 1)).thenReturn(comment);
        String jsonComment = objectMapper.writeValueAsString(comment);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/comments/user/1/event/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonComment);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(createdStatus, status);
    }


    @Test
    public void shouldReturnAllUsersCommentForGivenEventAndStatus200() throws Exception {
        //given
        List<Comment> comments = Arrays.asList(comment, comment);
        when(commentService.getAllUserCommentsForEvent(1, 1)).thenReturn(comments);
        String jsonComments = objectMapper.writeValueAsString(comments);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/user/1/event/1/comments")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonComments, contentAsString);
    }

    @Test
    public void shouldReturnAllCommentsForGivenEventAndStatus200() throws Exception {
        //given
        List<Comment> comments = Arrays.asList(comment, comment);
        when(commentService.getAllCommentsForEvent(1)).thenReturn(comments);
        String jsonComments = objectMapper.writeValueAsString(comments);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/event/1/comments")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonComments, contentAsString);
    }

    @Test
    public void shouldReturnAllCommentsForGivenUseAndStatus200() throws Exception {
        //given
        List<Comment> comments = Arrays.asList(comment, comment);
        when(commentService.getAllCommentsForUser(1)).thenReturn(comments);
        String jsonComments = objectMapper.writeValueAsString(comments);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/user/1/comments")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonComments, contentAsString);
    }

    @Test
    public void shouldReturnCommentByIdAndStatus200() throws Exception {
        when(commentService.findById(1)).thenReturn(comment);
        String jsonComment = objectMapper.writeValueAsString(comment);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/comments/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonComment, contentAsString);
    }

    @Test
    public void shouldReturnDeletedCommentByIdAndStatus200() throws Exception {
        //given
        when(commentService.findById(1)).thenReturn(comment);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                delete("/comments/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventDoesNotExist() throws Exception {
        //given
        when(commentService.getAllCommentsForUser(1)).thenThrow(UserNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/user/1/comments")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenUserDoesNotExist() throws Exception {
        //given
        when(commentService.getAllCommentsForEvent(1)).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/event/1/comments")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenCommentDoesNotExist() throws Exception {
        //given
        when(commentService.findById(1)).thenThrow(CommentNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/comments/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }
}