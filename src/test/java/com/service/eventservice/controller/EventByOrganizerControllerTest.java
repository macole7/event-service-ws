package com.service.eventservice.controller;

import com.service.eventservice.exception.OrganizerNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.service.EventService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EventByOrganizerController.class, secure = false)
public class EventByOrganizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    public void shouldReturnEventForGivenOrganizerAndReturnStatus200() throws Exception {
        //given
        int STATUS_OK = 200;
        Event event = new Event("party", LocalDate.now(), "Wroclaw", new Organizer());
        when(eventService.findByOrganizerId(1L)).thenReturn(event);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizer/1/events")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_OK, status);
    }

    @Test
    public void shouldReturnStatus404WhenOrganizerDoesNotExist() throws Exception {
        //given
        int STATUS_NOT_FOUND = 404;
        when(eventService.findByOrganizerId(1L)).thenThrow(OrganizerNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizer/1/events")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_NOT_FOUND, status);
    }
}