package com.service.eventservice.service;

import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImp implements EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventServiceImp(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findByName(String name) {
        List<Event> eventsByName = eventRepository.findByNameIgnoreCase(name);
        if (eventsByName == null || eventsByName.isEmpty()) {
            throw new EventNotFoundException("Event not found " + name);
        }
        return eventsByName;
    }

    @Override
    public Event findById(long id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found " + id));
    }

    @Override
    public List<Event> findByAddress(String address) {
        List<Event> eventByAddress = eventRepository.findByAddressIgnoreCase(address);
        if (eventByAddress == null || eventByAddress.isEmpty()) {
            throw new EventNotFoundException("Event not found " + address);
        }
        return eventByAddress;
    }

    @Override
    public List<Event> findByDateRange(LocalDate since, LocalDate until) {
        return eventRepository.findByDateBetween(since, until);
    }

    @Override
    public Event findByOrganizerId(long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    @Override
    public List<Event> findByDate(LocalDate date) {
        List<Event> eventsByDate = eventRepository.findByDate(date);
        if (eventsByDate == null || eventsByDate.isEmpty()) {
            throw new EventNotFoundException("Event not found by given date " + date);
        }
        return eventsByDate;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event, long id) {
        Event eventById = eventRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found for user " + id));
        eventById.setName(event.getName());
        eventById.setAddress(event.getAddress());
        eventById.setDate(event.getDate());
        eventById.setOrganizer(event.getOrganizer());
        return eventRepository.save(eventById);
    }
    
    @Override
    public Event deleteEvent(long id) {
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found for user " + id));
        eventRepository.delete(event);
        return event;
    }

    @Override
    public List<Event> findByNameAndAddressAndDate(String name, String address, LocalDate date) {
        List<Event> events = eventRepository.findByNameAndAddressAndDate(name, address, date);
        if (events == null || events.isEmpty()) {
           throw new EventNotFoundException("Event not found");
        }
        return events;
    }
}
