package com.service.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.User;
import com.service.eventservice.service.UserServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final List<User> users = generateListOfPeople();
    private static final int STATUS_OK = 200;
    private static final int STATUS_NOT_FOUND = 404;

    @Test
    public void shouldReturnAllUsersAndStatusOk() throws Exception {
        //given
        String jsonUsers = objectMapper.writeValueAsString(users);

        //when
        when(userService.findAllUsers()).thenReturn(users);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(STATUS_OK, status);
        assertEquals(jsonUsers, contentAsString);
    }

    @Test
    public void shouldReturnUserByUserIdAndStatusOk() throws Exception {
        //given
        User user = users.get(0);
        String jsonUser = objectMapper.writeValueAsString(user);

        //when
        when(userService.findById(1)).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals(STATUS_OK, status);
        assertEquals(jsonUser, contentAsString);
    }

    @Test
    public void shouldReturnNotFoundStatusForUnExistingUser() throws Exception {
        //when
        when(userService.findById(5)).thenThrow(UserNotFoundException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/5")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_NOT_FOUND, status);
    }

    @Test
    public void shouldCreateUserAndReturnStatusCreated() throws Exception {
        //given
        User user = users.get(0);
        int statusCreated = 201;
        String expectedUser = objectMapper.writeValueAsString(user);

        //when
        when(userService.createUser(any(User.class))).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUser);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(expectedUser, contentAsString);
        assertEquals(statusCreated, status);
    }

    @Test
    public void shouldDeleteUserAndReturnStatusOk() throws Exception {
        //when
        when(userService.findById(1L)).thenReturn(any(User.class));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_OK, status);
    }

    @Test
    public void shouldReturnStatus404WhenDeletingUserDoesNotExist() throws Exception {
        //when
        when(userService.deleteUser(5L)).thenThrow(UserNotFoundException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/5")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_NOT_FOUND, status);
    }

    @Test
    public void shouldUpdateUserAndReturnStatus200() throws Exception {
        //given
        User user = users.get(0);
        User updatedUser = new User("password", "username", "mail@gmail.com");
        String expectedUser = objectMapper.writeValueAsString(updatedUser);

        //when
        when(userService.updateUser(user, 1)).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedUser);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //then
        assertEquals(STATUS_OK, status);
    }

    private List<User> generateListOfPeople() {
        return Arrays.asList(
                new User("maciek10", "macole7", "m.olejnik@gmail.com"),
                new User("maciek11", "macole8", "m.olejnik@gmail.com")
        );
    }
}