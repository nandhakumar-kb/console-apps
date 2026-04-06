package vehiclerental.menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import vehiclerental.data.DataStore;
import vehiclerental.model.Borrower;
import vehiclerental.model.DamageLevel;
import vehiclerental.model.FineRecord;
import vehiclerental.model.RentalRecord;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;
import vehiclerental.service.FineService;
import vehiclerental.service.RentalService;
import vehiclerental.service.ReportService;
import vehiclerental.service.VehicleService;

public class BorrowerMenu {

    private final Scanner scanner;
    private final Borrower borrower;
    private final VehicleService vehicleService = new VehicleService();
    private final RentalService rentalService = new RentalService();
    private final FineService fineService = new FineService();
    private final ReportService reportService = new ReportService();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BorrowerMenu(Scanner scanner, Borrower borrower) {
        this.scanner = scanner;
        this.borrower = borrower;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println(" BORROWER MENU | " + borrower.getName());
            System.out.println(" Deposit Balance: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
            System.out.println("========================================");
            System.out.println("1. View Available Vehicles");
            System.out.println("2. Manage Checkout Cart");
            System.out.println("3. Checkout");
            System.out.println("4. My Active Rentals");
            System.out.println("5. Transactions (Extend/Exchange/Lost/Return)");
            System.out.println("6. My Previous Rentals");
            System.out.println("7. My Fine History");
            System.out.println("8. Logout");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    viewCatalog();
                    break;
                case 2:
                    cartMenu();
                    break;
                case 3:
                    System.out.println(rentalService.checkout(borrower));
                    break;
                case 4:
                    printActiveRentals();
                    break;
                case 5:
                    transactionMenu();
                    break;
                case 6:
                    printPreviousRentals();
                    break;
                case 7:
                    printFineHistory();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewCatalog() {
        List<Vehicle> vehicles = vehicleService.getDisplayableCatalog();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles currently available for booking.");
            return;
        }
        printVehicleTable(vehicles);
    }

    private void cartMenu() {
        while (true) {
            System.out.println("\n--- Checkout Cart ---");
            System.out.println("1. Add Vehicle to Cart");
            System.out.println("2. Remove Vehicle from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    addToCart();
                    break;
                case 2:
                    removeFromCart();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addToCart() {
        viewCatalog();
        System.out.println("Select by: 1. Vehicle ID  2. Vehicle Name");
        System.out.print("Choose: ");
        int choice = readInt();
        Vehicle selected = null;

        if (choice == 1) {
            System.out.print("Enter Vehicle ID: ");
            selected = DataStore.findVehicleById(scanner.nextLine().trim());
        } else if (choice == 2) {
            System.out.print("Enter Vehicle Name: ");
            String name = scanner.nextLine().trim().toLowerCase();
            for (Vehicle vehicle : vehicleService.getDisplayableCatalog()) {
                if (vehicle.getName().toLowerCase().contains(name)) {
                    selected = vehicle;
                    break;
                }
            }
        }

        if (selected == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        boolean ok = rentalService.addToCart(borrower, selected.getId());
        if (ok) {
            System.out.println("Added to cart: " + selected.getName());
        } else {
            System.out.println("Cannot add this vehicle. Rule check failed (max one active/cart vehicle per type). ");
        }
    }

    private void removeFromCart() {
        viewCart();
        if (borrower.getCartVehicleIds().isEmpty()) {
            return;
        }
        System.out.print("Enter Vehicle ID to remove: ");
        boolean removed = rentalService.removeFromCart(borrower, scanner.nextLine().trim());
        System.out.println(removed ? "Removed from cart." : "Vehicle not found in cart.");
    }

    private void viewCart() {
        List<Vehicle> cartVehicles = rentalService.getCartVehicles(borrower);
        if (cartVehicles.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        printVehicleTable(cartVehicles);
    }

    private void printActiveRentals() {
        List<RentalRecord> active = rentalService.getActiveRentals(borrower.getEmail());
        if (active.isEmpty()) {
            System.out.println("No active rentals.");
            return;
        }

        System.out.println(String.format("\n%-7s %-12s %-6s %-12s %-12s %-10s",
                "RID", "Vehicle", "Type", "Rent Date", "Due Date", "Ext Count"));
        System.out.println("-".repeat(68));
        for (RentalRecord record : active) {
            System.out.println(String.format("%-7s %-12s %-6s %-12s %-12s %-10d",
                    record.getRentalId(), record.getVehicleName(), record.getVehicleType(),
                    record.getRentDate(), record.getDueDate(), record.getExtensionCount()));
        }
    }

    private void transactionMenu() {
        List<RentalRecord> active = rentalService.getActiveRentals(borrower.getEmail());
        if (active.isEmpty()) {
            System.out.println("No active rentals for transaction.");
            return;
        }

        printActiveRentals();
        System.out.print("Enter Rental ID: ");
        String rentalId = scanner.nextLine().trim();
        RentalRecord record = rentalService.findActiveRentalById(borrower.getEmail(), rentalId);
        if (record == null) {
            System.out.println("Active rental not found.");
            return;
        }

        System.out.println("1. Extend tenure by 1 day");
        System.out.println("2. Return vehicle");
        System.out.println("3. Exchange vehicle");
        System.out.println("4. Mark vehicle as lost");
        System.out.println("5. Back");
        System.out.print("Choose: ");
        int choice = readInt();

        switch (choice) {
            case 1:
                System.out.println(rentalService.extendByOneDay(record));
                break;
            case 2:
                returnVehicleFlow(record);
                break;
            case 3:
                exchangeVehicleFlow(record);
                break;
            case 4:
                markLostFlow(record);
                break;
            default:
                break;
        }
    }

    private void returnVehicleFlow(RentalRecord record) {
        Vehicle vehicle = DataStore.findVehicleById(record.getVehicleId());
        if (vehicle == null) {
            System.out.println("Vehicle data missing.");
            return;
        }

        LocalDate returnDate = readReturnDate();
        System.out.print("Distance run (kms): ");
        int kmsRun = Math.max(0, readInt());

        double totalCharge = fineService.calculateRentalCharge(vehicle, record, returnDate);
        long billableDays = rentalService.getBillableDays(record, returnDate);

        DamageLevel damageLevel = readDamageLevelIfCar(vehicle.getType());
        double lateFine = fineService.calculateLateFine(record, returnDate, totalCharge);
        double extraDistanceFine = fineService.calculateExtraDistanceFine(kmsRun, billableDays, totalCharge);
        double damageFine = fineService.calculateDamageFine(vehicle.getType(), damageLevel, totalCharge);
        double totalFine = lateFine + extraDistanceFine + damageFine;

        System.out.println("Rental charge: Rs" + String.format("%.0f", totalCharge));
        if (totalFine > 0) {
            System.out.println("Fine breakup -> Late: Rs" + String.format("%.0f", lateFine)
                    + " | Distance: Rs" + String.format("%.0f", extraDistanceFine)
                    + " | Damage: Rs" + String.format("%.0f", damageFine));
            payFine(totalFine, "Vehicle return regulations", true);
        }

        rentalService.closeRentalReturned(record, vehicle, returnDate, kmsRun, totalCharge);
        vehicleService.updateServiceDueFlag(vehicle);
        System.out.println("Vehicle returned successfully.");
    }

    private void exchangeVehicleFlow(RentalRecord record) {
        returnVehicleFlow(record);
        System.out.println("Select replacement vehicle by ID from available catalog.");
        viewCatalog();
        System.out.print("Replacement Vehicle ID: ");
        String newVehicleId = scanner.nextLine().trim();
        String message = rentalService.rentVehicleDirect(borrower, newVehicleId);
        System.out.println(message);
    }

    private void markLostFlow(RentalRecord record) {
        Vehicle vehicle = DataStore.findVehicleById(record.getVehicleId());
        if (vehicle == null) {
            System.out.println("Vehicle data missing.");
            return;
        }

        double totalCharge = fineService.calculateRentalCharge(vehicle, record, LocalDate.now());
        double lossFine = fineService.calculateLostVehicleFine(vehicle);

        System.out.println("Rental charge till date: Rs" + String.format("%.0f", totalCharge));
        System.out.println("Loss fine: Rs" + String.format("%.0f", lossFine));
        payFine(lossFine, "Vehicle marked as lost", true);

        rentalService.closeRentalLost(record, totalCharge);
        if (vehicle.getTotalCount() > 0) {
            vehicle.setTotalCount(vehicle.getTotalCount() - 1);
        }
        System.out.println("Vehicle marked as lost and inventory updated.");
    }

    private void printPreviousRentals() {
        List<RentalRecord> previous = reportService.getPreviousRentals(borrower.getEmail());
        if (previous.isEmpty()) {
            System.out.println("No previous rentals.");
            return;
        }

        System.out.println(String.format("\n%-7s %-12s %-6s %-12s %-12s %-10s %-10s",
                "RID", "Vehicle", "Type", "From", "To", "Status", "Charge"));
        System.out.println("-".repeat(80));
        for (RentalRecord record : previous) {
            System.out.println(String.format("%-7s %-12s %-6s %-12s %-12s %-10s %-10.0f",
                    record.getRentalId(), record.getVehicleName(), record.getVehicleType(),
                    record.getRentDate(), record.getReturnDate(),
                    record.isLost() ? "LOST" : "RETURNED", record.getTotalRentalCharge()));
        }
    }

    private void printFineHistory() {
        List<FineRecord> fines = reportService.getFinesByBorrower(borrower.getEmail());
        if (fines.isEmpty()) {
            System.out.println("No fine history.");
            return;
        }

        System.out.println("\n--- Fine History ---");
        for (FineRecord fine : fines) {
            System.out.println(fine);
        }
    }

    private DamageLevel readDamageLevelIfCar(VehicleType type) {
        if (type != VehicleType.CAR) {
            return DamageLevel.NONE;
        }
        System.out.println("Car damage level: 1.NONE 2.LOW 3.MEDIUM 4.HIGH");
        System.out.print("Choose: ");
        int choice = readInt();
        if (choice == 2) {
            return DamageLevel.LOW;
        }
        if (choice == 3) {
            return DamageLevel.MEDIUM;
        }
        if (choice == 4) {
            return DamageLevel.HIGH;
        }
        return DamageLevel.NONE;
    }

    private LocalDate readReturnDate() {
        System.out.print("Return date DD/MM/YYYY (blank for today): ");
        String input = scanner.nextLine().trim();
        if (input.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(input, fmt);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date, using today.");
            return LocalDate.now();
        }
    }

    private void payFine(double amount, String reason, boolean allowDeposit) {
        if (amount <= 0) {
            return;
        }

        System.out.println("Fine to pay for " + reason + ": Rs" + String.format("%.0f", amount));
        if (allowDeposit) {
            System.out.println("Pay mode: 1. Cash  2. Deduct from deposit");
            System.out.print("Choose: ");
            int mode = readInt();
            boolean byCash = mode != 2;
            fineService.applyFine(borrower, reason, amount, byCash);
        } else {
            fineService.applyFine(borrower, reason, amount, true);
        }

        System.out.println("Current deposit: Rs" + String.format("%.0f", borrower.getSecurityDeposit()));
    }

    private void printVehicleTable(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        System.out.println(String.format("\n%-6s %-12s %-13s %-6s %-5s %-10s",
                "ID", "Name", "NumberPlate", "Type", "Avl", "Rent/Day"));
        System.out.println("-".repeat(60));
        for (Vehicle v : vehicles) {
            System.out.println(String.format("%-6s %-12s %-13s %-6s %-5d %-10.0f",
                    v.getId(), v.getName(), v.getNumberPlate(),
                    v.getType(), v.getAvailableCount(), v.getRentalPricePerDay()));
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
