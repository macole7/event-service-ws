package com.service.eventservice.repository;

import com.service.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByNameIgnoreCase(String name);

    List<Event> findByDate(LocalDate date);

    List<Event> findByAddressIgnoreCase(String address);

    List<Event> findByDateBetween(LocalDate start, LocalDate end);

    Event findByOrganizerId(Long id);

    List<Event> findByUsersId(long userId);

    List<Event> findByNameAndAddressAndDate(String name, String address, LocalDate date);

}
