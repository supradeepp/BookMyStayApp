import java.util.*;

// Room class (Domain Model)
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory class (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {

    public static void searchAvailableRooms(Inventory inventory, Map<String, Room> roomMap) {

        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Validation logic (Defensive Programming)
            if (available > 0) {
                Room room = roomMap.get(type);

                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Count: " + available);
                    System.out.println("===========================");
                }
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // Not available
        inventory.addRoom("Suite", 2);

        // Create Room Data
        Map<String, Room> roomMap = new HashMap<>();

        roomMap.put("Single", new Room("Single", 2000,
                Arrays.asList("WiFi", "AC", "TV")));

        roomMap.put("Double", new Room("Double", 3500,
                Arrays.asList("WiFi", "AC", "TV", "Mini Bar")));

        roomMap.put("Suite", new Room("Suite", 6000,
                Arrays.asList("WiFi", "AC", "TV", "Mini Bar", "Jacuzzi")));

        // Guest searches rooms
        SearchService.searchAvailableRooms(inventory, roomMap);
    }
}