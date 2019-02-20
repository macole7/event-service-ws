package com.service.eventservice.service;

import com.service.eventservice.exception.OrganizerNotFoundException;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.repository.OrganizerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrganizerServiceImplTest {

    @Mock
    private OrganizerRepository organizerRepository;
    @InjectMocks
    private OrganizerServiceImpl organizerService;

    @Test
    public void shouldReturnAllOrganizers() {
        //given
        List<Organizer> organizers = getOrganizers();
        when(organizerRepository.findAll()).thenReturn(organizers);
        //when
        List<Organizer> allOrganizers = organizerService.findAll();
        //then
        assertEquals(organizers, allOrganizers);
    }

    @Test
    public void shouldReturnOrganizerByOrganizerId() {
        //given
        Organizer organizer = getOrganizers().get(0);
        when(organizerRepository.findById(1L)).thenReturn(ofNullable(organizer));
        //when
        Organizer organizerById = organizerService.findById(1L);
        //then
        assertEquals(organizer, organizerById);
    }

    @Test(expected = OrganizerNotFoundException.class)
    public void shouldThrowOrganizerNotFoundExceptionWhenOrganizerDoesNotExist() {
        when(organizerRepository.findById(1L)).thenThrow(OrganizerNotFoundException.class);
        organizerService.findById(1L);
    }

    @Test
    public void shouldReturnOrganizersByName() {
        //given
        List<Organizer> organizers = Collections.singletonList(getOrganizers().get(0));
        when(organizerRepository.findByName("Franck")).thenReturn(organizers);
        //when
        List<Organizer> organizersByName = organizerService.findByName("Franck");
        assertEquals(organizers, organizersByName);
        assertEquals(1, organizersByName.size());
    }

    @Test(expected = OrganizerNotFoundException.class)
    public void shouldThrowOrganizerNotFoundExceptionWhenOrganizerByNameDoesNotExist() {
        when(organizerRepository.findByName("Jack")).thenThrow(OrganizerNotFoundException.class);
        organizerService.findByName("Jack");
    }

    @Test
    public void shouldReturnCreatedOrganizer() {
        //given
        Organizer organizer = getOrganizers().get(0);
        when(organizerRepository.save(organizer)).thenReturn(organizer);
        //when
        Organizer savedOrganizer = organizerService.createOrganizer(organizer);
        //then
        assertEquals(organizer, savedOrganizer);
    }

    @Test
    public void shouldReturnUpdatedUser() {
        //given
        Organizer organizer = getOrganizers().get(0);
        Organizer organizerToUpdate = new Organizer("Paul", "paul@gmail.com");
        when(organizerRepository.findById(1L)).thenReturn(ofNullable(organizer));
        when(organizerRepository.save(any(Organizer.class))).thenReturn(organizerToUpdate);
        //when
        Organizer updatedOrganizer = organizerService.updateUserById(organizerToUpdate, 1L);
        //then
        assertEquals(organizerToUpdate, updatedOrganizer);
    }

    @Test(expected = OrganizerNotFoundException.class)
    public void shouldThrowOrganizerNotFoundExceptionWhenOrganizerToUpdateDoesNotExist() {
        Organizer organizer = getOrganizers().get(0);
        when(organizerRepository.findById(1L)).thenThrow(OrganizerNotFoundException.class);
        organizerService.updateUserById(organizer, 1L);
    }

    private List<Organizer> getOrganizers() {
        return Arrays.asList(new Organizer("Franck", "franck@gmail.com"),
                new Organizer("Jack", "jack@gmail.com"));
    }
}