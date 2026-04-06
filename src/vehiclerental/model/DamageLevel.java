package vehiclerental.model;

public enum DamageLevel {
    NONE(0.0),
    LOW(0.20),
    MEDIUM(0.50),
    HIGH(0.75);

    private final double fineMultiplier;

    DamageLevel(double fineMultiplier) {
        this.fineMultiplier = fineMultiplier;
    }

    public double getFineMultiplier() {
        return fineMultiplier;
    }
}
