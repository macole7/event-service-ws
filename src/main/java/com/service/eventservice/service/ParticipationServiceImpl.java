package com.service.eventservice.service;

import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;
import com.service.eventservice.repository.EventRepository;
import com.service.eventservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private UserRepository userRepository;
    private EventRepository eventRepository;

    @Autowired
    public ParticipationServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findByUserId(long userId) {
        return eventRepository.findByUsersId(userId);
    }

    @Override
    public Set<User> findByEventId(long eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("There are not any events for provided id " + eventId));
        Set<User> users = event.getUsers();
        if (users.isEmpty()) {
            throw new UserNotFoundException("There are any users for the event_id " + eventId);
        }
        return users;

    }

    @Override
    public Event addUserToEvent(long eventId, long userId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Input event does not exist " + eventId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Input user does not exist " + userId));
        event.getUsers().add(user);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event deleteUserByUserIdFromEvent(long eventId, long userId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Input event does not exist " + eventId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Input user does not exist " + userId));
        event.getUsers().remove(user);
        eventRepository.save(event);
        return event;
    }
}




