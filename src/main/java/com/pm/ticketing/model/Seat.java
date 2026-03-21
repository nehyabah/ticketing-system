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
}
