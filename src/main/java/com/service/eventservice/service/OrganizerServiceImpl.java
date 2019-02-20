package com.service.eventservice.service;

import com.service.eventservice.exception.OrganizerNotFoundException;
import com.service.eventservice.model.Organizer;
import com.service.eventservice.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerServiceImpl implements OrganizerService {

    private OrganizerRepository organizerRepository;

    @Autowired
    public OrganizerServiceImpl(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @Override
    public List<Organizer> findAll() {
        return organizerRepository.findAll();
    }

    @Override
    public Organizer findById(long id) {
        return organizerRepository
                .findById(id).orElseThrow(() -> new OrganizerNotFoundException("Organizer not found " + id));
    }

    @Override
    public List<Organizer> findByName(String name) {
        List<Organizer> organizersByName = organizerRepository.findByName(name);
        if (organizersByName == null || organizersByName.isEmpty()) {
            throw new OrganizerNotFoundException("Organizer not found " + name);
        }
        return organizersByName;
    }

    @Override
    public Organizer createOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    @Override
    public Organizer updateUserById(Organizer organizer, long id) {
        Organizer organizerById = organizerRepository
                .findById(id).orElseThrow(() -> new OrganizerNotFoundException("Organizer not found " + id));
        organizerById.setEmail(organizer.getEmail());
        organizerById.setName(organizer.getName());
        return organizerRepository.save(organizerById);
    }

    @Override
    public void deleteById(long id) {
        organizerRepository.deleteById(id);
    }
}
