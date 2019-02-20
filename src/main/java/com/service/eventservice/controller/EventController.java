package com.service.eventservice.controller;

import com.service.eventservice.model.Event;
import com.service.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("events")
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") int id) {
        return eventService.findById(id);
    }

    @GetMapping("/address")
    public List<Event> getEventByAddress(@RequestParam(value = "address") String address) {
        return eventService.findByAddress(address);
    }

    @GetMapping("/name")
    public List<Event> getEventByName(@RequestParam(value = "name") String name) {
        return eventService.findByName(name);
    }

    @GetMapping("/date")
    public List<Event> getEventByDate(@RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return eventService.findByDate(date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/startDate/endDate")
    public List<Event> getEventsBetweenStartAndEndDate(@RequestParam(value = "startDate")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                       @RequestParam(value = "endDate")
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return eventService.findByDateRange(startDate, endDate);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@RequestBody Event event, @PathVariable int id) {
        return eventService.updateEvent(event, id);
    }

    @DeleteMapping("/{id}")
    public Event deleteEvent(@PathVariable int id) {
        return eventService.deleteEvent(id);
    }

    @GetMapping("/name/address/date")
    public List<Event> getEventByNameAndAddressAndDate(@RequestParam(value = "name") String name,
                                                       @RequestParam(value = "address") String address,
                                                       @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return eventService.findByNameAndAddressAndDate(name, address, date);
    }
}
