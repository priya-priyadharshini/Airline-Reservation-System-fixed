package com.airline.repository;
import com.airline.entity.Payment; import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment,Long>{}
