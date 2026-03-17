package supermarket.service;

import supermarket.model.Customer;

public class PaymentService {

    public double calculateDiscount(Customer customer, double cartTotal) {
        double discount = 0.0;

        if (customer.getLoyaltyScore() >= 50) {
            discount = 100.0;
        }

        if (cartTotal >= 5000 && customer.getLoyaltyScore() < 50) {
            discount = 100.0;
        }

        return discount;
    }

    public int calculateLoyaltyPoints(double amount) {
        return (int) (amount / 100);
    }

    public boolean processPayment(Customer customer, double amount) {
        if (customer.getCredit() >= amount) {
            customer.deductCredit(amount);
            return true;
        }
        return false;
    }

    public void applyRewards(Customer customer, double billAmount, boolean loyaltyUsed) {
        if (billAmount >= 5000 && !loyaltyUsed) {
            customer.addCredit(100.0);
        } else if (!loyaltyUsed) {
            int points = calculateLoyaltyPoints(billAmount);
            customer.addLoyaltyPoints(points);
        }
    }

    public double getPayableAmount(double cartTotal, double discount) {
        return cartTotal - discount;
    }
}
