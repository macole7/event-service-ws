package com.service.eventservice.service;

import com.service.eventservice.model.Organizer;

import java.util.List;

public interface OrganizerService {

    Organizer findById(long id);

    List<Organizer> findByName(String name);

    Organizer createOrganizer(Organizer organizer);

    List<Organizer> findAll();

    Organizer updateUserById(Organizer organizer, long id);

    void deleteById(long id);
}
