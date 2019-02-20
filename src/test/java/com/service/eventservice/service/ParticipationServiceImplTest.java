package com.service.eventservice.service;

import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.exception.ParticipationNotFoundException;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;
import com.service.eventservice.repository.EventRepository;
import com.service.eventservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ParticipationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private ParticipationServiceImpl participationService;

    private Event firstMockedEvent;
    private Event secondMockedEvent;
    private User firstMockedUser;
    private User secondMockedUser;

    @Before
    public void setUp() {
        firstMockedEvent = mock(Event.class);
        secondMockedEvent = mock(Event.class);
        firstMockedUser = mock(User.class);
        secondMockedUser = mock(User.class);
    }

    @Test
    public void shouldReturnEventsByUserId() {
        //given
        when(firstMockedEvent.getAddress()).thenReturn("London");
        when(secondMockedEvent.getAddress()).thenReturn("Wroclaw");
        List<Event> events = Arrays.asList(firstMockedEvent, secondMockedEvent);
        when(eventRepository.findByUsersId(1L)).thenReturn(events);
        //when
        List<Event> eventsByUserId = participationService.findByUserId(1L);
        //then
        assertEquals(events.size(), eventsByUserId.size());
    }

    @Test
    public void shouldReturnUsersByEventId() {
        //given
        when(firstMockedUser.getUsername()).thenReturn("jack");
        when(secondMockedUser.getUsername()).thenReturn("john");
        Set<User> users = new HashSet<>();
        users.add(firstMockedUser);
        users.add(secondMockedUser);
        when(firstMockedEvent.getUsers()).thenReturn(users);
        when(eventRepository.findById(1L)).thenReturn(ofNullable(firstMockedEvent));
        //when
        Set<User> usersByEventId = participationService.findByEventId(1L);
        //then
        assertEquals(users, usersByEventId);
    }

    @Test(expected = ParticipationNotFoundException.class)
    public void shouldThrowParticipationNotFoundExceptionWhenParticipationDoesNotExist() {
        when(eventRepository.findById(1L)).thenThrow(ParticipationNotFoundException.class);
        participationService.findByEventId(1L);
    }

    @Test
    public void shouldReturnCreatedEvent() {
        //given
        when(firstMockedEvent.getName()).thenReturn("party");
        when(firstMockedUser.getUsername()).thenReturn("jack");
        when(eventRepository.findById(1L)).thenReturn(ofNullable(firstMockedEvent));
        when(userRepository.findById(1L)).thenReturn(ofNullable(firstMockedUser));
        //when
        Event event = participationService.addUserToEvent(1L, 1L);
        assertEquals(firstMockedEvent, event);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistDuringCreatingParticipation() {
        when(userRepository.findById(1L)).thenThrow(UserNotFoundException.class);
        when(eventRepository.findById(1L)).thenReturn(ofNullable(firstMockedEvent));
        participationService.addUserToEvent(1L, 1L);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowEventNotFoundExceptionWhenEventDoesNotExistDuringCreatingParticipation() {
        when(userRepository.findById(1L)).thenReturn(ofNullable(firstMockedUser));
        when(eventRepository.findById(1L)).thenThrow(EventNotFoundException.class);
        participationService.addUserToEvent(1L, 1L);
    }

    @Test
    public void shouldReturnDeletedUserByUserId() {
        //given
        when(firstMockedEvent.getName()).thenReturn("party");
        when(firstMockedUser.getUsername()).thenReturn("jack");
        when(userRepository.findById(1L)).thenReturn(ofNullable(firstMockedUser));
        when(eventRepository.findById(1L)).thenReturn(ofNullable(firstMockedEvent));
        //when
        Event deletedEvent = participationService.deleteUserByUserIdFromEvent(1L, 1L);
        //then
        assertEquals(firstMockedEvent, deletedEvent);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistDuringDeletingParticipation() {
        when(userRepository.findById(1L)).thenThrow(UserNotFoundException.class);
        when(eventRepository.findById(1L)).thenReturn(ofNullable(firstMockedEvent));
        participationService.deleteUserByUserIdFromEvent(1L, 1L);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowEventNotFoundExceptionWhenEventDoesNotExistDuringDeletingParticipation() {
        when(userRepository.findById(1L)).thenReturn(ofNullable(firstMockedUser));
        when(eventRepository.findById(1L)).thenThrow(EventNotFoundException.class);
        participationService.deleteUserByUserIdFromEvent(1L, 1L);
    }

}