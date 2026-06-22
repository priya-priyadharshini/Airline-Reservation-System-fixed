package com.airline.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.*; import java.util.*;
@Entity
public class Booking{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true) private String pnr; private LocalDateTime bookingDate=LocalDateTime.now();
 @Enumerated(EnumType.STRING) private BookingStatus status=BookingStatus.CONFIRMED;
 private int seatCount; private BigDecimal totalAmount;
 @ManyToOne private User user; @ManyToOne private Flight flight;
 @OneToMany(mappedBy="booking",cascade=CascadeType.ALL,orphanRemoval=true) private List<Passenger> passengers=new ArrayList<>();
 @OneToOne(mappedBy="booking",cascade=CascadeType.ALL) private Payment payment; @OneToOne(mappedBy="booking",cascade=CascadeType.ALL) private Ticket ticket;
 public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getPnr(){return pnr;} public void setPnr(String pnr){this.pnr=pnr;} public LocalDateTime getBookingDate(){return bookingDate;} public void setBookingDate(LocalDateTime bookingDate){this.bookingDate=bookingDate;} public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus status){this.status=status;} public int getSeatCount(){return seatCount;} public void setSeatCount(int seatCount){this.seatCount=seatCount;} public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal totalAmount){this.totalAmount=totalAmount;} public User getUser(){return user;} public void setUser(User user){this.user=user;} public Flight getFlight(){return flight;} public void setFlight(Flight flight){this.flight=flight;} public List<Passenger> getPassengers(){return passengers;} public void setPassengers(List<Passenger> passengers){this.passengers=passengers;} public Payment getPayment(){return payment;} public void setPayment(Payment payment){this.payment=payment;} public Ticket getTicket(){return ticket;} public void setTicket(Ticket ticket){this.ticket=ticket;}
}
