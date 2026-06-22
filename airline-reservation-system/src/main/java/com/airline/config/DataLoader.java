package com.airline.config;
import com.airline.entity.Flight;
import com.airline.entity.FlightStatus;
import com.airline.entity.Role;
import com.airline.entity.Seat;
import com.airline.entity.User;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatRepository;
import com.airline.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Configuration
public class DataLoader{
 @Bean CommandLineRunner seed(UserRepository users, FlightRepository flights, SeatRepository seats, PasswordEncoder enc){return args->{
  if(users.findByEmailIgnoreCase("admin@airline.com").isEmpty()){User a=new User(); a.setFullName("Admin"); a.setEmail("admin@airline.com"); a.setPhone("9999999999"); a.setPassword(enc.encode("Admin@123")); a.setRole(Role.ADMIN); a.setActive(true); users.save(a);} 
  if(users.findByEmailIgnoreCase("user@airline.com").isEmpty()){User u=new User(); u.setFullName("Demo User"); u.setEmail("user@airline.com"); u.setPhone("8888888888"); u.setPassword(enc.encode("User@123")); u.setRole(Role.USER); u.setActive(true); users.save(u);} 
  if(flights.count()==0){String[] sources={"Chennai","Madurai","Bangalore","Delhi","Mumbai","Hyderabad"}; String[] dest={"Delhi","Chennai","Mumbai","Hyderabad","Bangalore","Madurai"}; for(int i=1;i<=18;i++){Flight f=new Flight(); f.setFlightNumber("AR"+(100+i)); f.setAirlineName(i%2==0?"Priya Airways":"SkyLine Express"); f.setSource(sources[i%sources.length]); f.setDestination(dest[i%dest.length]); f.setDepartureDate(LocalDate.now().plusDays((i%8)+1)); f.setDepartureTime(LocalTime.of(6+(i%12), (i%2)*30)); f.setArrivalDate(f.getDepartureDate()); f.setArrivalTime(f.getDepartureTime().plusHours(2+(i%3))); f.setPrice(BigDecimal.valueOf(3200+i*180)); f.setTotalSeats(36); f.setAvailableSeats(36); f.setStatus(FlightStatus.SCHEDULED); Flight saved=flights.save(f); for(int s=1;s<=36;s++){Seat seat=new Seat(); seat.setSeatNumber("S"+s); seat.setSeatType((s%6==1||s%6==0)?"Window":(s%6==2||s%6==5)?"Middle":"Aisle"); seat.setFlight(saved); seats.save(seat);} }}
 };}
}
