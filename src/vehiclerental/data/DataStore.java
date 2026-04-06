package vehiclerental.data;

import java.util.ArrayList;
import java.util.List;
import vehiclerental.model.Admin;
import vehiclerental.model.Borrower;
import vehiclerental.model.FineRecord;
import vehiclerental.model.RentalRecord;
import vehiclerental.model.User;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;

public class DataStore {
    public static final List<User> users = new ArrayList<>();
    public static final List<Vehicle> vehicles = new ArrayList<>();
    public static final List<RentalRecord> rentals = new ArrayList<>();
    public static final List<FineRecord> fines = new ArrayList<>();

    private static int vehicleCounter = 101;
    private static int rentalCounter = 1001;
    private static int fineCounter = 501;

    static {
        users.add(new Admin("admin@rental.com", "admin123", "Rental Admin"));
        users.add(new Borrower("kiran@user.com", "kiran123", "Kiran", 30000));
        users.add(new Borrower("sathya@user.com", "sathya123", "Sathya", 30000));

        vehicles.add(new Vehicle("V101", "Swift", "TN10AB1111", VehicleType.CAR, 3, 3, 1800, 800, false));
        vehicles.add(new Vehicle("V102", "Baleno", "TN10AB2222", VehicleType.CAR, 2, 2, 2000, 1200, false));
        vehicles.add(new Vehicle("V103", "City", "TN10AB3333", VehicleType.CAR, 1, 1, 2500, 3100, true));
        vehicles.add(new Vehicle("V104", "Activa", "TN10CD4444", VehicleType.BIKE, 4, 4, 700, 500, false));
        vehicles.add(new Vehicle("V105", "Pulsar", "TN10CD5555", VehicleType.BIKE, 2, 2, 900, 1400, false));
        vehicles.add(new Vehicle("V106", "Apache", "TN10CD6666", VehicleType.BIKE, 1, 1, 1000, 1700, true));
    }

    public static User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public static Vehicle findVehicleById(String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId().equalsIgnoreCase(id)) {
                return vehicle;
            }
        }
        return null;
    }

    public static Vehicle findVehicleByNumberPlate(String numberPlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getNumberPlate().equalsIgnoreCase(numberPlate)) {
                return vehicle;
            }
        }
        return null;
    }

    public static String nextVehicleId() {
        return "V" + (vehicleCounter++);
    }

    public static String nextRentalId() {
        return "R" + (rentalCounter++);
    }

    public static String nextFineId() {
        return "F" + (fineCounter++);
    }
}
