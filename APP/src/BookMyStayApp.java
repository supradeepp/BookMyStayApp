import java.util.*;

// Reservation Model
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

// Thread-Safe Inventory
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Concurrent Booking Processor
class ConcurrentBookingProcessor implements Runnable {

    private Queue<Reservation> bookingQueue;
    private InventoryService inventoryService;

    public ConcurrentBookingProcessor(Queue<Reservation> bookingQueue,
                                      InventoryService inventoryService) {
        this.bookingQueue = bookingQueue;
        this.inventoryService = inventoryService;
    }

    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            // synchronized block for safe queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.poll();
            }

            if (reservation != null) {
                boolean allocated =
                        inventoryService.allocateRoom(reservation.getRoomType());

                if (allocated) {
                    System.out.println(Thread.currentThread().getName()
                            + " confirmed booking for "
                            + reservation.getGuestName()
                            + " (" + reservation.getRoomType() + ")");
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " FAILED booking for "
                            + reservation.getGuestName()
                            + " (" + reservation.getRoomType() + ")");
                }
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Simulating multiple guests booking simultaneously
        bookingQueue.add(new Reservation("Alice", "Single"));
        bookingQueue.add(new Reservation("Bob", "Single"));
        bookingQueue.add(new Reservation("Charlie", "Single"));
        bookingQueue.add(new Reservation("David", "Double"));
        bookingQueue.add(new Reservation("Eva", "Suite"));
        bookingQueue.add(new Reservation("Frank", "Suite"));

        InventoryService inventoryService = new InventoryService();

        // Creating multiple threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventoryService),
                "Thread-1"
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventoryService),
                "Thread-2"
        );

        Thread t3 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventoryService),
                "Thread-3"
        );

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Inventory State:");
        inventoryService.displayInventory();
    }
}