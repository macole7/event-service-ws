package com.service.eventservice.controller;

import com.service.eventservice.model.Organizer;
import com.service.eventservice.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("organizers")
public class OrganizerController {

    private OrganizerService organizerService;

    @Autowired
    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping
    public List<Organizer> findAllOrganizers() {
        return organizerService.findAll();
    }

    @GetMapping("/{id}")
    public Organizer getOrganizerById(@PathVariable int id) {
        return organizerService.findById(id);
    }

    @GetMapping("/name")
    public List<Organizer> getOrganizersByName(@RequestParam(value = "name") String name) {
        return organizerService.findByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organizer createOrganizer(@RequestBody Organizer organizer) {
        return organizerService.createOrganizer(organizer);
    }

    @PutMapping("/{id}")
    public Organizer updateOrganizer(@RequestBody Organizer organizer, @PathVariable int id) {
        return organizerService.updateUserById(organizer, id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganizerById(@PathVariable int id) {
        organizerService.deleteById(id);
    }
}
