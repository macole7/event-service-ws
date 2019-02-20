package com.service.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private LocalDate date;
    @NotNull
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Participation",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @OneToMany
    private Set<Comment> comments = new HashSet<>();

    public Event(String name, LocalDate date, String address, Organizer organizer) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.organizer = organizer;
    }

    public Event() {
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public Long getId() {
        return id;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(name, event.name) &&
                Objects.equals(date, event.date) &&
                Objects.equals(address, event.address) &&
                Objects.equals(comments, event.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, address, comments);
    }
}
