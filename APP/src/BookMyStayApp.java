import java.io.*;
import java.util.*;

// -------------------- Reservation Model --------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// -------------------- System State Wrapper --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory,
                       List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public List<Reservation> getBookingHistory() {
        return bookingHistory;
    }
}

// -------------------- Persistence Service --------------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    public static void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public static SystemState loadState() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state restored successfully.");
            return state;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state. Starting fresh.");
            return null;
        }
    }
}

// -------------------- Main Class --------------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Attempt Recovery
        SystemState restoredState = PersistenceService.loadState();

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        if (restoredState != null) {
            inventory = restoredState.getInventory();
            bookingHistory = restoredState.getBookingHistory();
        } else {
            // Initialize fresh system
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 2);
            inventory.put("Suite", 1);

            bookingHistory = new ArrayList<>();
        }

        // Display current state
        System.out.println("\nCurrent Inventory: " + inventory);
        System.out.println("Current Booking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        // Simulate new booking
        System.out.println("\nAdding new reservation...");
        Reservation newReservation =
                new Reservation(UUID.randomUUID().toString(),
                        "Guest_" + (bookingHistory.size() + 1),
                        "Single");

        bookingHistory.add(newReservation);
        inventory.put("Single", inventory.get("Single") - 1);

        System.out.println("New reservation added: " + newReservation);

        // Save state before shutdown
        SystemState currentState =
                new SystemState(inventory, bookingHistory);

        PersistenceService.saveState(currentState);

        System.out.println("\nApplication shutdown complete.");
    }
}