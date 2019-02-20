package com.service.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.eventservice.exception.OrganizerNotFoundException;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.service.OrganizerService;
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
@WebMvcTest(value = OrganizerController.class, secure = false)
public class OrganizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizerService organizerService;
    private Organizer organizer;
    private ObjectMapper objectMapper;
    private static final int OK_STATUS = 200;
    private static final int NOT_FOUND_STATUS = 404;

    @Before
    public void setUp() {
        organizer = new Organizer("Jack", "jack@gmail.com");
        objectMapper = new ObjectMapper();
    }


    @Test
    public void shouldReturnAllOrganizersAndStatus200() throws Exception {
        //given
        List<Organizer> organizers = Arrays.asList(organizer, organizer);
        when(organizerService.findAll()).thenReturn(organizers);
        String jsonOrganizers = objectMapper.writeValueAsString(organizers);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizers")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonOrganizers, contentAsString);
    }

    @Test
    public void shouldReturnOrganizerByIdAndStatus200() throws Exception {
        //given
        when(organizerService.findById(1)).thenReturn(organizer);
        String jsonOrganizers = objectMapper.writeValueAsString(organizer);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizers/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonOrganizers, contentAsString);
    }

    @Test
    public void shouldReturnStatus404WhenOrganizerDoesNotExist() throws Exception {
        //given
        when(organizerService.findById(1)).thenThrow(OrganizerNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizers/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnOrganizersByNameAndStatus200() throws Exception {
        //given
        List<Organizer> organizers = Arrays.asList(organizer, organizer);
        when(organizerService.findByName("Jack")).thenReturn(organizers);
        String jsonOrganizers = objectMapper.writeValueAsString(organizers);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizers/name?name=Jack")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(OK_STATUS, status);
        assertEquals(jsonOrganizers, contentAsString);
    }

    @Test
    public void shouldReturnStatus404WhenOrganizersByNameDoesNotExist() throws Exception {
        //given
        when(organizerService.findByName("Jack")).thenThrow(OrganizerNotFoundException.class);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/organizers/name?name=Jack")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }

    @Test
    public void shouldReturnCreatedOrganizerAndStatus201() throws Exception {
        //given
        int createdStatus = 201;
        when(organizerService.createOrganizer(organizer)).thenReturn(organizer);
        String jsonOrganizers = objectMapper.writeValueAsString(organizer);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/organizers")
                .contentType(MediaType.APPLICATION_JSON).content(jsonOrganizers);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(createdStatus, status);
        assertEquals(jsonOrganizers, contentAsString);
    }

    @Test
    public void shouldReturnUpdatedEventAndStatus200() throws Exception {
        //given
        Organizer updatedUser = new Organizer();
        when(organizerService.updateUserById(organizer, 1)).thenReturn(updatedUser);
        String jsonUpdatedOrganizer = objectMapper.writeValueAsString(updatedUser);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/organizers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdatedOrganizer);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(OK_STATUS, status);
    }

    @Test
    public void shouldReturnStatus404WhenUpdatedOrganizerDoesNotExist() throws Exception {
        //given
        when(organizerService.updateUserById(organizer, 1)).thenThrow(OrganizerNotFoundException.class);
        String jsonUpdatedOrganizer = objectMapper.writeValueAsString(organizer);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/organizers/1")
                .contentType(MediaType.APPLICATION_JSON).content(jsonUpdatedOrganizer);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(NOT_FOUND_STATUS, status);
    }
}
