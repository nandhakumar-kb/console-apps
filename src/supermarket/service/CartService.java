package supermarket.service;

import java.util.List;
import supermarket.data.DataStore;
import supermarket.model.*;

public class CartService {

    public boolean addToCart(List<CartItem> cart, String productId, int quantity) {
        Product prod = DataStore.findProductById(productId);
        if (prod == null || prod.getQuantity() < quantity) {
            return false;
        }

        for (CartItem item : cart) {
            if (item.getProductId().equalsIgnoreCase(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }

        cart.add(new CartItem(prod.getProductId(), prod.getName(), prod.getPrice(), quantity));
        return true;
    }

    public boolean removeFromCart(List<CartItem> cart, String productId) {
        for (CartItem item : cart) {
            if (item.getProductId().equalsIgnoreCase(productId)) {
                cart.remove(item);
                return true;
            }
        }
        return false;
    }

    public boolean updateQuantityInCart(List<CartItem> cart, String productId, int newQuantity) {
        for (CartItem item : cart) {
            if (item.getProductId().equalsIgnoreCase(productId)) {
                if (newQuantity <= 0) {
                    cart.remove(item);
                } else {
                    Product prod = DataStore.findProductById(productId);
                    if (prod != null && prod.getQuantity() >= newQuantity) {
                        item.setQuantity(newQuantity);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public double getCartTotal(List<CartItem> cart) {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getTotal();
        }
        return total;
    }

    public void printCart(List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n" + String.format("%-12s %-30s %-10s %-8s %-10s",
                "Product ID", "Product Name", "Price", "Qty", "Total"));
        System.out.println("-".repeat(75));
        for (CartItem item : cart) {
            System.out.println(String.format("%-12s %-30s Rs%-8.0f %-8d Rs%-8.0f",
                    item.getProductId(), item.getProductName(),
                    item.getPrice(), item.getQuantity(), item.getTotal()));
        }
        System.out.println("-".repeat(75));
        System.out.println(String.format("%-12s %-30s %-10s %-8s Rs%-8.0f",
                "", "", "", "TOTAL", getCartTotal(cart)));
    }
}
