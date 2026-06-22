package com.airline.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity
public class Ticket{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(unique=true) private String ticketNumber; private LocalDateTime issuedAt=LocalDateTime.now();
 @OneToOne private Booking booking;
 public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getTicketNumber(){return ticketNumber;} public void setTicketNumber(String ticketNumber){this.ticketNumber=ticketNumber;} public LocalDateTime getIssuedAt(){return issuedAt;} public void setIssuedAt(LocalDateTime issuedAt){this.issuedAt=issuedAt;} public Booking getBooking(){return booking;} public void setBooking(Booking booking){this.booking=booking;}
}
