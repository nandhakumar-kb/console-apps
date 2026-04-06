package vehiclerental.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import vehiclerental.data.DataStore;
import vehiclerental.model.FineRecord;
import vehiclerental.model.RentalRecord;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;

public class ReportService {

    public List<Vehicle> getServiceDueVehicles() {
        return DataStore.vehicles.stream()
                .filter(Vehicle::isServiceDue)
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesSortedByRentalPrice() {
        return DataStore.vehicles.stream()
                .sorted(Comparator.comparingDouble(Vehicle::getRentalPricePerDay)
                        .thenComparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> searchAndFilterVehicles(String nameQuery, VehicleType type) {
        String query = nameQuery == null ? "" : nameQuery.toLowerCase();
        List<Vehicle> result = new ArrayList<>();

        for (Vehicle vehicle : DataStore.vehicles) {
            boolean matchesName = query.isBlank() || vehicle.getName().toLowerCase().contains(query);
            boolean matchesType = type == null || vehicle.getType() == type;
            if (matchesName && matchesType) {
                result.add(vehicle);
            }
        }

        result.sort(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    public List<Vehicle> getRentedOutVehicles() {
        return DataStore.vehicles.stream()
                .filter(v -> v.getAvailableCount() < v.getTotalCount())
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getNeverRentedVehicles() {
        return DataStore.vehicles.stream()
                .filter(v -> v.getTotalRentalCount() == 0)
                .sorted(Comparator.comparing(Vehicle::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<RentalRecord> getPreviousRentals(String borrowerEmail) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : DataStore.rentals) {
            if (record.getBorrowerEmail().equalsIgnoreCase(borrowerEmail) && !record.isActive()) {
                result.add(record);
            }
        }
        return result;
    }

    public List<FineRecord> getFinesByBorrower(String borrowerEmail) {
        List<FineRecord> result = new ArrayList<>();
        for (FineRecord fine : DataStore.fines) {
            if (fine.getBorrowerEmail().equalsIgnoreCase(borrowerEmail)) {
                result.add(fine);
            }
        }
        return result;
    }
}
