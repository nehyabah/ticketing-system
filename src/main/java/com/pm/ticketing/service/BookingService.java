package com.pm.ticketing.service;

import com.pm.ticketing.dto.BookingRequest;
import com.pm.ticketing.dto.BookingResponse;
import com.pm.ticketing.exception.ResourceNotFoundException;
import com.pm.ticketing.model.Booking;
import com.pm.ticketing.model.Seat;
import com.pm.ticketing.model.User;
import com.pm.ticketing.repository.BookingRepository;
import com.pm.ticketing.repository.EventRepository;
import com.pm.ticketing.repository.SeatRepository;
import com.pm.ticketing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          SeatRepository seatRepository,
                          UserService userService,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public BookingResponse bookSeat(BookingRequest request) {
        //Idempotency Check - has this exact booking been processed before?
        Optional<Booking> existing = bookingRepository
                .findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        //Find User
        User user = userService.findUserOrThrow(request.userId());

        // FIND AND LOCK SEAT - pessimistic lock prevents double booking
        Seat seat = seatRepository.findByIdWithLock(request.seatId())
                .orElseThrow(()-> new ResourceNotFoundException("Seat Not Found with id: " + request.seatId()));

        //CHECK SEAT IS AVAILABLE



        return toResponse(saved);
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSeat().getEvent().getName(),
                booking.getSeat().getSeatNumber(),
                booking.getSeat().getPrice(),
                booking.getUser().getName(),
                booking.getBookingTime(),
                booking.getStatus().name()
        );
    }


}
