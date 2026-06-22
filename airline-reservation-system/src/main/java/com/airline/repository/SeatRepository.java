package com.airline.repository;
import com.airline.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface SeatRepository extends JpaRepository<Seat,Long>{ List<Seat> findByFlightIdOrderBySeatNumber(Long flightId); List<Seat> findByIdIn(List<Long> ids); }
