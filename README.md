# Flight Booking Backend

This is the backend application for a flight booking system. It provides RESTful API endpoints for managing flights, seats, users, and other related functionalities.

## Frontend Repository

https://github.com/maksym7576/aerolineaDonBosco-front-

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JPA / Hibernate
- MySQL (assumed, please confirm your database choice)

## API Endpoints

### AuthController

- `POST /api/auth/login`: User login
  - Body: { username, password }
  - Returns: AuthResponse

- `POST /api/auth/register`: User registration
  - Body: { username, email, password }
  - Returns: AuthResponse

### FlightImageController

- `GET /api/image/flight/{flightId}`: Get images for a specific flight
- `POST /api/image/create/{flightId}`: Upload an image for a flight
- `DELETE /api/image/delete/{id}`: Delete a flight image
- `PUT /api/image/update/{flightImageId}`: Update a flight image

### FlightsController

- `GET /api/flight/get`: Get all flights
- `GET /api/flight/get/{id}`: Get a specific flight
- `GET /api/flight/search`: Search for flights
  - Parameters: originCountry, originCity, destinationCountry, destinationCity, localDate (optional)
- `DELETE /api/flight/delete/{id}`: Delete a flight
- `PUT /api/flight/update/{id}`: Update a flight
- `POST /api/flight/create`: Create a new flight

### PassengersController

- `POST /api/v1/new/passengers`: Create a new passenger
- `GET /api/v1/passengers/{id}`: Get a specific passenger
- `GET /api/v1/passengers`: Get all passengers
- `PUT /api/v1/update/passengers/{id}`: Update a passenger
- `DELETE /api/v1/delete/passengers/{id}`: Delete a passenger

### ReservationController

- `POST /api/v1/new/seats`: Create a new seats
- `GET /api/v1/seats/{id}`: Get a specific seats
- `GET /api/v1/seats`: Get all seats
- `PUT /api/v1/update/seats/{id}`: Update a seats
- `DELETE /api/v1/delete/seats/{id}`: Delete a seats
- `PUT /api/v1/return/{reservationId}`: Return a seats
- `GET /api/v1/seats/user/{userId}`: Get all seats for a user

### RoutesController

- `POST /api/v1/new/routes`: Create a new route
- `GET /api/v1/routes/{id}`: Get a specific route
- `GET /api/v1/routes`: Get all routes
- `PUT /api/v1/update/routes/{id}`: Update a route
- `DELETE /api/v1/delete/routes/{id}`: Delete a route

### UserController

- `DELETE /api/user/delete/{id}`: Delete a user
- `PUT /api/user/updateUsername/{id}`: Update a user's username
- `GET /api/user`: Get the current user's profile
- `GET /api/user/getUsername`: Get a user by username
- `PUT /api/user/updatePassword/{id}`: Update a user's password
- `GET /api/user/get/{id}`: Get a user by ID
- `PUT /api/user/updateRole/{id}`: Update a user's role

### WalletController

- `GET /api/wallet/{id}`: Get a wallet by ID
- `POST /api/wallet/create/{id}`: Create a wallet for a user
- `GET /api/wallet/user/{userId}`: Get a wallet by user ID
- `PUT /api/wallet/user/addMoney/{userId}`: Add money to a user's wallet

## Setup and Installation

1. Clone the repository:
   ```
   git clone [repository URL]
   ```

2. Navigate to the project directory:
   ```
   cd flight-booking-backend
   ```

## Security

This application uses Spring Security for authentication and authorization. Make sure to include the JWT token in the Authorization header for protected endpoints.

## Contributing

[https://github.com/Jrdevangel]
[https://github.com/abelloch]
[https://github.com/maksym7576]
