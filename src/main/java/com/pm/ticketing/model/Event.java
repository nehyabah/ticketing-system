package com.pm.ticketing.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String venue;
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    public Event() {
    }

    public Event(String name, String venue, LocalDateTime dateTime) {
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
