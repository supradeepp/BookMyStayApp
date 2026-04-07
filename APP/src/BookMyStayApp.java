// UseCase10BookingCancellation.java

import java.util.*;

// -------------------- RESERVATION MODEL --------------------

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.active = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        this.active = false;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Status: " + (active ? "CONFIRMED" : "CANCELLED"));
    }
}

// -------------------- INVENTORY SERVICE --------------------

class InventoryService {

    private Map<String, Integer> availability;

    public InventoryService() {
        availability = new HashMap<>();
        availability.put("Single Room", 1);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public void incrementRoom(String roomType) {
        availability.put(roomType,
                availability.getOrDefault(roomType, 0) + 1);
    }

    public void decrementRoom(String roomType) {
        availability.put(roomType,
                availability.getOrDefault(roomType, 0) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {

    private Map<String, Reservation> reservations;

    public BookingHistory() {
        reservations = new HashMap<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public void displayAll() {
        System.out.println("\nBooking History:");
        for (Reservation reservation : reservations.values()) {
            reservation.display();
        }
    }
}

// -------------------- CANCELLATION SERVICE --------------------

class CancellationService {

    private InventoryService inventoryService;
    private BookingHistory bookingHistory;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventoryService,
                               BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
        this.rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {

        System.out.println("\nAttempting cancellation for: " + reservationId);

        Reservation reservation =
                bookingHistory.getReservation(reservationId);

        // Validation
        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        if (!reservation.isActive()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Rollback Process (Controlled Order)

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(reservationId);

        // Step 2: Restore inventory
        inventoryService.incrementRoom(reservation.getRoomType());

        // Step 3: Mark reservation as cancelled
        reservation.cancel();

        System.out.println("Cancellation Successful. Inventory Restored.");
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO Order): " + rollbackStack);
    }
}

// -------------------- APPLICATION ENTRY --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 10.0");
        System.out.println("UC10: Booking Cancellation & Rollback");
        System.out.println("=================================");

        InventoryService inventory = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SI101", "Arun", "Single Room");
        Reservation r2 = new Reservation("SU201", "Meena", "Suite Room");

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);

        // Simulate allocation effect
        inventory.decrementRoom("Single Room");
        inventory.decrementRoom("Suite Room");

        CancellationService cancellationService =
                new CancellationService(inventory, bookingHistory);

        bookingHistory.displayAll();
        inventory.displayInventory();

        // Perform cancellation
        cancellationService.cancelReservation("SI101");

        bookingHistory.displayAll();
        inventory.displayInventory();
        cancellationService.displayRollbackStack();

        System.out.println("\nSystem state restored safely.");
        System.out.println("Application Terminated.");
    }
}