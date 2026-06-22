package com.airline.entity;
import jakarta.persistence.*;
@Entity
public class Passenger{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String name; private int age; private String gender; private String seatNumber;
 @ManyToOne private Booking booking;
 public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getName(){return name;} public void setName(String name){this.name=name;} public int getAge(){return age;} public void setAge(int age){this.age=age;} public String getGender(){return gender;} public void setGender(String gender){this.gender=gender;} public String getSeatNumber(){return seatNumber;} public void setSeatNumber(String seatNumber){this.seatNumber=seatNumber;} public Booking getBooking(){return booking;} public void setBooking(Booking booking){this.booking=booking;}
}
