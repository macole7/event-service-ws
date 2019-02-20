package com.service.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.model.User;
import com.service.eventservice.service.ParticipationService;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ParticipationController.class, secure = false)
public class ParticipationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ParticipationService participationService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Event event;
    private User user;
    private static final int OK_STATUS = 200;
    private static final int NOT_FOUND_STATUS = 404;

    @Before
    public void setUp() {
        user = new User("password", "username", "m2@gmail.com");
        event = new Event("party", LocalDate.now(), "Wroclaw", new Organizer());
    }

    @Test
    public void shouldReturnUsersByEventIdAndStatus200() throws Exception {
        //given
        Set<User> users = Collections.singleton(user);
        when(participationService.findByEventId(1)).thenReturn(users);
        String jsonUsers = objectMapper.writeValueAsString(users);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/1/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(jsonUsers, contentAsString);
        assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventDoesNotExist() throws Exception {
        //given
        when(participationService.findByEventId(1)).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/1/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnEventsByUserIdAndStatus200() throws Exception {
        //given
        List<Event> events = Collections.singletonList(event);
        when(participationService.findByUserId(1)).thenReturn(events);
        String jsonEvents = objectMapper.writeValueAsString(events);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/1/events")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenUserDoesNotExist() throws Exception {
        //given
        when(participationService.findByUserId(1)).thenThrow(UserNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/1/events")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnCreatedEventAndStatus201() throws Exception {
        //given
        int createdStatus = 201;
        when(participationService.addUserToEvent(1, 1)).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/1/users/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(createdStatus, status);
    }

    @Test
    public void shouldReturnDeletedEventAndStatus200() throws Exception {
        //given
        when(participationService.addUserToEvent(1, 1)).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/1/users/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(OK_STATUS, status);
    }
}