package supermarket.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private String billNumber;
    private String customerEmail;
    private LocalDate billDate;
    private List<BillItem> items;
    private double totalAmount;
    private double discountApplied;
    private int pointsEarned;

    public Bill(String billNumber, String customerEmail, LocalDate billDate) {
        this.billNumber     = billNumber;
        this.customerEmail  = customerEmail;
        this.billDate       = billDate;
        this.items          = new ArrayList<>();
        this.totalAmount    = 0.0;
        this.discountApplied = 0.0;
        this.pointsEarned   = 0;
    }

    public String getBillNumber()             { return billNumber; }
    public String getCustomerEmail()          { return customerEmail; }
    public LocalDate getBillDate()            { return billDate; }
    public List<BillItem> getItems()          { return items; }
    public double getTotalAmount()            { return totalAmount; }
    public double getDiscountApplied()        { return discountApplied; }
    public int getPointsEarned()              { return pointsEarned; }

    public void setTotalAmount(double amt)    { this.totalAmount = amt; }
    public void setDiscountApplied(double d)  { this.discountApplied = d; }
    public void setPointsEarned(int pts)      { this.pointsEarned = pts; }

    public void addItem(BillItem item)        { this.items.add(item); }
}
