package vehiclerental.model;

import java.time.LocalDate;

public class RentalRecord {
    private final String rentalId;
    private final String borrowerEmail;
    private final String vehicleId;
    private final String vehicleName;
    private final VehicleType vehicleType;
    private final LocalDate rentDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean active;
    private boolean lost;
    private int extensionCount;
    private int kmsRun;
    private double totalRentalCharge;

    public RentalRecord(String rentalId, String borrowerEmail, String vehicleId,
            String vehicleName, VehicleType vehicleType, LocalDate rentDate,
            LocalDate dueDate) {
        this.rentalId = rentalId;
        this.borrowerEmail = borrowerEmail;
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
        this.active = true;
    }

    public String getRentalId() {
        return rentalId;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isLost() {
        return lost;
    }

    public int getExtensionCount() {
        return extensionCount;
    }

    public int getKmsRun() {
        return kmsRun;
    }

    public double getTotalRentalCharge() {
        return totalRentalCharge;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public void incrementExtensionCount() {
        this.extensionCount++;
    }

    public void setKmsRun(int kmsRun) {
        this.kmsRun = kmsRun;
    }

    public void setTotalRentalCharge(double totalRentalCharge) {
        this.totalRentalCharge = totalRentalCharge;
    }
}
