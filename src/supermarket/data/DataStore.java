package supermarket.data;

import supermarket.model.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<User>     users    = new ArrayList<>();
    public static List<Product>  products = new ArrayList<>();
    public static List<Bill>     bills    = new ArrayList<>();

    private static int billCounter = 1000;

    static {
        users.add(new Admin("admin@supermarket.com",    "admin123",     "Shop Manager"));
        users.add(new Admin("manager@supermarket.com",  "manager123",   "Cashier Manager"));
        users.add(new Customer("pradeep@customer.com",     "pradeep123",      "Pradeep"));
        users.add(new Customer("sathya@customer.com",    "sathya123",     "Sathya"));
        users.add(new Customer("ragavan@customer.com",     "ragavan123",      "Ragavan"));

        products.add(new Product("P001", "Rice (1kg)",                 180.0, 50));
        products.add(new Product("P002", "Wheat Flour (1kg)",          90.0, 40));
        products.add(new Product("P003", "Milk (1L)",                 60.0, 30));
        products.add(new Product("P004", "Eggs (12pcs)",               120.0, 25));
        products.add(new Product("P005", "Bread (White)",              50.0, 20));
        products.add(new Product("P006", "Butter (500g)",              280.0, 15));
        products.add(new Product("P007", "Oil (1L)",                   200.0, 35));
        products.add(new Product("P008", "Sugar (1kg)",                50.0, 45));
        products.add(new Product("P009", "Salt (500g)",                15.0, 80));
        products.add(new Product("P010", "Tea Powder (500g)",          400.0, 12));
        products.add(new Product("P011", "Coffee (500g)",              500.0, 10));
        products.add(new Product("P012", "Soap (200g)",                40.0, 50));
    }

    public static User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public static Product findProductById(String productId) {
        for (Product prod : products) {
            if (prod.getProductId().equalsIgnoreCase(productId)) {
                return prod;
            }
        }
        return null;
    }

    public static String generateBillNumber() {
        return "BILL" + (billCounter++);
    }
}
