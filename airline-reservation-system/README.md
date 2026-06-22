# Airline Reservation System

A complete Spring Boot + Thymeleaf airline booking project for college submission and viva.

## Features

### User
- Role-based login using dropdown: USER / ADMIN
- Email/password validation
- User registration
- User dashboard with available flights
- Flight search by source, destination and date
- Visual seat selection
- Passenger details entry
- Professional mock payment page with multiple payment methods
- E-ticket generation
- Downloadable PDF ticket with QR code
- View booking history
- Cancel confirmed bookings

### Admin
- Admin dashboard with cards: total flights, users, bookings and revenue
- Add flights
- Edit flights
- Delete flights
- View users
- Activate/deactivate users
- View and cancel bookings

## Technology Stack

- Java 17
- Spring Boot 3.3.4
- Spring Data JPA
- Thymeleaf
- Bootstrap 5
- H2 embedded database
- Maven
- OpenPDF
- ZXing QR Code

## How to Run

Open PowerShell inside the project folder where `pom.xml` is present.

```powershell
mvn clean spring-boot:run
```

Open browser:

```text
http://127.0.0.1:8080
```

## Login Details

### Admin

```text
Email: admin@airline.com
Password: Admin@123
Role: ADMIN
```

### User

```text
Email: user@airline.com
Password: User@123
Role: USER
```

## H2 Database Console

```text
http://127.0.0.1:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:file:./data/airline_db
User Name: sa
Password: password
```

## Recommended Demo Flow

1. Login as Admin.
2. View dashboard.
3. Add or edit a flight.
4. Logout.
5. Login as User.
6. Search flights.
7. Select flight and seats.
8. Enter passenger details.
9. Complete mock payment.
10. View and download ticket PDF.
11. Cancel booking from My Bookings.

