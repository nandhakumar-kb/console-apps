package supermarket.model;

public class Admin extends User {
    private int saleCount;

    public Admin(String email, String password, String name) {
        super(email, password, name, "ADMIN");
        this.saleCount = 0;
    }

    public int getSaleCount()              { return saleCount; }
    public void incrementSaleCount()       { this.saleCount++; }
    public void setSaleCount(int count)    { this.saleCount = count; }
}
