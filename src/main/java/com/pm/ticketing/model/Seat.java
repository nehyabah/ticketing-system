package com.pm.ticketing.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private double price;

    @Enumerated(EnumType.STRING)
    private  SeatStatus status;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Seat() {
    }

    public Seat(String seatNumber, double price, SeatStatus status) {
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public Event getEvent() {
        return event;
    }

    public void setStatus(SeatStatus status) { this.status = status; }
    public void setEvent(Event event) { this.event = event; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setPrice(double price) { this.price = price; }
}
