package com.airline.service;
import com.airline.dto.PaymentDto; import com.airline.entity.*; import com.airline.repository.*; import com.airline.util.CodeGenerator; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; import java.util.*;
@Service
public class BookingService{
 private final BookingRepository bookings; private final FlightRepository flights; private final SeatRepository seats; private final UserRepository users;
 public BookingService(BookingRepository bookings,FlightRepository flights,SeatRepository seats,UserRepository users){this.bookings=bookings;this.flights=flights;this.seats=seats;this.users=users;}
 @Transactional public Booking create(Long userId,Long flightId,List<Long> seatIds,List<String> names,List<Integer> ages,List<String> genders,PaymentDto pay){
  User user=users.findById(userId).orElseThrow(); Flight flight=flights.findById(flightId).orElseThrow();
  if(seatIds==null||seatIds.isEmpty()) throw new IllegalArgumentException("Select at least one seat");
  List<Seat> selected=seats.findByIdIn(seatIds); if(selected.size()!=seatIds.size()) throw new IllegalArgumentException("Invalid seat selection");
  for(Seat s:selected){ if(s.isBooked()) throw new IllegalArgumentException("Seat "+s.getSeatNumber()+" is already booked"); }
  Booking b=new Booking(); b.setUser(user); b.setFlight(flight); b.setPnr(CodeGenerator.pnr()); b.setSeatCount(selected.size()); b.setTotalAmount(flight.getPrice().multiply(BigDecimal.valueOf(selected.size())));
  for(int i=0;i<selected.size();i++){ Seat s=selected.get(i); s.setBooked(true); seats.save(s); Passenger p=new Passenger(); p.setName(get(names,i,"Passenger "+(i+1))); p.setAge(getInt(ages,i,18)); p.setGender(get(genders,i,"Other")); p.setSeatNumber(s.getSeatNumber()); p.setBooking(b); b.getPassengers().add(p);} 
  flight.setAvailableSeats(Math.max(0, flight.getAvailableSeats()-selected.size())); flights.save(flight);
  Payment payment=new Payment(); payment.setMethod(pay.method); payment.setAmount(b.getTotalAmount()); payment.setMaskedReference(mask(pay)); payment.setBooking(b); b.setPayment(payment);
  Ticket t=new Ticket(); t.setTicketNumber(CodeGenerator.ticket()); t.setBooking(b); b.setTicket(t); return bookings.save(b);
 }
 private String get(List<String> l,int i,String def){return l!=null&&i<l.size()&&l.get(i)!=null&&!l.get(i).isBlank()?l.get(i):def;} private int getInt(List<Integer> l,int i,int def){return l!=null&&i<l.size()&&l.get(i)!=null?l.get(i):def;}
 private String mask(PaymentDto p){ if("UPI".equalsIgnoreCase(p.method)) return p.upiId; if(p.cardNumber!=null&&p.cardNumber.length()>=4)return "**** **** **** "+p.cardNumber.substring(p.cardNumber.length()-4); return "MOCK-PAYMENT";}
 public List<Booking> userBookings(Long userId){return bookings.findByUserIdOrderByBookingDateDesc(userId);} public List<Booking> all(){return bookings.findAll();} public Booking get(Long id){return bookings.findById(id).orElseThrow();}
 @Transactional public void cancel(Long id,Long userId,boolean admin){Booking b=get(id); if(!admin && !b.getUser().getId().equals(userId)) throw new IllegalArgumentException("Not allowed"); if(b.getStatus()==BookingStatus.CANCELLED)return; b.setStatus(BookingStatus.CANCELLED); for(Passenger p:b.getPassengers()){ for(Seat s:seats.findByFlightIdOrderBySeatNumber(b.getFlight().getId())) if(s.getSeatNumber().equals(p.getSeatNumber())){s.setBooked(false);seats.save(s);} } Flight f=b.getFlight(); f.setAvailableSeats(f.getAvailableSeats()+b.getSeatCount()); flights.save(f); bookings.save(b);} public long count(){return bookings.count();} public BigDecimal revenue(){return bookings.findAll().stream().filter(b->b.getStatus()==BookingStatus.CONFIRMED).map(Booking::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);} }
