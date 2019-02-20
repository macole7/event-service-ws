package com.service.eventservice.service;

import com.service.eventservice.model.Event;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> findAllEvents();

    List<Event> findByName(String name);

    Event findById(long id);

    List<Event> findByAddress(String address);

    List<Event> findByDateRange(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate since,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate until);

    Event findByOrganizerId(long id);

    List<Event> findByDate(LocalDate date);

    Event createEvent(Event event);

    Event updateEvent(Event event, long id);

    Event deleteEvent(long id);

    List<Event> findByNameAndAddressAndDate(String name, String address, LocalDate date);

}
