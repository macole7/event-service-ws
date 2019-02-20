package com.service.eventservice.service;

import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.User;
import com.service.eventservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private List<User> users;

    @Before
    public void initListOfUsers() {
        users = generateListOfPeople();
    }

    @Test
    public void shouldReturnEmptyList() {
        //given
        List<User> emptyList = new ArrayList<>();
        //when
        when(userRepository.findAll()).thenReturn(emptyList);
        List<User> allUsers = userService.findAllUsers();
        //then
        assertEquals(emptyList, allUsers);
    }

    @Test
    public void shouldReturnAllUsers() {
        //given
        //when
        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.findAllUsers();
        //then
        assertArrayEquals(new List[]{users}, new List[]{allUsers});
        assertEquals(3, allUsers.size());
    }

    @Test
    public void shouldReturnProperUsernamePasswordAndEmail() {
        //given
        String expectedUsername = "macole8";
        String expectedPassword = "maciek11";
        String expectedEmail = "m.olejnik@gmail.com";
        //when
        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.findAllUsers();
        //then
        assertEquals(expectedUsername, allUsers.get(1).getUsername());
        assertEquals(expectedPassword, allUsers.get(1).getPassword());
        assertEquals(expectedEmail, allUsers.get(1).getEmail());
    }

    @Test
    public void shouldCheckIfUserIsNotNull() {
        //given
        User user = users.get(0);
        //when
        when(userRepository.findById(0L)).thenReturn(ofNullable(user));
        User userById = userService.findById(0);
        //then
        assertNotNull(userById);
    }

    @Test
    public void shouldReturnProperUsernamePasswordAndEmailForGivenUser() {
        //given
        User user = users.get(0);
        //when
        when(userRepository.findById(0L)).thenReturn(ofNullable(user));
        User userById = userService.findById(0);
        assertEquals(user, userById);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        //when
        userService.findById(5);
        userService.deleteUser(5);
        userService.updateUser(users.get(0), 5);
        //then shouldThrowException
    }

    @Test
    public void shouldReturnProperUserUsernameAndEmailForCreatedUser() {
        //given
        User mockedUser = mock(User.class);
        when(mockedUser.getEmail()).thenReturn("m.olejnik@gmail.com");
        when(mockedUser.getUsername()).thenReturn("macole8");
        //when
        when(userRepository.save(mockedUser)).thenReturn(mockedUser);
        User createdUser = userService.createUser(mockedUser);
        //then
        assertEquals(mockedUser.getEmail(), createdUser.getEmail());
        assertEquals(mockedUser.getUsername(), createdUser.getUsername());
        assertEquals(mockedUser, createdUser);
    }

    @Test
    public void shouldDeleteProperUserByUserId() {
        //given
        User user = users.get(0);
        //when
        when(userRepository.findById(1L)).thenReturn(ofNullable(user));
        User deletedUser = userService.deleteUser(1);
        //then
        assertEquals(user, deletedUser);
    }

    @Test
    public void shouldReturnUserByUsername() {
        //given
        List<User> users = Collections.singletonList(this.users.get(0));
        //when
        when(userRepository.findByUsername("macole7")).thenReturn(users);
        List<User> userByUsername = userService.findByUsername("macole7");
        //then
        assertEquals(users.size(), userByUsername.size());
        assertEquals(users.get(0).getUsername(), userByUsername.get(0).getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowNotFoundUserExceptionWhenFoundListIsEmpty() {
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        userService.findByUsername(anyString());
    }


    private List<User> generateListOfPeople() {
        return Arrays.asList(
                new User("maciek10", "macole7", "m.olejnik@gmail.com"),
                new User("maciek11", "macole8", "m.olejnik@gmail.com"),
                new User("maciek12", "macole9", "m.olejnik@gmail.com")
        );
    }
}