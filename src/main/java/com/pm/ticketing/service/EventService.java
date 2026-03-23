package com.pm.ticketing.service;

import com.pm.ticketing.dto.CreateEventRequest;
import com.pm.ticketing.dto.EventResponse;
import com.pm.ticketing.dto.SeatResponse;
import com.pm.ticketing.exception.ResourceNotFoundException;
import com.pm.ticketing.model.Event;
import com.pm.ticketing.model.Seat;
import com.pm.ticketing.model.SeatStatus;
import com.pm.ticketing.repository.EventRepository;
import com.pm.ticketing.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    public EventService(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    public EventResponse createEvent(CreateEventRequest request) {
        Event event = new Event(
                request.name(),
                request.venue(),
                LocalDateTime.parse(request.dateTime())
        );

        for(int i =1; i<= request.numberOfSeats(); i++){
            String seatNumber = "A" +  i;
            Seat seat = new Seat(seatNumber, request.seatPrice());
            event.addSeat(seat);
        }

        Event saved = eventRepository.save(event);
        return toResponse(saved);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Event findEventOrThrow(Long id){
        return eventRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Event not found"));
    }

    public EventResponse getEventById(Long id){
        Event event = findEventOrThrow(id);
        return toResponse(event);
    }

    public List<SeatResponse> getSeatsForEvent(Long eventId){
        findEventOrThrow(eventId);
        return seatRepository.findByEventId(eventId).stream()
                .map(this::toSeatResponse)
                .toList();
    }

    private EventResponse toResponse(Event event) {
        long available = seatRepository.countByEventIdAndStatus(
                event.getId(), SeatStatus.AVAILABLE
        );
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getVenue(),
                event.getDateTime(),
                event.getSeats().size(),
                (int) available
        );
    }

    private SeatResponse toSeatResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getPrice(),
                seat.getStatus().name()
        );
    }
}
