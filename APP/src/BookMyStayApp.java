// UseCase3InventorySetup.java

import java.util.HashMap;
import java.util.Map;

// Inventory Class (Centralized State Management)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - Initialize Inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register Room Types with Availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get Availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update Availability (Controlled Method)
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Display Entire Inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 3.0");
        System.out.println("UC3: Centralized Room Inventory");
        System.out.println("=================================\n");

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Display Initial Inventory
        inventory.displayInventory();

        System.out.println("\nUpdating Suite Room availability to 4...\n");

        // Controlled Update
        inventory.updateAvailability("Suite Room", 4);

        // Display Updated Inventory
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}