package westminster_shopping_center;

import westminster_shopping_center.product.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private final List<Map<Product, Double>> products;
    private double total;

    public ShoppingCart() {
        this.total = 0;
        this.products = new ArrayList<>();
    }

    public List<Map<Product, Double>> getProducts() {
        List<Map<Product, Double>> clonedProducts = new ArrayList<>(products.size());

        for (Map<Product, Double> productEntry : products) {
            clonedProducts.add(new HashMap<>(productEntry));
        }

        return clonedProducts;
    }

    public void addProduct(Product product, double quantity) {
        Map<Product, Double> productEntry = new HashMap<>();
        productEntry.put(product, quantity);
        products.add(productEntry);
        total += product.getPrice() * quantity;
    }

    public void removeProduct(Product product) {
        for (Map<Product, Double> productEntry : products) {
            if (productEntry.containsKey(product)) {
                total -= product.getPrice() * productEntry.get(product);
                products.remove(productEntry);
                break;
            }
        }
    }

    public void emptyCart() {
        products.clear();
        total = 0;
    }

    public double getTotal() {
        return total;
    }
}
