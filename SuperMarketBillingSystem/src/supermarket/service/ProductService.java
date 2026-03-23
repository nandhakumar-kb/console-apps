package supermarket.service;

import supermarket.data.DataStore;
import supermarket.model.Product;
import supermarket.model.Customer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductService {

    public boolean addProduct(String productId, String name, double price, int quantity) {
        if (DataStore.findProductById(productId) != null) {
            return false;
        }
        DataStore.products.add(new Product(productId, name, price, quantity));
        return true;
    }

    public boolean deleteProduct(String productId) {
        Product prod = DataStore.findProductById(productId);
        if (prod == null) return false;
        DataStore.products.remove(prod);
        return true;
    }

    public List<Product> getAllProductsSortedByName() {
        List<Product> sorted = new ArrayList<>(DataStore.products);
        sorted.sort(Comparator.comparing(Product::getName));
        return sorted;
    }

    public List<Product> getAllProductsSortedByPrice() {
        List<Product> sorted = new ArrayList<>(DataStore.products);
        sorted.sort(Comparator.comparingDouble(Product::getPrice));
        return sorted;
    }

    public List<Product> searchByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product prod : DataStore.products) {
            if (prod.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(prod);
            }
        }
        return result;
    }

    public List<Product> getAvailableProducts() {
        List<Product> result = new ArrayList<>();
        for (Product prod : DataStore.products) {
            if (prod.getQuantity() > 0) {
                result.add(prod);
            }
        }
        return result;
    }
}
