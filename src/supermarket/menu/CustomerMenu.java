package supermarket.menu;

import supermarket.data.DataStore;
import supermarket.model.*;
import supermarket.service.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private final Scanner       scanner;
    private final Customer      customer;
    private final ProductService productService = new ProductService();
    private final CartService   cartService     = new CartService();
    private final PaymentService paymentService = new PaymentService();
    private final BillService   billService     = new BillService();
    private final List<CartItem> cart           = new ArrayList<>();

    public CustomerMenu(Scanner scanner, Customer customer) {
        this.scanner   = scanner;
        this.customer  = customer;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  CUSTOMER MENU  |  " + customer.getName());
            System.out.println("  Credit: Rs" + String.format("%.0f", customer.getCredit()) +
                    " | Loyalty Points: " + customer.getLoyaltyScore());
            System.out.println("========================================");
            System.out.println("1. Shop & Browse Products");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Purchase History");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: shop();          break;
                case 2: viewCart();      break;
                case 3: checkout();      break;
                case 4: purchaseHistory(); break;
                case 5:
                    System.out.println("Logged out. Goodbye, " + customer.getName() + "!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void shop() {
        while (true) {
            System.out.println("\n--- Shopping ---");
            List<Product> available = productService.getAvailableProducts();
            if (available.isEmpty()) { System.out.println("No products available."); return; }

            System.out.println("Sort by: 1. Name   2. Price");
            System.out.print("Choose: ");
            int choice = readInt();
            List<Product> prods = (choice == 2)
                    ? productService.getAllProductsSortedByPrice()
                    : productService.getAllProductsSortedByName();

            System.out.println("\n" + String.format("%-12s %-30s %-10s %-10s",
                    "Product ID", "Product Name", "Price", "Stock"));
            System.out.println("-".repeat(65));
            boolean anyAvailable = false;
            for (Product p : prods) {
                if (p.getQuantity() > 0) {
                    System.out.println(String.format("%-12s %-30s Rs%-8.0f %-10d",
                            p.getProductId(), p.getName(), p.getPrice(), p.getQuantity()));
                    anyAvailable = true;
                }
            }
            if (!anyAvailable) { System.out.println("No products available."); return; }

            System.out.println("\n1. Add to Cart  2. View Cart  3. Back");
            System.out.print("Choose: ");
            int op = readInt();

            if (op == 1) {
                addToCartInteractive();
            } else if (op == 2) {
                viewCart();
            } else if (op == 3) {
                return;
            }
        }
    }

    private void addToCartInteractive() {
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim();
        Product prod = DataStore.findProductById(productId);
        if (prod == null) { System.out.println("Product not found."); return; }
        if (prod.getQuantity() <= 0) { System.out.println("Product out of stock."); return; }

        System.out.print("Enter Quantity: ");
        int qty = readInt();
        if (qty <= 0 || qty > prod.getQuantity()) { System.out.println("Invalid quantity."); return; }

        if (cartService.addToCart(cart, productId, qty)) {
            System.out.println(qty + " x " + prod.getName() + " added to cart.");
        } else {
            System.out.println("Could not add to cart.");
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }

        cartService.printCart(cart);

        System.out.println("\n1. Edit Quantity  2. Remove Item  3. Back");
        System.out.print("Choose: ");
        int choice = readInt();

        if (choice == 1) {
            System.out.print("Enter Product ID to edit: ");
            String productId = scanner.nextLine().trim();
            System.out.print("New quantity: ");
            int newQty = readInt();
            if (cartService.updateQuantityInCart(cart, productId, newQty)) {
                System.out.println("Quantity updated.");
            } else {
                System.out.println("Could not update.");
            }
        } else if (choice == 2) {
            System.out.print("Enter Product ID to remove: ");
            if (cartService.removeFromCart(cart, scanner.nextLine().trim())) {
                System.out.println("Item removed.");
            } else {
                System.out.println("Item not found in cart.");
            }
        }
    }

    private void checkout() {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }

        double cartTotal = cartService.getCartTotal(cart);

        System.out.println("\n--- Checkout ---");
        System.out.println("Cart Total: Rs" + String.format("%.0f", cartTotal));
        System.out.println("Your Credit: Rs" + String.format("%.0f", customer.getCredit()));

        if (customer.getCredit() < cartTotal) {
            System.out.println("Insufficient credit. Cannot checkout.");
            return;
        }

        double discount = paymentService.calculateDiscount(customer, cartTotal);
        boolean loyaltyUsed = false;

        if (discount > 0 && customer.getLoyaltyScore() >= 50) {
            System.out.println("\nYou have loyalty points. Use 100 Rs discount from loyalty? (yes/no)");
            System.out.print("Choose: ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                loyaltyUsed = true;
                customer.setLoyaltyScore(customer.getLoyaltyScore() - 50);
            }
        }

        if (loyaltyUsed) {
            discount = 100.0;
        } else if (cartTotal >= 5000) {
            System.out.println("Bill >= Rs5000. You get Rs100 cashback to your wallet.");
            discount = 100.0;
        } else {
            discount = 0.0;
        }

        double payableAmount = paymentService.getPayableAmount(cartTotal, discount);

        System.out.println("\nSubtotal: Rs" + String.format("%.0f", cartTotal));
        if (discount > 0) {
            System.out.println("Discount: Rs" + String.format("%.0f", discount));
        }
        System.out.println("Payable Amount: Rs" + String.format("%.0f", payableAmount));

        System.out.print("\nConfirm payment? (yes/no): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.println("Checkout cancelled.");
            return;
        }

        if (!paymentService.processPayment(customer, payableAmount)) {
            System.out.println("Payment failed. Insufficient credit.");
            return;
        }

        int pointsEarned = 0;
        if (loyaltyUsed) {
            pointsEarned = 0;
        } else if (cartTotal >= 5000) {
            customer.addCredit(100.0);
            pointsEarned = 0;
        } else {
            pointsEarned = paymentService.calculateLoyaltyPoints(cartTotal);
            customer.addLoyaltyPoints(pointsEarned);
        }

        customer.setTotalSpent(customer.getTotalSpent() + cartTotal);

        Bill bill = billService.createBill(customer, cart, discount, pointsEarned);
        billService.printBill(bill);

        System.out.println("\nPayment successful! Thank you for shopping.");
        System.out.println("Updated Credit: Rs" + String.format("%.0f", customer.getCredit()));
        System.out.println("Updated Loyalty Points: " + customer.getLoyaltyScore());

        cart.clear();
    }

    private void purchaseHistory() {
        List<Bill> bills = billService.getBillsForCustomer(customer.getEmail());
        if (bills.isEmpty()) { System.out.println("No purchase history yet."); return; }

        System.out.println("\n--- Purchase History ---");
        System.out.println(String.format("%-15s %-15s %-15s",
                "Bill Number", "Date", "Amount"));
        System.out.println("-".repeat(50));
        for (Bill bill : bills) {
            System.out.println(String.format("%-15s %-15s Rs%-12.0f",
                    bill.getBillNumber(), bill.getBillDate(), bill.getTotalAmount()));
        }

        System.out.print("\nEnter Bill Number to view details (or press Enter to go back): ");
        String billNo = scanner.nextLine().trim();
        if (!billNo.isEmpty()) {
            for (Bill bill : bills) {
                if (bill.getBillNumber().equalsIgnoreCase(billNo)) {
                    billService.printBill(bill);
                    return;
                }
            }
            System.out.println("Bill not found.");
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
}
