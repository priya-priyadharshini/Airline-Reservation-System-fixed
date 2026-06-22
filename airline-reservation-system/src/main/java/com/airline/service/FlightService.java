package com.airline.service;

import com.airline.entity.Flight;
import com.airline.entity.FlightStatus;
import com.airline.entity.Seat;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flights;
    private final SeatRepository seats;

    public FlightService(FlightRepository flights, SeatRepository seats) {
        this.flights = flights;
        this.seats = seats;
    }

    public List<Flight> available() {
        return flights.findByStatusNot(FlightStatus.CANCELLED);
    }

    public List<Flight> search(String source, String destination, LocalDate date) {
        source = source == null ? "" : source.trim();
        destination = destination == null ? "" : destination.trim();
        if (date == null) {
            return flights.findBySourceIgnoreCaseContainingAndDestinationIgnoreCaseContainingAndStatusNot(source, destination, FlightStatus.CANCELLED);
        }
        return flights.findBySourceIgnoreCaseContainingAndDestinationIgnoreCaseContainingAndDepartureDateAndStatusNot(source, destination, date, FlightStatus.CANCELLED);
    }

    public Flight get(Long id) {
        return flights.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
    }

    public Flight save(Flight flight) {
        if (flight.getAvailableSeats() == 0) {
            flight.setAvailableSeats(flight.getTotalSeats());
        }
        Flight saved = flights.save(flight);
        ensureSeats(saved);
        return saved;
    }

    public void delete(Long id) {
        flights.deleteById(id);
    }

    public long count() {
        return flights.count();
    }

    public List<Seat> seats(Long flightId) {
        return seats.findByFlightIdOrderBySeatNumber(flightId);
    }

    private void ensureSeats(Flight flight) {
        if (!seats(flight.getId()).isEmpty()) {
            return;
        }
        for (int i = 1; i <= flight.getTotalSeats(); i++) {
            Seat seat = new Seat();
            String type = (i % 6 == 1 || i % 6 == 0) ? "Window" : (i % 6 == 2 || i % 6 == 5) ? "Middle" : "Aisle";
            seat.setSeatNumber("S" + i);
            seat.setSeatType(type);
            seat.setFlight(flight);
            seats.save(seat);
        }
    }
}
