package com.service.eventservice.service;

import com.service.eventservice.exception.EventNotFoundException;
import com.service.eventservice.model.Event;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImpTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImp eventService;

    private List<Event> events = generateEventsList();

    @Test
    public void shouldReturnAllEvents() {
        //given list of events
        //when
        when(eventRepository.findAll()).thenReturn(events);
        List<Event> allEvents = eventService.findAllEvents();
        //then
        assertEquals(events, allEvents);
    }

    @Test
    public void shouldReturnEmptyListOfEventsWhenThereAreNotAnyEventsInDB() {
        //given
        List<Event> events = new ArrayList<>();
        //when
        when(eventRepository.findAll()).thenReturn(new ArrayList<>());
        List<Event> allEvents = eventService.findAllEvents();
        //then
        assertEquals(events, allEvents);
        assertEquals(0, allEvents.size());
    }

    @Test
    public void shouldReturnProperEventByEventName() {
        //given
        String eventName = "Party1";
        //when
        when(eventRepository.findByNameIgnoreCase(eventName)).thenReturn(events);
        List<Event> eventsByName = eventService.findByName(eventName);
        //then
        assertEquals(events, eventsByName);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenGivenNameOfEventDoesNotExist() {
        //when
        when(eventRepository.findByNameIgnoreCase(anyString())).thenReturn(null);
        eventService.findByName("AnyName");
    }

    @Test
    public void shouldReturnProperEventByEventId() {
        //given
        Event event = events.get(0);
        //when
        when(eventRepository.findById(1L)).thenReturn(ofNullable(event));
        Event eventById = eventService.findById(1);
        //then
        assertEquals(event, eventById);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenGivenEventByIdDoesNotExist() {
        when(eventRepository.findById(1L)).thenThrow(EventNotFoundException.class);
        eventService.findById(1);
    }

    @Test
    public void shouldReturnEventByAddress() {
        //given
        String address = "Wroclaw";
        //when
        when(eventRepository.findByAddressIgnoreCase(address)).thenReturn(events);
        List<Event> eventsByAddress = eventService.findByAddress(address);
        //then
        assertEquals(events, eventsByAddress);
    }

    @Test(expected = EventNotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenGivenEventDoesNotExist() {
        //when
        when(eventRepository.findByAddressIgnoreCase(anyString())).thenThrow(EventNotFoundException.class);
        eventService.findByAddress("AnyAddress");
    }

    @Test
    public void shouldReturnEventForGivenDateOfEvent() {
        //given
        LocalDate date = LocalDate.of(2018, 11, 29);
        Event event = events.get(0);
        //when
        when(eventRepository.findByDate(date)).thenReturn(events);
        List<Event> eventsByDate = eventService.findByDate(date);
        //then
        assertEquals(events, eventsByDate);
    }

    @Test
    public void shouldReturnEventThatIsBetweenTwoDates() {
        //given
        LocalDate startDate = LocalDate.of(2016, 1, 1);
        LocalDate endDate = LocalDate.of(2019, 1, 1);
        //when
        when(eventRepository.findByDateBetween(startDate, endDate)).thenReturn(events);
        List<Event> eventsByDateRange = eventService.findByDateRange(startDate, endDate);
        //then
        assertEquals(events.size(), eventsByDateRange.size());
    }

    @Test
    public void shouldReturnEventsForGivenUser() {
        //given
        Event event = events.get(0);
        //when
        when(eventRepository.findByOrganizerId(1L)).thenReturn(event);
        Event eventByOrganizerId = eventService.findByOrganizerId(1);
        //then
        assertEquals(event, eventByOrganizerId);
    }

    @Test
    public void shouldReturnEventsForGivenNameAndAddressAndDate() {
        //given
        String name = "Party1";
        String address = "Wroclaw";
        LocalDate date = LocalDate.of(2018, 11, 29);
        List<Event> eventsList = Collections.singletonList(events.get(0));
        //when
        when(eventRepository.findByNameAndAddressAndDate(name, address, date)).thenReturn(eventsList);
        List<Event> eventByNameAndAddressAndDate = eventService.findByNameAndAddressAndDate(name, address, date);
        //then
        assertEquals(1, eventByNameAndAddressAndDate.size());
        assertEquals(name, eventByNameAndAddressAndDate.get(0).getName());
        assertEquals(address, eventByNameAndAddressAndDate.get(0).getAddress());
        assertEquals(date, eventByNameAndAddressAndDate.get(0).getDate());
    }

    @Test
    public void shouldReturnEventsForGivenNameWhenAddressAndDateIsEmpty() {
        //given
        String name = "Party1";
        String address = "";
        LocalDate date = LocalDate.now();
        List<Event> eventsList = Collections.singletonList(events.get(0));
        //when
        when(eventRepository.findByNameAndAddressAndDate(name, address, date)).thenReturn(eventsList);
        List<Event> eventByNameAndAddressAndDate = eventService.findByNameAndAddressAndDate(name, address, date);
        //then
        assertEquals(1, eventByNameAndAddressAndDate.size());
        assertEquals(name, eventByNameAndAddressAndDate.get(0).getName());
    }

    private List<Event> generateEventsList() {
        Organizer organizer = new Organizer("John", "m2@gmail.com");
        return Arrays.asList(
                new Event("Party1", LocalDate.of(2018, 11, 29), "Wroclaw", organizer),
                new Event("Party2", LocalDate.of(2018, 12, 27), "Wroclaw", organizer),
                new Event("Party3", LocalDate.of(2019, 3, 25), "Wroclaw", organizer));
    }
}