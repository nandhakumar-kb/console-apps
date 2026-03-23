package supermarket.model;

public class Product {
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private int totalSold;

    public Product(String productId, String name, double price, int quantity) {
        this.productId   = productId;
        this.name        = name;
        this.price       = price;
        this.quantity    = quantity;
        this.totalSold   = 0;
    }

    public String getProductId()          { return productId; }
    public String getName()               { return name; }
    public double getPrice()              { return price; }
    public int    getQuantity()           { return quantity; }
    public int    getTotalSold()          { return totalSold; }

    public void setName(String name)      { this.name = name; }
    public void setPrice(double price)    { this.price = price; }
    public void setQuantity(int qty)      { this.quantity = qty; }
    public void addQuantity(int qty)      { this.quantity += qty; }
    public void reduceQuantity(int qty)   { this.quantity -= qty; }
    public void incrementSoldCount(int qty) { this.totalSold += qty; }

    @Override
    public String toString() {
        return String.format("%-12s %-30s Rs%-8.0f Stock: %4d",
                productId, name, price, quantity);
    }
}
