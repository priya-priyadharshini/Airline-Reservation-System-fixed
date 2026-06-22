package com.airline.repository;
import com.airline.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface BookingRepository extends JpaRepository<Booking,Long>{ List<Booking> findByUserIdOrderByBookingDateDesc(Long userId); Optional<Booking> findByPnr(String pnr); }
