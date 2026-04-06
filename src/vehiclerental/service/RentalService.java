package vehiclerental.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import vehiclerental.data.DataStore;
import vehiclerental.model.Borrower;
import vehiclerental.model.RentalRecord;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;

public class RentalService {

    public boolean addToCart(Borrower borrower, String vehicleId) {
        Vehicle vehicle = DataStore.findVehicleById(vehicleId);
        if (vehicle == null || vehicle.getAvailableCount() <= 0 || vehicle.isServiceDue()) {
            return false;
        }

        if (borrower.getCartVehicleIds().contains(vehicleId)) {
            return false;
        }

        if (hasActiveType(borrower.getEmail(), vehicle.getType()) || hasCartType(borrower, vehicle.getType())) {
            return false;
        }

        borrower.getCartVehicleIds().add(vehicleId);
        return true;
    }

    public boolean removeFromCart(Borrower borrower, String vehicleId) {
        return borrower.getCartVehicleIds().removeIf(id -> id.equalsIgnoreCase(vehicleId));
    }

    public List<Vehicle> getCartVehicles(Borrower borrower) {
        List<Vehicle> result = new ArrayList<>();
        for (String vehicleId : borrower.getCartVehicleIds()) {
            Vehicle vehicle = DataStore.findVehicleById(vehicleId);
            if (vehicle != null) {
                result.add(vehicle);
            }
        }
        return result;
    }

    public String checkout(Borrower borrower) {
        List<Vehicle> selected = getCartVehicles(borrower);
        if (selected.isEmpty()) {
            return "Cart is empty.";
        }

        for (Vehicle vehicle : selected) {
            if (vehicle.getType() == VehicleType.BIKE && borrower.getSecurityDeposit() < 3000) {
                return "Checkout failed. Minimum deposit for BIKE is Rs3000.";
            }
            if (vehicle.getType() == VehicleType.CAR && borrower.getSecurityDeposit() < 10000) {
                return "Checkout failed. Minimum deposit for CAR is Rs10000.";
            }
            if (vehicle.getAvailableCount() <= 0 || vehicle.isServiceDue()) {
                return "Checkout failed. One or more selected vehicles are unavailable or due for service.";
            }
        }

        LocalDate today = LocalDate.now();
        for (Vehicle vehicle : selected) {
            vehicle.setAvailableCount(vehicle.getAvailableCount() - 1);
            vehicle.incrementRentalCount();
            DataStore.rentals.add(new RentalRecord(
                    DataStore.nextRentalId(),
                    borrower.getEmail(),
                    vehicle.getId(),
                    vehicle.getName(),
                    vehicle.getType(),
                    today,
                    today));
        }

        int count = selected.size();
        borrower.getCartVehicleIds().clear();
        return "Checkout successful. " + count + " vehicle(s) rented for today.";
    }

    public String rentVehicleDirect(Borrower borrower, String vehicleId) {
        Vehicle vehicle = DataStore.findVehicleById(vehicleId);
        if (vehicle == null) {
            return "Vehicle not found.";
        }
        if (vehicle.getAvailableCount() <= 0 || vehicle.isServiceDue()) {
            return "Vehicle is unavailable or due for service.";
        }
        if (hasActiveType(borrower.getEmail(), vehicle.getType())) {
            return "You already have an active " + vehicle.getType() + " rental.";
        }
        if (vehicle.getType() == VehicleType.BIKE && borrower.getSecurityDeposit() < 3000) {
            return "Minimum deposit for BIKE is Rs3000.";
        }
        if (vehicle.getType() == VehicleType.CAR && borrower.getSecurityDeposit() < 10000) {
            return "Minimum deposit for CAR is Rs10000.";
        }

        LocalDate today = LocalDate.now();
        vehicle.setAvailableCount(vehicle.getAvailableCount() - 1);
        vehicle.incrementRentalCount();
        DataStore.rentals.add(new RentalRecord(
                DataStore.nextRentalId(),
                borrower.getEmail(),
                vehicle.getId(),
                vehicle.getName(),
                vehicle.getType(),
                today,
                today));
        return "Vehicle rented successfully.";
    }

    public List<RentalRecord> getActiveRentals(String borrowerEmail) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : DataStore.rentals) {
            if (record.getBorrowerEmail().equalsIgnoreCase(borrowerEmail) && record.isActive()) {
                result.add(record);
            }
        }
        return result;
    }

    public List<RentalRecord> getRentalHistory(String borrowerEmail) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : DataStore.rentals) {
            if (record.getBorrowerEmail().equalsIgnoreCase(borrowerEmail) && !record.isActive()) {
                result.add(record);
            }
        }
        return result;
    }

    public RentalRecord findActiveRentalById(String borrowerEmail, String rentalId) {
        for (RentalRecord record : DataStore.rentals) {
            if (record.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)
                    && record.getRentalId().equalsIgnoreCase(rentalId)
                    && record.isActive()) {
                return record;
            }
        }
        return null;
    }

    public boolean hasActiveType(String borrowerEmail, VehicleType type) {
        for (RentalRecord record : DataStore.rentals) {
            if (record.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)
                    && record.isActive()
                    && record.getVehicleType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCartType(Borrower borrower, VehicleType type) {
        for (String vehicleId : borrower.getCartVehicleIds()) {
            Vehicle vehicle = DataStore.findVehicleById(vehicleId);
            if (vehicle != null && vehicle.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public String extendByOneDay(RentalRecord record) {
        if (!record.isActive()) {
            return "Cannot extend closed rental.";
        }
        if (record.getExtensionCount() >= 2) {
            return "Tenure extension limit reached (max two consecutive extensions).";
        }
        LocalDate nextDue = record.getDueDate().plusDays(1);
        record.setDueDate(nextDue);
        record.incrementExtensionCount();
        return "Extended successfully. New due date: " + nextDue;
    }

    public void closeRentalReturned(RentalRecord record, Vehicle vehicle, LocalDate returnDate, int kmsRun,
            double totalCharge) {
        record.setReturnDate(returnDate);
        record.setActive(false);
        record.setKmsRun(Math.max(0, kmsRun));
        record.setTotalRentalCharge(totalCharge);

        vehicle.setAvailableCount(vehicle.getAvailableCount() + 1);
        vehicle.addMileage(kmsRun);
    }

    public void closeRentalLost(RentalRecord record, double totalCharge) {
        record.setReturnDate(LocalDate.now());
        record.setActive(false);
        record.setLost(true);
        record.setTotalRentalCharge(totalCharge);
    }

    public long getBillableDays(RentalRecord record, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(record.getRentDate(), endDate) + 1;
        return Math.max(days, 1);
    }
}
