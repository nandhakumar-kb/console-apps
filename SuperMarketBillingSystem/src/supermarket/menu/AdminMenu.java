package supermarket.menu;

import supermarket.data.DataStore;
import supermarket.model.*;
import supermarket.service.*;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final Scanner       scanner;
    private final Admin         admin;
    private final ProductService productService = new ProductService();
    private final ReportService  reportService  = new ReportService();

    public AdminMenu(Scanner scanner, Admin admin) {
        this.scanner = scanner;
        this.admin   = admin;
    }

    public void show() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  ADMIN MENU  |  " + admin.getName());
            System.out.println("========================================");
            System.out.println("1. Product Management");
            System.out.println("2. User Management");
            System.out.println("3. Reports");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: productManagement(); break;
                case 2: userManagement();    break;
                case 3: reports();          break;
                case 4:
                    System.out.println("Logged out. Goodbye, " + admin.getName() + "!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void productManagement() {
        while (true) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Add Product");
            System.out.println("2. Modify Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Search Product");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: addProduct();    break;
                case 2: modifyProduct(); break;
                case 3: deleteProduct(); break;
                case 4: viewAllProducts(); break;
                case 5: searchProduct(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addProduct() {
        System.out.print("Product ID  : "); String id    = scanner.nextLine().trim();
        System.out.print("Product Name: "); String name  = scanner.nextLine().trim();
        System.out.print("Price (Rs)  : "); double price = readDouble();
        System.out.print("Quantity    : "); int    qty   = readInt();

        if (productService.addProduct(id, name, price, qty)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Product ID already exists.");
        }
    }

    private void modifyProduct() {
        System.out.print("Enter Product ID to modify: ");
        String id = scanner.nextLine().trim();
        Product prod = DataStore.findProductById(id);
        if (prod == null) { System.out.println("Product not found."); return; }

        System.out.println("Current: " + prod);
        System.out.println("Modify: 1.Name  2.Price  3.Quantity  4.Back");
        System.out.print("Choose: ");
        int choice = readInt();
        switch (choice) {
            case 1:
                System.out.print("New Name: ");
                prod.setName(scanner.nextLine().trim());
                System.out.println("Name updated.");
                break;
            case 2:
                System.out.print("New Price (Rs): ");
                prod.setPrice(readDouble());
                System.out.println("Price updated.");
                break;
            case 3:
                System.out.print("New Quantity: ");
                prod.setQuantity(readInt());
                System.out.println("Quantity updated.");
                break;
            case 4: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String id = scanner.nextLine().trim();
        System.out.print("Confirm delete? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            if (productService.deleteProduct(id)) {
                System.out.println("Product deleted.");
            } else {
                System.out.println("Product not found.");
            }
        }
    }

    private void viewAllProducts() {
        System.out.println("Sort by: 1. Name   2. Price");
        System.out.print("Choose: ");
        int choice = readInt();
        List<Product> prods = (choice == 2)
                ? productService.getAllProductsSortedByPrice()
                : productService.getAllProductsSortedByName();
        printProductTable(prods);
    }

    private void searchProduct() {
        System.out.print("Enter product name (partial match ok): ");
        List<Product> prods = productService.searchByName(scanner.nextLine().trim());
        if (prods.isEmpty()) { System.out.println("No products found."); return; }
        printProductTable(prods);
    }

    private void printProductTable(List<Product> prods) {
        System.out.println("\n" + String.format("%-12s %-30s %-10s %-10s",
                "Product ID", "Product Name", "Price", "Stock"));
        System.out.println("-".repeat(65));
        for (Product p : prods) {
            System.out.println(String.format("%-12s %-30s Rs%-8.0f %-10d",
                    p.getProductId(), p.getName(), p.getPrice(), p.getQuantity()));
        }
    }

    private void userManagement() {
        while (true) {
            System.out.println("\n--- User Management ---");
            System.out.println("1. Add Admin");
            System.out.println("2. Add Customer");
            System.out.println("3. View All Users");
            System.out.println("4. Increase Customer Credit");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: addAdmin();          break;
                case 2: addCustomer();       break;
                case 3: viewAllUsers();      break;
                case 4: increaseCredit();    break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addAdmin() {
        System.out.print("Email    : "); String email    = scanner.nextLine().trim();
        if (DataStore.findUserByEmail(email) != null) {
            System.out.println("This email is already registered."); return;
        }
        System.out.print("Password : "); String password = scanner.nextLine().trim();
        System.out.print("Name     : "); String name     = scanner.nextLine().trim();
        DataStore.users.add(new Admin(email, password, name));
        System.out.println("Admin account created for: " + name);
    }

    private void addCustomer() {
        System.out.print("Email    : "); String email    = scanner.nextLine().trim();
        if (DataStore.findUserByEmail(email) != null) {
            System.out.println("This email is already registered."); return;
        }
        System.out.print("Password : "); String password = scanner.nextLine().trim();
        System.out.print("Name     : "); String name     = scanner.nextLine().trim();
        DataStore.users.add(new Customer(email, password, name));
        System.out.println("Customer account created for: " + name + " | Initial credit: Rs1000");
    }

    private void viewAllUsers() {
        System.out.println("\n" + String.format("%-32s %-22s %-10s %-12s",
                "Email", "Name", "Role", "Credit"));
        System.out.println("-".repeat(78));
        for (User u : DataStore.users) {
            String credit = u.getRole().equals("CUSTOMER")
                    ? "Rs" + String.format("%.0f", ((Customer) u).getCredit())
                    : "-";
            System.out.println(String.format("%-32s %-22s %-10s %-12s",
                    u.getEmail(), u.getName(), u.getRole(), credit));
        }
    }

    private void increaseCredit() {
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine().trim();
        User user = DataStore.findUserByEmail(email);
        if (user == null || !user.getRole().equals("CUSTOMER")) {
            System.out.println("Customer not found."); return;
        }
        Customer customer = (Customer) user;
        System.out.println("Current credit: Rs" + String.format("%.0f", customer.getCredit()));
        System.out.print("Amount to add: Rs");
        double amt = readDouble();
        customer.addCredit(amt);
        System.out.println("Updated credit: Rs" + String.format("%.0f", customer.getCredit()));
    }

    private void reports() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Low Stock Products");
            System.out.println("2. Never Sold Products");
            System.out.println("3. Top Customers by Spending");
            System.out.println("4. Top Selling Admins");
            System.out.println("5. Top Sold Products");
            System.out.println("6. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: lowStockReport();       break;
                case 2: neverSoldReport();      break;
                case 3: topCustomersReport();   break;
                case 4: topAdminsReport();      break;
                case 5: topProductsReport();    break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void lowStockReport() {
        System.out.print("Show products with stock <= : ");
        int threshold = readInt();
        List<Product> prods = reportService.getLowStockProducts(threshold);
        if (prods.isEmpty()) { System.out.println("No products below this threshold."); return; }
        System.out.println("\nLow Stock Products:");
        printProductTable(prods);
    }

    private void neverSoldReport() {
        List<Product> prods = reportService.getNeverSoldProducts();
        if (prods.isEmpty()) { System.out.println("All products have been sold."); return; }
        System.out.println("\nNever Sold Products:");
        printProductTable(prods);
    }

    private void topCustomersReport() {
        List<User> customers = reportService.getTopCustomersBySpent();
        System.out.println("\nTop Customers by Spending:");
        System.out.println(String.format("%-32s %-22s %-15s",
                "Email", "Name", "Total Spent"));
        System.out.println("-".repeat(70));
        for (User u : customers) {
            if (u.getRole().equals("CUSTOMER")) {
                Customer c = (Customer) u;
                System.out.println(String.format("%-32s %-22s Rs%-12.0f",
                        u.getEmail(), u.getName(), c.getTotalSpent()));
            }
        }
    }

    private void topAdminsReport() {
        List<User> admins = reportService.getTopAdminsBySales();
        System.out.println("\nTop Admins by Sales:");
        System.out.println(String.format("%-32s %-22s %-12s",
                "Email", "Name", "Sales Count"));
        System.out.println("-".repeat(70));
        for (User u : admins) {
            Admin a = (Admin) u;
            System.out.println(String.format("%-32s %-22s %-12d",
                    u.getEmail(), u.getName(), a.getSaleCount()));
        }
    }

    private void topProductsReport() {
        List<Product> prods = reportService.getTopSoldProducts();
        System.out.println("\nTop Sold Products:");
        System.out.println(String.format("%-12s %-30s %-10s %-12s",
                "Product ID", "Product Name", "Price", "Sold Count"));
        System.out.println("-".repeat(70));
        for (Product p : prods) {
            if (p.getTotalSold() > 0) {
                System.out.println(String.format("%-12s %-30s Rs%-8.0f %-12d",
                        p.getProductId(), p.getName(), p.getPrice(), p.getTotalSold()));
            }
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private double readDouble() {
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (Exception e) { return 0.0; }
    }
}
