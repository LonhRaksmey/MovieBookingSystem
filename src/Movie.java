import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movie {

    // ANSI Color Codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_GREEN_BOLD = "\u001B[1;32m";
    public static final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
    public static final String ANSI_BLUE_BOLD = "\u001B[1;34m";
    public static final String ANSI_RED_BOLD = "\u001B[1;31m";
    public static final String ANSI_CYAN_BOLD = "\u001B[1;36m";

    // DateTime Formatter
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:");
    static void welcome() {
        System.out.println("\n" +
                " _    _ _____ _     _____ ________  ___ _____ \n" +
                "| |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|\n" +
                "| |  | | |__ | |   | /  \\/ | | | .  . || |__  \n" +
                "| |/\\| |  __|| |   | |   | | | | |\\/| ||  __| \n" +
                "\\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___ \n" +
                " \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/");

        System.out.print("-".repeat(20));
        System.out.print("HALL Movie Booking System");
        System.out.print("-".repeat(20));
        System.out.println();
    }

    // Helper function to convert 0-based column index to letter
    private static String colIndexToLetter(int colIndex) {
        if (colIndex >= 0 && colIndex < 26) { // Handles A-Z
            return String.valueOf((char)('A' + colIndex));
        }
        return "?"; // Fallback for unexpected index, though NUM_COLS limits this
    }
    public static void main(String[] args) {

        welcome();
        // --- Configuration ---
        final int NUM_ROWS = 5;
        final int NUM_COLS = 10; // Max 26 for current colIndexToLetter
        final String[] HALL_NAMES = {"Morning", "Evening", "Tonight"};
        final int MAX_BK = 100;

        // --- Initialize Data Structures ---
        int[][] morningSeats = new int[NUM_ROWS][NUM_COLS];
        int[][] eveningSeats = new int[NUM_ROWS][NUM_COLS];
        int[][] tonightSeats = new int[NUM_ROWS][NUM_COLS];
        int[][][] allHallSeats = {morningSeats, eveningSeats, tonightSeats};

        // Booking history using fixed-size parallel arrays
        int[] bookingIds = new int[MAX_BK];
        int[] bookingHallIndices = new int[MAX_BK];
        int[] bookingRows = new int[MAX_BK]; // 0-based row index
        int[] bookingCols = new int[MAX_BK]; // 0-based column index
        String[] bookingCustomerNames = new String[MAX_BK];
        String[] bookingStatuses = new String[MAX_BK]; // Current status

        // Timestamps for history
        String[] bookingTimeStrings = new String[MAX_BK];      // Time of booking creation
        String[] checkInTimeStrings = new String[MAX_BK];      // Time of check-in
        String[] checkOutTimeStrings = new String[MAX_BK];     // Time of check-out

        int nextBookingId = 1;
        int currentBookingCount = 0;

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Initialize timestamp arrays to empty strings or a placeholder
        for (int i = 0; i < MAX_BK; i++) {
            bookingTimeStrings[i] = "";
            checkInTimeStrings[i] = "";
            checkOutTimeStrings[i] = "";
        }

        while (running) {
            System.out.println(ANSI_CYAN_BOLD + "\n--- HALL Movie Booking System ---" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "A." + ANSI_RESET + " Booking");
            System.out.println(ANSI_YELLOW + "B." + ANSI_RESET + " Check In");
            System.out.println(ANSI_YELLOW + "C." + ANSI_RESET + " Check Out");
            System.out.println(ANSI_YELLOW + "D." + ANSI_RESET + " Display Hall Seating");
            System.out.println(ANSI_YELLOW + "E." + ANSI_RESET + " History");
            System.out.println(ANSI_YELLOW + "F." + ANSI_RESET + " Exit");
            System.out.print(ANSI_GREEN + "Enter your choice (A-F): " + ANSI_RESET);

            String Str = scanner.nextLine().trim().toUpperCase();
            char choice = (Str.isEmpty()) ? ' ' : Str.charAt(0);

            switch (choice) {
                case 'A': { // Booking
                    System.out.println(ANSI_BLUE_BOLD + "\n--- Book a Seat ---" + ANSI_RESET);
                    if (currentBookingCount >= MAX_BK) {
                        System.out.println(ANSI_RED + "Sorry, the system cannot store more bookings. Maximum limit reached." + ANSI_RESET);
                        break;
                    }

                    System.out.println(ANSI_PURPLE + "Available Halls:" + ANSI_RESET);
                    for (int i = 0; i < HALL_NAMES.length; i++) {
                        System.out.println(ANSI_YELLOW + (i + 1) + ". " + ANSI_CYAN + HALL_NAMES[i] + ANSI_RESET);
                    }

                    int hallChoiceIdx = -1;
                    while (true) {
                        System.out.print(ANSI_GREEN + "Select hall (1-" + HALL_NAMES.length + "): " + ANSI_RESET);
                        if (scanner.hasNextInt()) {
                            int hallInput = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            if (hallInput >= 1 && hallInput <= HALL_NAMES.length) {
                                hallChoiceIdx = hallInput - 1;
                                break;
                            } else {
                                System.out.println(ANSI_RED + "Invalid hall selection." + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED + "Invalid input. Please enter a number." + ANSI_RESET);
                            scanner.nextLine(); // Consume invalid input
                        }
                    }

                    String selectedHallName = HALL_NAMES[hallChoiceIdx];
                    int[][] selectedHallSeats = allHallSeats[hallChoiceIdx];

                    System.out.println(ANSI_BLUE_BOLD + "\n--- Seating for " + ANSI_CYAN + selectedHallName + ANSI_BLUE_BOLD
                            + " Hall [R][C] ---" + ANSI_RESET);
                    System.out.print(ANSI_YELLOW_BOLD + "   "); // For row numbers
                    for (int c = 0; c < NUM_COLS; c++) {
                        System.out.printf("%3s", colIndexToLetter(c)); // Display A, B, C...
                    }
                    System.out.println(ANSI_RESET);
                    for (int r = 0; r < NUM_ROWS; r++) {
                        System.out.printf(ANSI_YELLOW_BOLD + "%2d " + ANSI_RESET, r + 1); // Row numbers 1-based
                        for (int c = 0; c < NUM_COLS; c++) {
                            if (selectedHallSeats[r][c] == 0) {
                                System.out.print(ANSI_GREEN + "  O" + ANSI_RESET);
                            } else {
                                System.out.print(ANSI_RED + "  X" + ANSI_RESET);
                            }
                        }
                        System.out.println();
                    }
                    System.out.println(ANSI_BLUE + "-".repeat(NUM_COLS * 3 + 3) + ANSI_RESET);

                    int rowInput = -1; // 1-based for user display
                    int rowIdx = -1, colIdx = -1; // 0-based for array access

                    while (true) {
                        System.out.print(ANSI_GREEN + "Enter row number (1-" + NUM_ROWS + "): " + ANSI_RESET);
                        if (scanner.hasNextInt()) {
                            rowInput = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            if (rowInput >= 1 && rowInput <= NUM_ROWS) {
                                // Row input is valid, now get column input
                                System.out.print(ANSI_GREEN + "Enter column letter (A-" + colIndexToLetter(NUM_COLS - 1) + "): " + ANSI_RESET);
                                String colLetterInputStr = scanner.nextLine().trim().toUpperCase();

                                if (colLetterInputStr.length() == 1) {
                                    char colLetter = colLetterInputStr.charAt(0);
                                    if (colLetter >= 'A' && colLetter < (char)('A' + NUM_COLS)) {
                                        rowIdx = rowInput - 1; // Convert 1-based row to 0-based index
                                        colIdx = colLetter - 'A'; // Convert 'A'..'J' to 0..9
                                        break; // Both row and column are valid, exit loop
                                    } else {
                                        System.out.println(ANSI_RED + "Invalid column letter. Must be between A and " + colIndexToLetter(NUM_COLS - 1) + "." + ANSI_RESET);
                                    }
                                } else {
                                    System.out.println(ANSI_RED + "Invalid column input. Please enter a single letter." + ANSI_RESET);
                                }
                            } else {
                                System.out.println(ANSI_RED + "Invalid row number." + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED + "Invalid row input. Please enter a number." + ANSI_RESET);
                            scanner.nextLine(); // Consume invalid input
                        }
                    }


                    if (selectedHallSeats[rowIdx][colIdx] == 0) {
                        System.out.print(ANSI_GREEN + "Enter customer name: " + ANSI_RESET);
                        String customerName = scanner.nextLine();

                        selectedHallSeats[rowIdx][colIdx] = nextBookingId;

                        bookingIds[currentBookingCount] = nextBookingId;
                        bookingHallIndices[currentBookingCount] = hallChoiceIdx;
                        bookingRows[currentBookingCount] = rowIdx; // Store 0-based row index
                        bookingCols[currentBookingCount] = colIdx; // Store 0-based col index
                        bookingCustomerNames[currentBookingCount] = customerName;
                        bookingStatuses[currentBookingCount] = "Booked";
                        bookingTimeStrings[currentBookingCount] = LocalDateTime.now().format(df);
                        checkInTimeStrings[currentBookingCount] = "N/A";
                        checkOutTimeStrings[currentBookingCount] = "N/A";


                        System.out.println(ANSI_GREEN_BOLD + "Booking successful! Your Booking ID is: " + ANSI_YELLOW_BOLD + nextBookingId +
                                ANSI_GREEN_BOLD + ". Seat: " + ANSI_CYAN + selectedHallName +
                                ANSI_GREEN_BOLD + " Row " + ANSI_YELLOW + rowInput + // Display 1-based row
                                ANSI_GREEN_BOLD + " Col " + ANSI_YELLOW + colIndexToLetter(colIdx) + // Display letter for col
                                "." + ANSI_RESET);
                        nextBookingId++;
                        currentBookingCount++;
                    } else {
                        System.out.println(ANSI_RED + "Sorry, this seat (ROW " + rowInput + ", COL " + colIndexToLetter(colIdx) + ") is already booked." + ANSI_RESET);
                    }
                    break;
                }

                case 'B': { // Check In
                    System.out.println(ANSI_BLUE_BOLD + "\n--- 🗝️Check In ---" + ANSI_RESET);
                    if (currentBookingCount == 0) {
                        System.out.println(ANSI_YELLOW + "No bookings available to check in." + ANSI_RESET);
                        break;
                    }
                    System.out.print(ANSI_GREEN + "Enter Booking ID to Check In: " + ANSI_RESET);
                    if (scanner.hasNextInt()) {
                        int bookingIdToCheckIn = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        boolean foundBooking = false;
                        for (int i = 0; i < currentBookingCount; i++) {
                            if (bookingIds[i] == bookingIdToCheckIn) {
                                foundBooking = true;
                                if ("Booked".equals(bookingStatuses[i])) {
                                    bookingStatuses[i] = "Checked-In";
                                    checkInTimeStrings[i] = LocalDateTime.now().format(df);
                                    System.out.println(ANSI_GREEN_BOLD + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckIn + ANSI_GREEN_BOLD + " checked in successfully." + ANSI_RESET);
                                } else if ("Checked-In".equals(bookingStatuses[i])) {
                                    System.out.println(ANSI_YELLOW + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckIn + ANSI_YELLOW + " is already checked in." + ANSI_RESET);
                                } else {
                                    System.out.println(ANSI_RED + "Cannot check in. Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckIn + ANSI_RED + " status: " + bookingStatuses[i] + "." + ANSI_RESET);
                                }
                                break;
                            }
                        }
                        if (!foundBooking) {
                            System.out.println(ANSI_RED + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckIn + ANSI_RED + " not found." + ANSI_RESET);
                        }
                    } else {
                        System.out.println(ANSI_RED + "Invalid Booking ID format. Please enter a number." + ANSI_RESET);
                        scanner.nextLine(); // Consume invalid input
                    }
                    break;
                }

                case 'C': { // Check Out
                    System.out.println(ANSI_BLUE_BOLD + "\n--- Check Out ---" + ANSI_RESET);
                    if (currentBookingCount == 0) {
                        System.out.println(ANSI_YELLOW + "No bookings available to check out." + ANSI_RESET);
                        break;
                    }
                    System.out.print(ANSI_GREEN + "Enter Booking ID to Check Out: " + ANSI_RESET);
                    if (scanner.hasNextInt()) {
                        int bookingIdToCheckout = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        int foundBookingIdx = -1;
                        for (int i = 0; i < currentBookingCount; i++) {
                            if (bookingIds[i] == bookingIdToCheckout) {
                                foundBookingIdx = i;
                                break;
                            }
                        }

                        if (foundBookingIdx != -1) {
                            if ("Checked-Out".equals(bookingStatuses[foundBookingIdx])) {
                                System.out.println(ANSI_YELLOW + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckout + ANSI_YELLOW + " is already checked out." + ANSI_RESET);
                            } else {
                                int hallIdxForSeatArray = bookingHallIndices[foundBookingIdx];
                                int rowOfBooking = bookingRows[foundBookingIdx]; // 0-based
                                int colOfBooking = bookingCols[foundBookingIdx]; // 0-based

                                allHallSeats[hallIdxForSeatArray][rowOfBooking][colOfBooking] = 0; // Free the seat
                                bookingStatuses[foundBookingIdx] = "Checked-Out";
                                checkOutTimeStrings[foundBookingIdx] = LocalDateTime.now().format(df);
                                System.out.println(ANSI_GREEN_BOLD + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckout +
                                        ANSI_GREEN_BOLD + " checked out successfully. Seat R" + (rowOfBooking + 1) + // Display 1-based row
                                        " C" + colIndexToLetter(colOfBooking) + // Display letter for col
                                        " in " + ANSI_CYAN + HALL_NAMES[hallIdxForSeatArray] +
                                        ANSI_GREEN_BOLD + " hall is now available." + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED + "Booking ID " + ANSI_YELLOW_BOLD + bookingIdToCheckout + ANSI_RED + " not found." + ANSI_RESET);
                        }
                    } else {
                        System.out.println(ANSI_RED + "Invalid Booking ID format. Please enter a number." + ANSI_RESET);
                        scanner.nextLine(); // Consume invalid input
                    }
                    break;
                }

                case 'D': { // Display Hall Seating
                    System.out.println(ANSI_BLUE_BOLD + "\n--- Display Hall Seating ---" + ANSI_RESET);
                    System.out.println(ANSI_PURPLE + "Available Halls:" + ANSI_RESET);
                    for (int i = 0; i < HALL_NAMES.length; i++) {
                        System.out.println(ANSI_YELLOW + (i + 1) + ". " + ANSI_CYAN + HALL_NAMES[i] + ANSI_RESET);
                    }

                    int hallChoiceDisplayIdx = -1;
                    while (true) {
                        System.out.print(ANSI_GREEN + "Select hall to display (1-" + HALL_NAMES.length + "): " + ANSI_RESET);
                        if (scanner.hasNextInt()) {
                            int hallInput = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            if (hallInput >= 1 && hallInput <= HALL_NAMES.length) {
                                hallChoiceDisplayIdx = hallInput - 1;
                                break;
                            } else {
                                System.out.println(ANSI_RED + "Invalid hall selection." + ANSI_RESET);
                            }
                        } else {
                            System.out.println(ANSI_RED + "Invalid input. Please enter a number." + ANSI_RESET);
                            scanner.nextLine(); // Consume invalid input
                        }
                    }

                    String displayHallName = HALL_NAMES[hallChoiceDisplayIdx];
                    int[][] displayHallSeats = allHallSeats[hallChoiceDisplayIdx];

                    System.out.println(ANSI_BLUE_BOLD + "\n--- Seating for " + ANSI_CYAN + displayHallName + ANSI_BLUE_BOLD + " Hall [R][C] ---" + ANSI_RESET);
                    System.out.print(ANSI_YELLOW_BOLD + "   "); // For row numbers
                    for (int c = 0; c < NUM_COLS; c++) {
                        System.out.printf("%3s", colIndexToLetter(c)); // Display A, B, C...
                    }
                    System.out.println(ANSI_RESET);
                    for (int r = 0; r < NUM_ROWS; r++) {
                        System.out.printf(ANSI_YELLOW_BOLD + "%2d " + ANSI_RESET, r + 1); // Row numbers 1-based
                        for (int c = 0; c < NUM_COLS; c++) {
                            if (displayHallSeats[r][c] == 0) {
                                System.out.print(ANSI_GREEN + "  O" + ANSI_RESET);
                            } else {
                                System.out.print(ANSI_RED + "  X" + ANSI_RESET);
                            }
                        }
                        System.out.println();
                    }
                    System.out.println(ANSI_BLUE + "-".repeat(NUM_COLS * 3 + 3) + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "O" + ANSI_RESET + " = Available, " + ANSI_RED + "X" + ANSI_RESET + " = Booked/Occupied");
                    break;
                }

                case 'E': { // History
                    System.out.println(ANSI_BLUE_BOLD + "\n--- Booking History ---" + ANSI_RESET);
                    if (currentBookingCount == 0) {
                        System.out.println(ANSI_YELLOW + "No bookings have been made yet." + ANSI_RESET);
                    } else {
                        // Header
                        System.out.printf(ANSI_CYAN_BOLD + "%-4s | %-18s | %-7s | %-10s | %-11s | %-19s | %-19s | %-19s\n" + ANSI_RESET,
                                "ID", "Customer", "Seat", "Hall", "Status", "Booked Time", "Checked-In Time", "Checked-Out Time");
                        System.out.println(ANSI_CYAN + "-".repeat(130) + ANSI_RESET);

                        for (int i = 0; i < currentBookingCount; i++) {
                            String statusColor = ANSI_RESET;
                            if ("Booked".equals(bookingStatuses[i])) statusColor = ANSI_YELLOW;
                            else if ("Checked-In".equals(bookingStatuses[i])) statusColor = ANSI_GREEN;
                            else if ("Checked-Out".equals(bookingStatuses[i])) statusColor = ANSI_RED;

                            // Display 1-based row and letter for column
                            String seatInfo = "R" + (bookingRows[i] + 1) + "|C-" + colIndexToLetter(bookingCols[i]);

                            System.out.printf(ANSI_YELLOW_BOLD + "%-4d" + ANSI_RESET + " | " + "%-18.18s | " +
                                            ANSI_PURPLE + "%-7s" + ANSI_RESET + " | " + ANSI_CYAN + "%-10s" + ANSI_RESET + " | " +
                                            statusColor + "%-11s" + ANSI_RESET + " | " +
                                            ANSI_GREEN + "%-19s" + ANSI_RESET + " | " +
                                            ANSI_BLUE + "%-19s" + ANSI_RESET + " | " +
                                            ANSI_RED + "%-19s" + ANSI_RESET + "\n",
                                    bookingIds[i],
                                    bookingCustomerNames[i],
                                    seatInfo, // Use formatted seatInfo
                                    HALL_NAMES[bookingHallIndices[i]],
                                    bookingStatuses[i],
                                    bookingTimeStrings[i].isEmpty() ? "N/A" : bookingTimeStrings[i],
                                    checkInTimeStrings[i].isEmpty() ? "N/A" : checkInTimeStrings[i],
                                    checkOutTimeStrings[i].isEmpty() ? "N/A" : checkOutTimeStrings[i]);
                        }
                    }
                    break;
                }

                case 'F': {
                    System.out.println(ANSI_RED_BOLD + "\nExiting Movie Booking System. Goodbye!" + ANSI_RESET);
                    running = false;
                    break;
                }

                default: {
                    System.out.println(ANSI_RED_BOLD + "Invalid choice. Please select an option from A to F." + ANSI_RESET);
                    break;
                }
            }
        }
    }
}