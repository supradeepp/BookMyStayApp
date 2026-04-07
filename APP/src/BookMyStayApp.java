// UseCase6RoomAllocationService.java

import java.util.*;

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
}

// -------------------- INVENTORY SERVICE --------------------

class InventoryService {

    private HashMap<String, Integer> availability;

    public InventoryService() {
        availability = new HashMap<>();
        availability.put("Single Room", 2);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory State:");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private Queue<Reservation> requestQueue;
    private InventoryService inventoryService;

    // Map room type -> allocated room IDs
    private HashMap<String, Set<String>> allocatedRooms;

    // Global set to prevent duplicate room IDs
    private Set<String> usedRoomIds;

    private int roomIdCounter = 101;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.requestQueue = new LinkedList<>();
        this.allocatedRooms = new HashMap<>();
        this.usedRoomIds = new HashSet<>();
    }

    // Add booking request (FIFO)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    // Process all requests
    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            if (inventoryService.getAvailability(roomType) > 0) {

                String roomId = generateUniqueRoomId(roomType);

                // Record allocation
                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                // Update inventory immediately
                inventoryService.decrementRoom(roomType);

                System.out.println("Booking Confirmed!");
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed - No rooms available for " + roomType);
            }
        }
    }

    // Unique Room ID Generator
    private String generateUniqueRoomId(String roomType) {

        String prefix = roomType.substring(0, 2).toUpperCase();
        String roomId;

        do {
            roomId = prefix + roomIdCounter++;
        } while (usedRoomIds.contains(roomId));

        usedRoomIds.add(roomId);
        return roomId;
    }

    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// -------------------- APPLICATION ENTRY --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 6.0");
        System.out.println("UC6: Reservation Confirmation");
        System.out.println("=================================");

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        bookingService.addRequest(new Reservation("Arun", "Single Room"));
        bookingService.addRequest(new Reservation("Meena", "Single Room"));
        bookingService.addRequest(new Reservation("Rahul", "Single Room")); // Should fail

        bookingService.addRequest(new Reservation("Divya", "Suite Room"));

        // Process all bookings
        bookingService.processBookings();

        bookingService.displayAllocatedRooms();
        inventory.displayInventory();

        System.out.println("\nSystem Consistency Maintained. Application Terminated.");
    }
}