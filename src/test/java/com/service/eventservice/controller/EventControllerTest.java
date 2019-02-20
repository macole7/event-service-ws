package com.service.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.service.EventService;
import org.junit.Assert;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EventController.class, secure = false)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private Event event;
    private static final int OK_STATUS = 200;
    private static final int NOT_FOUND_STATUS = 404;
    private ObjectMapper objectMapper;
    private String dateAsString = "2019-01-01";
    private LocalDate date = LocalDate.parse(dateAsString, DateTimeFormatter.ISO_LOCAL_DATE);

    @Before
    public void setUp() {
        event = new Event("party", date, "Wroclaw", new Organizer());
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnAllEventsAndStatus200() throws Exception {
        //given
        List<Event> events = Arrays.asList(event, event);
        when(eventService.findAllEvents()).thenReturn(events);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnEventByIdAndStatus200() throws Exception {
        //given
        when(eventService.findById(1)).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventDoesNotExist() throws Exception {
        //given
        when(eventService.findById(1)).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnEventsByNameAndStatus200() throws Exception {
        //given
        List<Event> events = Arrays.asList(event, event);
        when(eventService.findByName("party")).thenReturn(events);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/name?name=party")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnEventsByAddressAndStatus200() throws Exception {
        //given
        List<Event> events = Arrays.asList(event, event);
        when(eventService.findByAddress("Wroclaw")).thenReturn(events);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/address?address=Wroclaw")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventsByNameDoNotExist() throws Exception {
        //given
        when(eventService.findByName("party")).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/name?name=party")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventsByAddressDoNotExist() throws Exception {
        //given
        when(eventService.findByAddress("Wroclaw")).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/address?address=Wroclaw")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnEventsByDateAndStatus200() throws Exception {
        //given
        String dateAsString = "2019-01-01";
        LocalDate date = LocalDate.parse(dateAsString);
        List<Event> events = Arrays.asList(event, event);
        when(eventService.findByDate(date)).thenReturn(events);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/date?date=2019-01-01")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventsByDateDoNotExist() throws Exception {
        //given
        String dateAsString = "2019-01-01";
        LocalDate date = LocalDate.parse(dateAsString);
        when(eventService.findByDate(date)).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/date?date=2019-01-01")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnCreatedEventAndStatus201() throws Exception {
        //given
        int createdStatus = 201;
        String expectedEvent = objectMapper.writeValueAsString(new Event());
        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedEvent);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(createdStatus, status);
    }

    @Test
    public void shouldReturnUpdatedEventAndStatus200() throws Exception {
        //given
        when(eventService.updateEvent(event, 1)).thenReturn(event);
        String expectedEvent = objectMapper.writeValueAsString(new Event());

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedEvent);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventByIdDoNotExistDuringUpdating() throws Exception {
        //given
        when(eventService.updateEvent(new Event(), 1)).thenThrow(EventNotFoundException.class);
        String expectedEvent = objectMapper.writeValueAsString(new Event());

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedEvent);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnEventByIdAndStatus200DuringDeleting() throws Exception {
        //given
        when(eventService.deleteEvent(1)).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenEventDoesNotExistDuringDeleting() throws Exception {
        //given
        when(eventService.deleteEvent(1)).thenThrow(EventNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        Assert.assertEquals(NOT_FOUND_STATUS, status);
    }
}
