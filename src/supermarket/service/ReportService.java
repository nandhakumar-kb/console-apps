package supermarket.service;

import java.util.ArrayList;
import java.util.List;
import supermarket.data.DataStore;
import supermarket.model.*;

public class ReportService {

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> result = new ArrayList<>();
        for (Product prod : DataStore.products) {
            if (prod.getQuantity() <= threshold) {
                result.add(prod);
            }
        }
        return result;
    }

    public List<Product> getNeverSoldProducts() {
        List<Product> result = new ArrayList<>();
        for (Product prod : DataStore.products) {
            if (prod.getTotalSold() == 0) {
                result.add(prod);
            }
        }
        return result;
    }

    public List<User> getTopCustomersBySpent() {
        List<User> customers = new ArrayList<>();
        for (User user : DataStore.users) {
            if (user.getRole().equals("CUSTOMER")) {
                customers.add(user);
            }
        }
        customers.sort((a, b) -> Double.compare(
                ((Customer) b).getTotalSpent(),
                ((Customer) a).getTotalSpent()));
        return customers;
    }

    public List<User> getTopAdminsBySales() {
        List<User> admins = new ArrayList<>();
        for (User user : DataStore.users) {
            if (user.getRole().equals("ADMIN")) {
                admins.add(user);
            }
        }
        admins.sort((a, b) -> Integer.compare(
                ((Admin) b).getSaleCount(),
                ((Admin) a).getSaleCount()));
        return admins;
    }

    public List<Product> getTopSoldProducts() {
        List<Product> sorted = new ArrayList<>(DataStore.products);
        sorted.sort((a, b) -> Integer.compare(b.getTotalSold(), a.getTotalSold()));
        return sorted;
    }
}
