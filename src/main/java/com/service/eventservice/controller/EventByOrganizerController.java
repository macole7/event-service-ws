package com.service.eventservice.controller;

import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/organizer")
public class EventByOrganizerController {

    private EventService eventService;

    @Autowired
    private EventByOrganizerController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}/events")
    public Event getAllEventsByOrganizerId(@PathVariable int id) {
        return Optional.of(eventService
                .findByOrganizerId(id)).orElseThrow(() -> new EventNotFoundException("Event not found for " + "organizer " + id));

    }
}
