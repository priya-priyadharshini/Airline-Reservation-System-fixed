package com.airline.controller;

import com.airline.dto.PaymentDto;
import com.airline.entity.Booking;
import com.airline.entity.Flight;
import com.airline.entity.Passenger;
import com.airline.service.BookingService;
import com.airline.service.FlightService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final FlightService flights;
    private final BookingService bookings;

    public UserController(FlightService flights, BookingService bookings) {
        this.flights = flights;
        this.bookings = bookings;
    }

    @GetMapping("/dashboard")
    public String dash(Model model) {
        model.addAttribute("flights", flights.available());
        return "user/dashboard";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String source,
                         @RequestParam(required = false) String destination,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         Model model) {
        model.addAttribute("flights", flights.search(source, destination, date));
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        return "user/search";
    }

    @GetMapping("/book/{flightId}")
    public String seats(@PathVariable Long flightId, Model model) {
        Flight flight = flights.get(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("seats", flights.seats(flightId));
        return "user/seats";
    }

    @PostMapping("/book/{flightId}/passengers")
    public String passengers(@PathVariable Long flightId,
                             @RequestParam(required = false) List<Long> seatIds,
                             Model model) {
        if (seatIds == null || seatIds.isEmpty()) {
            model.addAttribute("flight", flights.get(flightId));
            model.addAttribute("seats", flights.seats(flightId));
            model.addAttribute("error", "Please select at least one seat");
            return "user/seats";
        }
        model.addAttribute("flight", flights.get(flightId));
        model.addAttribute("seatIds", seatIds);
        return "user/passengers";
    }

    @PostMapping("/book/{flightId}/payment")
    public String payment(@PathVariable Long flightId,
                          @RequestParam List<Long> seatIds,
                          @RequestParam List<String> names,
                          @RequestParam List<Integer> ages,
                          @RequestParam List<String> genders,
                          Model model) {
        Flight flight = flights.get(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("seatIds", seatIds);
        model.addAttribute("names", names);
        model.addAttribute("ages", ages);
        model.addAttribute("genders", genders);
        model.addAttribute("amount", flight.getPrice().multiply(java.math.BigDecimal.valueOf(seatIds.size())));
        return "user/payment";
    }

    @PostMapping("/book/{flightId}/confirm")
    public String confirm(@PathVariable Long flightId,
                          @RequestParam List<Long> seatIds,
                          @RequestParam List<String> names,
                          @RequestParam List<Integer> ages,
                          @RequestParam List<String> genders,
                          PaymentDto payment,
                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Booking booking = bookings.create(userId, flightId, seatIds, names, ages, genders, payment);
        return "redirect:/user/ticket/" + booking.getId();
    }

    @GetMapping("/ticket/{id}")
    public String ticket(@PathVariable Long id, Model model) {
        model.addAttribute("booking", bookings.get(id));
        return "user/ticket";
    }

    @GetMapping("/bookings")
    public String myBookings(HttpSession session, Model model) {
        model.addAttribute("bookings", bookings.userBookings((Long) session.getAttribute("userId")));
        return "user/bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancel(@PathVariable Long id, HttpSession session) {
        bookings.cancel(id, (Long) session.getAttribute("userId"), false);
        return "redirect:/user/bookings";
    }

    @GetMapping("/ticket/{id}/download")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) throws Exception {
        Booking booking = bookings.get(id);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        document.add(new Paragraph("PRIYA AIRWAYS - E-TICKET", titleFont));
        document.add(new Paragraph("PNR: " + booking.getPnr(), boldFont));
        document.add(new Paragraph("Ticket Number: " + booking.getTicket().getTicketNumber()));
        document.add(new Paragraph("Flight: " + booking.getFlight().getFlightNumber() + " - " + booking.getFlight().getAirlineName()));
        document.add(new Paragraph("Route: " + booking.getFlight().getSource() + " to " + booking.getFlight().getDestination()));
        document.add(new Paragraph("Departure: " + booking.getFlight().getDepartureDate() + " " + booking.getFlight().getDepartureTime()));
        document.add(new Paragraph("Arrival: " + booking.getFlight().getArrivalDate() + " " + booking.getFlight().getArrivalTime()));
        document.add(new Paragraph("Payment: " + booking.getPayment().getMethod() + " - " + booking.getPayment().getStatus()));
        document.add(new Paragraph("Total Amount: Rs. " + booking.getTotalAmount()));
        document.add(new Paragraph("Status: " + booking.getStatus()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Passengers", boldFont));
        for (Passenger passenger : booking.getPassengers()) {
            document.add(new Paragraph(passenger.getName() + " | Age: " + passenger.getAge() + " | Gender: " + passenger.getGender() + " | Seat: " + passenger.getSeatNumber()));
        }
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Scan QR for booking verification", boldFont));
        Image qr = Image.getInstance(createQrImage("PNR:" + booking.getPnr() + ";TICKET:" + booking.getTicket().getTicketNumber() + ";FLIGHT:" + booking.getFlight().getFlightNumber()));
        qr.scaleAbsolute(120, 120);
        document.add(qr);
        document.add(new Paragraph("This is a demo project ticket generated by Airline Reservation System."));
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket-" + booking.getPnr() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(output.toByteArray());
    }

    private byte[] createQrImage(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 180, 180);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", png);
        return png.toByteArray();
    }
}
