package supermarket.model;

public class Customer extends User {
    private double credit;
    private int loyaltyScore;
    private double totalSpent;

    public Customer(String email, String password, String name) {
        super(email, password, name, "CUSTOMER");
        this.credit       = 1000.0;
        this.loyaltyScore = 0;
        this.totalSpent   = 0.0;
    }

    public double getCredit()              { return credit; }
    public void   setCredit(double amount) { this.credit = amount; }

    public int    getLoyaltyScore()        { return loyaltyScore; }
    public void   setLoyaltyScore(int pts) { this.loyaltyScore = pts; }

    public double getTotalSpent()          { return totalSpent; }
    public void   setTotalSpent(double amt) { this.totalSpent = amt; }

    public void addCredit(double amount)   { this.credit += amount; }
    public void deductCredit(double amount){ this.credit -= amount; }
    public void addLoyaltyPoints(int pts) { this.loyaltyScore += pts; }
}
