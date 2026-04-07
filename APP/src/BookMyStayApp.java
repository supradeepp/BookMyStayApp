// UseCase2RoomInitialization.java

abstract class Room {

    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price Per Night: ₹" + pricePerNight);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1500.0);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2500.0);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000.0);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("      Book My Stay App           ");
        System.out.println("Version: 2.0");
        System.out.println("UC2: Basic Room Initialization");
        System.out.println("=================================\n");

        // Creating Room Objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display Details
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailability);
        System.out.println("---------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailability);
        System.out.println("---------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailability);
        System.out.println("---------------------------------");

        System.out.println("Application Terminated.");
    }
}