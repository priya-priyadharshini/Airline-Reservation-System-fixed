package com.airline.repository;

import com.airline.entity.Flight;
import com.airline.entity.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByStatusNot(FlightStatus status);
    List<Flight> findBySourceIgnoreCaseContainingAndDestinationIgnoreCaseContainingAndStatusNot(String source, String destination, FlightStatus status);
    List<Flight> findBySourceIgnoreCaseContainingAndDestinationIgnoreCaseContainingAndDepartureDateAndStatusNot(String source, String destination, LocalDate date, FlightStatus status);
}
