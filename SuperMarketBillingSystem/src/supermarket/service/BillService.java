package supermarket.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import supermarket.data.DataStore;
import supermarket.model.*;

public class BillService {

    public Bill createBill(Customer customer, List<CartItem> cartItems, double discountApplied, int pointsEarned) {
        String billNumber = DataStore.generateBillNumber();
        Bill bill = new Bill(billNumber, customer.getEmail(), LocalDate.now());

        double totalAmount = 0.0;
        for (CartItem item : cartItems) {
            BillItem billItem = new BillItem(item.getProductId(), item.getProductName(),
                    item.getPrice(), item.getQuantity());
            bill.addItem(billItem);
            totalAmount += billItem.getTotal();

            Product prod = DataStore.findProductById(item.getProductId());
            if (prod != null) {
                prod.reduceQuantity(item.getQuantity());
                prod.incrementSoldCount(item.getQuantity());
            }
        }

        bill.setTotalAmount(totalAmount);
        bill.setDiscountApplied(discountApplied);
        bill.setPointsEarned(pointsEarned);
        DataStore.bills.add(bill);
        return bill;
    }

    public List<Bill> getBillsForCustomer(String customerEmail) {
        List<Bill> result = new ArrayList<>();
        for (Bill bill : DataStore.bills) {
            if (bill.getCustomerEmail().equalsIgnoreCase(customerEmail)) {
                result.add(bill);
            }
        }
        return result;
    }

    public void printBill(Bill bill) {
        System.out.println("\n" + "=".repeat(75));
        System.out.println(String.format("BILL NUMBER: %-50s DATE: %s", bill.getBillNumber(), bill.getBillDate()));
        System.out.println("=".repeat(75));
        System.out.println(String.format("%-12s %-30s %-10s %-8s %-10s",
                "Product ID", "Product Name", "Price", "Qty", "Total"));
        System.out.println("-".repeat(75));

        double subtotal = 0.0;
        for (BillItem item : bill.getItems()) {
            System.out.println(String.format("%-12s %-30s Rs%-8.0f %-8d Rs%-8.0f",
                    item.getProductId(), item.getProductName(),
                    item.getPrice(), item.getQuantity(), item.getTotal()));
            subtotal += item.getTotal();
        }

        System.out.println("-".repeat(75));
        System.out.println(String.format("%-70s Rs%-8.0f", "SUBTOTAL:", subtotal));

        if (bill.getDiscountApplied() > 0) {
            System.out.println(String.format("%-70s Rs%-8.0f", "DISCOUNT:", bill.getDiscountApplied()));
        }

        System.out.println(String.format("%-70s Rs%-8.0f", "TOTAL AMOUNT:", bill.getTotalAmount()));

        if (bill.getPointsEarned() > 0) {
            System.out.println(String.format("%-70s %-8d", "LOYALTY POINTS EARNED:", bill.getPointsEarned()));
        }

        System.out.println("=".repeat(75));
    }
}
