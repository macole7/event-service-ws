package com.service.eventservice.service;

import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;

import java.util.List;
import java.util.Set;

public interface ParticipationService {

    List<Event> findByUserId(long userId);

    Set<User> findByEventId(long eventId);

    Event addUserToEvent(long eventId, long userId);

    Event deleteUserByUserIdFromEvent(long eventId, long userId);

}
