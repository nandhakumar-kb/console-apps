package vehiclerental.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import vehiclerental.data.DataStore;
import vehiclerental.model.Borrower;
import vehiclerental.model.User;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;

public class VehicleService {

    public String addVehicle(String name, String numberPlate, VehicleType type,
            int count, double rentalPricePerDay, int mileageSinceService) {
        if (DataStore.findVehicleByNumberPlate(numberPlate) != null) {
            return "Vehicle with this number plate already exists.";
        }
        boolean serviceDue = mileageSinceService >= getServiceThreshold(type);
        Vehicle vehicle = new Vehicle(DataStore.nextVehicleId(), name, numberPlate, type,
                count, count, rentalPricePerDay, mileageSinceService, serviceDue);
        DataStore.vehicles.add(vehicle);
        return "Vehicle added successfully with ID: " + vehicle.getId();
    }

    public boolean deleteVehicle(String vehicleId) {
        Vehicle vehicle = DataStore.findVehicleById(vehicleId);
        if (vehicle == null) {
            return false;
        }
        return DataStore.vehicles.remove(vehicle);
    }

    public List<Vehicle> getAllSortedByName() {
        return DataStore.vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getAllSortedByAvailableCount() {
        return DataStore.vehicles.stream()
                .sorted(Comparator.comparingInt(Vehicle::getAvailableCount).reversed()
                        .thenComparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> searchByName(String namePart) {
        String query = namePart.toLowerCase();
        return DataStore.vehicles.stream()
                .filter(v -> v.getName().toLowerCase().contains(query))
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Vehicle searchByNumberPlate(String numberPlate) {
        return DataStore.findVehicleByNumberPlate(numberPlate);
    }

    public List<Vehicle> getDisplayableCatalog() {
        return DataStore.vehicles.stream()
                .filter(v -> v.getAvailableCount() > 0)
                .filter(v -> !v.isServiceDue())
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> filterByType(List<Vehicle> vehicles, VehicleType type) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getType() == type) {
                result.add(vehicle);
            }
        }
        return result;
    }

    public int getServiceThreshold(VehicleType type) {
        return type == VehicleType.CAR ? 3000 : 1500;
    }

    public void updateServiceDueFlag(Vehicle vehicle) {
        int threshold = getServiceThreshold(vehicle.getType());
        vehicle.setServiceDue(vehicle.getMileageSinceService() >= threshold);
    }

    public boolean markVehicleServiced(String vehicleId) {
        Vehicle vehicle = DataStore.findVehicleById(vehicleId);
        if (vehicle == null) {
            return false;
        }
        vehicle.setMileageSinceService(0);
        vehicle.setServiceDue(false);
        return true;
    }

    public String updateBorrowerDeposit(String borrowerEmail, double amount, boolean add) {
        User user = DataStore.findUserByEmail(borrowerEmail);
        if (!(user instanceof Borrower)) {
            return "Borrower not found.";
        }

        Borrower borrower = (Borrower) user;
        double updated = add ? borrower.getSecurityDeposit() + amount
                : borrower.getSecurityDeposit() - amount;
        borrower.setSecurityDeposit(updated);
        return "Borrower deposit updated. Current deposit: Rs" + String.format("%.0f", updated);
    }
}
