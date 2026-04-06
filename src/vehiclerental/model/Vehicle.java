package vehiclerental.model;

public class Vehicle {
    private final String id;
    private String name;
    private String numberPlate;
    private VehicleType type;
    private int availableCount;
    private int totalCount;
    private double rentalPricePerDay;
    private int mileageSinceService;
    private boolean serviceDue;
    private int totalRentalCount;

    public Vehicle(String id, String name, String numberPlate, VehicleType type,
            int availableCount, int totalCount, double rentalPricePerDay,
            int mileageSinceService, boolean serviceDue) {
        this.id = id;
        this.name = name;
        this.numberPlate = numberPlate;
        this.type = type;
        this.availableCount = availableCount;
        this.totalCount = totalCount;
        this.rentalPricePerDay = rentalPricePerDay;
        this.mileageSinceService = mileageSinceService;
        this.serviceDue = serviceDue;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public VehicleType getType() {
        return type;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public int getMileageSinceService() {
        return mileageSinceService;
    }

    public boolean isServiceDue() {
        return serviceDue;
    }

    public int getTotalRentalCount() {
        return totalRentalCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setRentalPricePerDay(double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public void setMileageSinceService(int mileageSinceService) {
        this.mileageSinceService = mileageSinceService;
    }

    public void setServiceDue(boolean serviceDue) {
        this.serviceDue = serviceDue;
    }

    public void setTotalRentalCount(int totalRentalCount) {
        this.totalRentalCount = totalRentalCount;
    }

    public void addMileage(int kms) {
        this.mileageSinceService += Math.max(0, kms);
    }

    public void incrementRentalCount() {
        this.totalRentalCount++;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | Avl:%d | Price: Rs%.0f | ServiceDue:%s",
                id, name, numberPlate, type, availableCount, rentalPricePerDay, serviceDue ? "YES" : "NO");
    }
}
