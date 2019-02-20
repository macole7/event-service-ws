package com.service.eventservice.repository;

import com.service.eventservice.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    List<Organizer> findByName(String name);
}
