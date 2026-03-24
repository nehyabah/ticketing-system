# Concert Ticket Booking System

A Spring Boot REST API for booking concert tickets, built to demonstrate **transactions**, **concurrency control**, and **idempotency** — the core patterns behind every production booking, payment, and inventory system.

## What This Project Demonstrates

### Transactions (`@Transactional`)
When a user books a seat, four things happen: the seat is marked as booked, the booking record is created, and the user's wallet is charged. If any step fails, **all changes roll back** — no ghost bookings, no lost money.

### Concurrency Control (Pessimistic Locking)
When two users try to book the same seat at the same millisecond, the database locks the seat row so only one request proceeds at a time. The first user gets the seat; the second gets a clean "seat already booked" error — not a double booking.

### Idempotency
If a user clicks "Book" three times because their internet is slow, the system recognises duplicate requests using an **idempotency key** and returns the same result without creating multiple bookings or charging multiple times.

### Optimistic Locking (`@Version`)
Seats have a version field managed by Hibernate. If two transactions somehow read the same seat simultaneously, only the first save succeeds — the second detects the version mismatch and fails safely.

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (in-memory)
- **Maven**

## API Endpoints

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a user with wallet balance |

### Events
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/events` | Create an event with auto-generated seats |
| GET | `/api/events` | List all events with availability count |
| GET | `/api/events/{id}` | Get event details |
| GET | `/api/events/{eventId}/seats` | List all seats with status |

### Bookings
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings` | Book a seat (with idempotency key) |
| GET | `/api/bookings/user/{userId}` | Get user's bookings |
| DELETE | `/api/bookings/{id}` | Cancel booking (refunds wallet, releases seat) |

## How to Run

```bash
# Clone the repo
git clone https://github.com/yourusername/booking-system.git
cd booking-system

# Run with Maven
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

## Example Usage

### 1. Create a user
```bash
POST /api/users
{
    "name": "John",
    "email": "john@test.com",
    "initialBalance": 500.00
}
```

### 2. Create an event with seats
```bash
POST /api/events
{
    "name": "Harry Styles Concert",
    "venue": "3Arena Dublin",
    "dateTime": "2026-06-15T20:00:00",
    "numberOfSeats": 5,
    "seatPrice": 89.99
}
```

### 3. Book a seat
```bash
POST /api/bookings
{
    "userId": 1,
    "seatId": 1,
    "idempotencyKey": "john-seat1-20260315"
}
```

### 4. Test idempotency — send the same request again
Same request, same response. No duplicate booking.

### 5. Test concurrency — try booking the same seat as another user
Returns `409 Conflict` — seat is already booked.

### 6. Cancel and get refund
```bash
DELETE /api/bookings/1
```
Wallet balance restored. Seat available again.

## Project Structure

```
src/main/java/com/john/bookingsystem/
├── model/
│   ├── Event.java            # Event entity with cascading seats
│   ├── Seat.java             # Seat with @Version for optimistic locking
│   ├── User.java             # User with wallet balance
│   ├── Booking.java          # Booking with idempotency key
│   ├── SeatStatus.java       # AVAILABLE, BOOKED, HELD
│   └── BookingStatus.java    # CONFIRMED, CANCELLED
├── dto/
│   ├── CreateEventRequest.java
│   ├── CreateUserRequest.java
│   ├── BookingRequest.java   # Includes idempotency key
│   ├── EventResponse.java
│   ├── SeatResponse.java
│   ├── BookingResponse.java
│   └── UserResponse.java
├── repository/
│   ├── EventRepository.java
│   ├── SeatRepository.java   # Includes pessimistic lock query
│   ├── BookingRepository.java
│   └── UserRepository.java
├── service/
│   ├── EventService.java
│   ├── BookingService.java   # Core logic: transactions + locking + idempotency
│   └── UserService.java
├── controller/
│   ├── EventController.java
│   ├── BookingController.java
│   └── UserController.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── SeatNotAvailableException.java
    ├── InsufficientBalanceException.java
    └── GlobalExceptionHandler.java
```

## Key Design Decisions

**Pessimistic locking over optimistic for seat booking** — In a high-demand scenario (concert tickets), conflicts are expected, not rare. Pessimistic locking prevents wasted work by queuing requests rather than letting them all proceed and fail.

**Idempotency key as a unique database constraint** — Even if the application-level check fails, the database rejects duplicates. Defence in depth.

**Wallet balance on the User entity** — Simplified for learning. In production, payments would go through a separate payment service (Stripe, etc.) with its own transaction guarantees.

**CascadeType.ALL on Event → Seats** — Creating an event with seats is a single atomic operation. Deleting an event removes all its seats automatically.



- `@Transactional` ensures all-or-nothing database operations
- Pessimistic locking (`@Lock(PESSIMISTIC_WRITE)`) prevents concurrent access to the same row
- Optimistic locking (`@Version`) detects conflicting updates after the fact
- Idempotency keys prevent duplicate processing from retries or double-clicks
- `CascadeType.ALL` propagates persistence operations from parent to child entities
- Proper HTTP status codes: 409 for conflicts, 400 for bad requests, 404 for not found
