package vehiclerental.menu;

import java.util.List;
import java.util.Scanner;
import vehiclerental.data.DataStore;
import vehiclerental.model.Admin;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;
import vehiclerental.service.ReportService;
import vehiclerental.service.VehicleService;

public class AdminMenu {

    private final Scanner scanner;
    private final Admin admin;
    private final VehicleService vehicleService = new VehicleService();
    private final ReportService reportService = new ReportService();

    public AdminMenu(Scanner scanner, Admin admin) {
        this.scanner = scanner;
        this.admin = admin;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println(" ADMIN MENU | " + admin.getName());
            System.out.println("========================================");
            System.out.println("1. Vehicle Inventory");
            System.out.println("2. Borrower Deposit Management");
            System.out.println("3. Reports");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    inventoryMenu();
                    break;
                case 2:
                    manageBorrowerDeposit();
                    break;
                case 3:
                    reportsMenu();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void inventoryMenu() {
        while (true) {
            System.out.println("\n--- Vehicle Inventory ---");
            System.out.println("1. Add Vehicle (Car/Bike)");
            System.out.println("2. Modify Vehicle");
            System.out.println("3. Delete Vehicle");
            System.out.println("4. View All Vehicles (sorted)");
            System.out.println("5. Search Vehicle");
            System.out.println("6. Mark Vehicle as Serviced");
            System.out.println("7. Back");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    modifyVehicle();
                    break;
                case 3:
                    deleteVehicle();
                    break;
                case 4:
                    viewVehicles();
                    break;
                case 5:
                    searchVehicle();
                    break;
                case 6:
                    markServiced();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addVehicle() {
        System.out.print("Name              : ");
        String name = scanner.nextLine().trim();
        System.out.print("Number Plate      : ");
        String numberPlate = scanner.nextLine().trim();
        System.out.print("Type (1.Car 2.Bike): ");
        int typeChoice = readInt();
        VehicleType type = typeChoice == 1 ? VehicleType.CAR : VehicleType.BIKE;
        System.out.print("Available Count   : ");
        int count = readInt();
        System.out.print("Rental Price/Day  : ");
        double price = readDouble();
        System.out.print("Mileage Since Service (kms): ");
        int kms = readInt();

        System.out.println(vehicleService.addVehicle(name, numberPlate, type, count, price, kms));
    }

    private void modifyVehicle() {
        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine().trim();
        Vehicle vehicle = DataStore.findVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Current: " + vehicle);
        System.out.println("1. Name");
        System.out.println("2. Number Plate");
        System.out.println("3. Available Count");
        System.out.println("4. Total Count");
        System.out.println("5. Rental Price/Day");
        System.out.println("6. Back");
        System.out.print("Choose: ");
        int choice = readInt();

        switch (choice) {
            case 1:
                System.out.print("New Name: ");
                vehicle.setName(scanner.nextLine().trim());
                System.out.println("Updated.");
                break;
            case 2:
                System.out.print("New Number Plate: ");
                vehicle.setNumberPlate(scanner.nextLine().trim());
                System.out.println("Updated.");
                break;
            case 3:
                System.out.print("New Available Count: ");
                vehicle.setAvailableCount(readInt());
                System.out.println("Updated.");
                break;
            case 4:
                System.out.print("New Total Count: ");
                vehicle.setTotalCount(readInt());
                System.out.println("Updated.");
                break;
            case 5:
                System.out.print("New Rental Price/Day: ");
                vehicle.setRentalPricePerDay(readDouble());
                System.out.println("Updated.");
                break;
            default:
                break;
        }
    }

    private void deleteVehicle() {
        System.out.print("Enter Vehicle ID to delete: ");
        String vehicleId = scanner.nextLine().trim();
        System.out.print("Confirm delete (yes/no): ");
        if (!"yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            System.out.println("Cancelled.");
            return;
        }
        boolean deleted = vehicleService.deleteVehicle(vehicleId);
        System.out.println(deleted ? "Vehicle deleted." : "Vehicle not found.");
    }

    private void viewVehicles() {
        System.out.println("Sort by: 1. Name  2. Available Count");
        System.out.print("Choose: ");
        int choice = readInt();
        List<Vehicle> vehicles = choice == 2
                ? vehicleService.getAllSortedByAvailableCount()
                : vehicleService.getAllSortedByName();

        printVehicleTable(vehicles);
    }

    private void searchVehicle() {
        System.out.println("Search by: 1. Name  2. Number Plate");
        System.out.print("Choose: ");
        int choice = readInt();

        if (choice == 1) {
            System.out.print("Enter name: ");
            List<Vehicle> result = vehicleService.searchByName(scanner.nextLine().trim());
            if (result.isEmpty()) {
                System.out.println("No matching vehicles.");
                return;
            }
            printVehicleTable(result);
            return;
        }

        System.out.print("Enter number plate: ");
        Vehicle vehicle = vehicleService.searchByNumberPlate(scanner.nextLine().trim());
        System.out.println(vehicle == null ? "Vehicle not found." : vehicle.toString());
    }

    private void markServiced() {
        System.out.print("Enter Vehicle ID to mark serviced: ");
        boolean ok = vehicleService.markVehicleServiced(scanner.nextLine().trim());
        System.out.println(ok ? "Service status reset; vehicle available in catalog." : "Vehicle not found.");
    }

    private void manageBorrowerDeposit() {
        System.out.print("Borrower Email: ");
        String email = scanner.nextLine().trim();
        System.out.println("1. Add Deposit  2. Deduct Deposit");
        System.out.print("Choose: ");
        int choice = readInt();
        System.out.print("Amount: Rs");
        double amount = readDouble();

        String message = vehicleService.updateBorrowerDeposit(email, amount, choice == 1);
        System.out.println(message);
    }

    private void reportsMenu() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Vehicles Due for Service");
            System.out.println("2. All Vehicles Sorted by Rental Price");
            System.out.println("3. Search by Name and Filter by Type");
            System.out.println("4. Rented Out Vehicles");
            System.out.println("5. Never Rented Vehicles");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    printVehicleTable(reportService.getServiceDueVehicles());
                    break;
                case 2:
                    printVehicleTable(reportService.getVehiclesSortedByRentalPrice());
                    break;
                case 3:
                    reportSearchFilter();
                    break;
                case 4:
                    printVehicleTable(reportService.getRentedOutVehicles());
                    break;
                case 5:
                    printVehicleTable(reportService.getNeverRentedVehicles());
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void reportSearchFilter() {
        System.out.print("Vehicle name contains (blank for all): ");
        String query = scanner.nextLine().trim();
        System.out.println("Type filter: 1.All  2.Car  3.Bike");
        System.out.print("Choose: ");
        int filterChoice = readInt();

        VehicleType type = null;
        if (filterChoice == 2) {
            type = VehicleType.CAR;
        } else if (filterChoice == 3) {
            type = VehicleType.BIKE;
        }

        List<Vehicle> result = reportService.searchAndFilterVehicles(query, type);
        printVehicleTable(result);
    }

    private void printVehicleTable(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println(String.format("\n%-6s %-12s %-13s %-6s %-5s %-5s %-10s %-10s %-8s",
                "ID", "Name", "NumberPlate", "Type", "Avl", "Tot", "Rent/Day", "Mileage", "Service"));
        System.out.println("-".repeat(86));
        for (Vehicle v : vehicles) {
            System.out.println(String.format("%-6s %-12s %-13s %-6s %-5d %-5d %-10.0f %-10d %-8s",
                    v.getId(), v.getName(), v.getNumberPlate(), v.getType(),
                    v.getAvailableCount(), v.getTotalCount(), v.getRentalPricePerDay(),
                    v.getMileageSinceService(), v.isServiceDue() ? "YES" : "NO"));
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
