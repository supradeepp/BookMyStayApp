// UseCase5BookingRequestQueue.java

import java.util.LinkedList;
import java.util.Queue;

// -------------------- RESERVATION MODEL --------------------

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// -------------------- BOOKING REQUEST QUEUE --------------------

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (FIFO order preserved automatically)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Display all pending requests
    public void displayPendingRequests() {
        System.out.println("\nPending Booking Requests (FIFO Order):");

        for (Reservation reservation : requestQueue) {
            reservation.displayReservation();
        }
    }

    // Peek next request (without removing)
    public void viewNextRequest() {
        Reservation next = requestQueue.peek();
        if (next != null) {
            System.out.println("\nNext Request to be processed:");
            next.displayReservation();
        } else {
            System.out.println("\nNo pending requests.");
        }
    }
}

// -------------------- APPLICATION ENTRY --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 5.0");
        System.out.println("UC5: Booking Request Queue (FIFO)");
        System.out.println("=================================\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Arun", "Single Room"));
        bookingQueue.addRequest(new Reservation("Meena", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double Room"));

        // Display queue
        bookingQueue.displayPendingRequests();

        // Show next request (without removing)
        bookingQueue.viewNextRequest();

        System.out.println("\nNo inventory changes performed.");
        System.out.println("Application Terminated.");
    }
}