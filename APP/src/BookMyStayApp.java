// UseCase8BookingHistoryReport.java

import java.util.*;

// -------------------- RESERVATION MODEL --------------------

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// -------------------- BOOKING HISTORY (DATA STORAGE) --------------------

class BookingHistory {

    // List preserves insertion order (chronological history)
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Store confirmed reservation
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Retrieve all reservations (read-only purpose)
    public List<Reservation> getAllReservations() {
        return confirmedBookings;
    }
}

// -------------------- REPORT SERVICE --------------------

class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display full booking history
    public void generateFullReport() {

        System.out.println("\n--- Booking History Report ---");

        List<Reservation> reservations = bookingHistory.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation reservation : reservations) {
            reservation.displayReservation();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {

        List<Reservation> reservations = bookingHistory.getAllReservations();

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Confirmed Bookings: " + reservations.size());

        // Count room types
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation reservation : reservations) {
            roomTypeCount.put(
                    reservation.getRoomType(),
                    roomTypeCount.getOrDefault(reservation.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " bookings");
        }
    }
}

// -------------------- APPLICATION ENTRY --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 8.0");
        System.out.println("UC8: Booking History & Reporting");
        System.out.println("=================================");

        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings
        bookingHistory.addReservation(new Reservation("SI101", "Arun", "Single Room"));
        bookingHistory.addReservation(new Reservation("SU201", "Meena", "Suite Room"));
        bookingHistory.addReservation(new Reservation("DO301", "Rahul", "Double Room"));
        bookingHistory.addReservation(new Reservation("SI102", "Divya", "Single Room"));

        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Admin requests reports
        reportService.generateFullReport();
        reportService.generateSummaryReport();

        System.out.println("\nBooking data remains unchanged.");
        System.out.println("Application Terminated.");
    }
}