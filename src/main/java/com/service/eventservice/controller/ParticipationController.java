package com.service.eventservice.controller;

import com.service.eventservice.model.Event;
import com.service.eventservice.model.User;
import com.service.eventservice.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipationController {

    private ParticipationService participationService;

    @Autowired
    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping("events/{eventId}/users")
    public Set<User> getAllUsersForEvent(@PathVariable int eventId) {
        return participationService.findByEventId(eventId);
    }

    @GetMapping("users/{userId}/events")
    public List<Event> getAllEventsForUser(@PathVariable int userId) {
        return participationService.findByUserId(userId);
    }

    @PostMapping("events/{eventId}/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Event insaretUserToEvent(@PathVariable int eventId, @PathVariable int userId) {
        return participationService.addUserToEvent(eventId, userId);
    }

    @DeleteMapping("events/{eventId}/users/{userId}")
    public Event deleteUserFromEvent(@PathVariable int eventId, @PathVariable int userId) {
        return participationService.deleteUserByUserIdFromEvent(eventId, userId);
    }

}
