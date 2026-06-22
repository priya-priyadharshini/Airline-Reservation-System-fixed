package com.airline.controller;
import com.airline.entity.*; import com.airline.service.*; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/admin")
public class AdminController{
 private final FlightService flights; private final UserService users; private final BookingService bookings; public AdminController(FlightService flights,UserService users,BookingService bookings){this.flights=flights;this.users=users;this.bookings=bookings;}
 @GetMapping("/dashboard") public String dash(Model m){m.addAttribute("flightCount",flights.count());m.addAttribute("userCount",users.count());m.addAttribute("bookingCount",bookings.count());m.addAttribute("revenue",bookings.revenue());return "admin/dashboard";}
 @GetMapping("/flights") public String fl(Model m){m.addAttribute("flights",flights.available());return "admin/flights";}
 @GetMapping("/flights/new") public String newF(Model m){m.addAttribute("flight",new Flight());m.addAttribute("statuses",FlightStatus.values());return "admin/flight-form";}
 @GetMapping("/flights/{id}/edit") public String edit(@PathVariable Long id,Model m){m.addAttribute("flight",flights.get(id));m.addAttribute("statuses",FlightStatus.values());return "admin/flight-form";}
 @PostMapping("/flights/save") public String save(Flight f){flights.save(f);return "redirect:/admin/flights";}
 @PostMapping("/flights/{id}/delete") public String del(@PathVariable Long id){flights.delete(id);return "redirect:/admin/flights";}
 @GetMapping("/users") public String us(Model m){m.addAttribute("users",users.all());return "admin/users";}
 @PostMapping("/users/{id}/toggle") public String tog(@PathVariable Long id){users.toggle(id);return "redirect:/admin/users";}
 @PostMapping("/users/{id}/delete") public String du(@PathVariable Long id){users.delete(id);return "redirect:/admin/users";}
 @GetMapping("/bookings") public String bo(Model m){m.addAttribute("bookings",bookings.all());return "admin/bookings";}
 @PostMapping("/bookings/{id}/cancel") public String cb(@PathVariable Long id){bookings.cancel(id,null,true);return "redirect:/admin/bookings";}
}
