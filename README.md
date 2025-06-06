# Movie Booking System 🎬 [ it's small for junior ]

A console-based Java application for managing movie theater seat bookings with hall selection, seat reservation, and booking history tracking.

## Features ✨

- **Multi-hall Support**: Manage bookings across different showtimes (Morning, Evening, Tonight)
- **Interactive Seat Map**: Visual representation of available/booked seats
- **Booking Lifecycle**:
  - Book seats with customer details
  - Check-in functionality
  - Check-out to free up seats
- **Comprehensive History**: View all bookings with timestamps
- **User-Friendly Interface**: Color-coded console output with intuitive menus

## Technical Details ⚙️

- **Language**: Java
- **Dependencies**:
  - Java 8+
  - `java.time` for timestamp management
- **Data Structures**:
  - 2D arrays for seat management
  - Parallel arrays for booking history
- **ANSI Colors**: Enhanced console output with colored text

## Installation & Usage 🚀

1. **Requirements**:
   - Java JDK 8 or later
   - Terminal/Command Prompt

2. **Running the Application**:
   ```bash
   javac Movie.java
   java Movie


## Code Structure 📂
Movie.java
└───main()
    ├───welcome() - Displays welcome banner
    ├───colIndexToLetter() - Helper for column display
    ├───Seat management arrays
    ├───Booking history arrays
    └───Main menu loop with switch-case

