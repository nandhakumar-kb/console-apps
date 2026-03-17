package supermarket.model;

public class BillItem {
    private String productId;
    private String productName;
    private double price;
    private int quantity;

    public BillItem(String productId, String productName, double price, int quantity) {
        this.productId   = productId;
        this.productName = productName;
        this.price       = price;
        this.quantity    = quantity;
    }

    public String getProductId()        { return productId; }
    public String getProductName()      { return productName; }
    public double getPrice()            { return price; }
    public int    getQuantity()         { return quantity; }
    public double getTotal()            { return price * quantity; }

    @Override
    public String toString() {
        return String.format("%-12s %-30s Rs%-8.0f x%-4d = Rs%-8.0f",
                productId, productName, price, quantity, getTotal());
    }
}
