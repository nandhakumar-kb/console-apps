package vehiclerental.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import vehiclerental.data.DataStore;
import vehiclerental.model.Borrower;
import vehiclerental.model.DamageLevel;
import vehiclerental.model.FineRecord;
import vehiclerental.model.RentalRecord;
import vehiclerental.model.Vehicle;
import vehiclerental.model.VehicleType;

public class FineService {

    public double calculateRentalCharge(Vehicle vehicle, RentalRecord record, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(record.getRentDate(), endDate) + 1;
        days = Math.max(days, 1);
        return days * vehicle.getRentalPricePerDay();
    }

    public double calculateLateFine(RentalRecord record, LocalDate returnDate, double totalRentalCharge) {
        if (!returnDate.isAfter(record.getDueDate())) {
            return 0;
        }
        long lateDays = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
        return lateDays * (totalRentalCharge * 0.10);
    }

    public double calculateExtraDistanceFine(int kmsRun, long billableDays, double totalRentalCharge) {
        if (billableDays <= 0) {
            billableDays = 1;
        }
        double perDay = (double) kmsRun / billableDays;
        if (perDay <= 500.0) {
            return 0;
        }
        return totalRentalCharge * 0.15;
    }

    public double calculateDamageFine(VehicleType type, DamageLevel damageLevel, double totalRentalCharge) {
        if (type != VehicleType.CAR || damageLevel == DamageLevel.NONE) {
            return 0;
        }
        return totalRentalCharge * damageLevel.getFineMultiplier();
    }

    public double calculateLostVehicleFine(Vehicle vehicle) {
        return vehicle.getRentalPricePerDay() * 15;
    }

    public void applyFine(Borrower borrower, String reason, double amount, boolean byCash) {
        if (amount <= 0) {
            return;
        }
        if (!byCash) {
            borrower.setSecurityDeposit(borrower.getSecurityDeposit() - amount);
        }
        DataStore.fines.add(new FineRecord(
                DataStore.nextFineId(),
                borrower.getEmail(),
                reason,
                amount,
                byCash ? "CASH" : "DEPOSIT",
                LocalDate.now()));
    }
}
