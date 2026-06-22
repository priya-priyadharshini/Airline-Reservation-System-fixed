package com.airline.entity;
import jakarta.persistence.*;
@Entity
public class Seat{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String seatNumber; private String seatType; private boolean booked=false;
 @ManyToOne private Flight flight;
 public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getSeatNumber(){return seatNumber;} public void setSeatNumber(String seatNumber){this.seatNumber=seatNumber;} public String getSeatType(){return seatType;} public void setSeatType(String seatType){this.seatType=seatType;} public boolean isBooked(){return booked;} public void setBooked(boolean booked){this.booked=booked;} public Flight getFlight(){return flight;} public void setFlight(Flight flight){this.flight=flight;}
}
