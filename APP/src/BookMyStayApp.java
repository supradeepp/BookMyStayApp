// UseCase7AddOnServiceSelection.java

import java.util.*;

// -------------------- ADD-ON SERVICE MODEL --------------------

class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println(serviceName + " - ₹" + price);
    }
}

// -------------------- ADD-ON SERVICE MANAGER --------------------

class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach service to reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);

        System.out.println("Service added to Reservation ID: " + reservationId);
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nAdd-On Services for Reservation ID: " + reservationId);

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    // Calculate total add-on cost
    public double calculateTotalServiceCost(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);
        double total = 0;

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getPrice();
            }
        }

        return total;
    }
}

// -------------------- APPLICATION ENTRY --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        Book My Stay App         ");
        System.out.println("Version: 7.0");
        System.out.println("UC7: Add-On Service Selection");
        System.out.println("=================================");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Example reservation IDs (from UC6 allocation)
        String reservationId1 = "SI101";
        String reservationId2 = "SU201";

        // Guest selects services
        serviceManager.addService(reservationId1, new AddOnService("Breakfast", 500));
        serviceManager.addService(reservationId1, new AddOnService("Airport Pickup", 1200));

        serviceManager.addService(reservationId2, new AddOnService("Extra Bed", 800));

        // Display services
        serviceManager.displayServices(reservationId1);
        double total1 = serviceManager.calculateTotalServiceCost(reservationId1);
        System.out.println("Total Add-On Cost: ₹" + total1);

        serviceManager.displayServices(reservationId2);
        double total2 = serviceManager.calculateTotalServiceCost(reservationId2);
        System.out.println("Total Add-On Cost: ₹" + total2);

        System.out.println("\nCore booking and inventory remain unchanged.");
        System.out.println("Application Terminated.");
    }
}