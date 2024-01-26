package westminster_shopping_center.product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Product implements Serializable {
    private static final List<Product> PRODUCTS = new ArrayList<>();
    private final String id;
    private String name;
    private int stock;
    private double price;

    protected Product(String name, int stock, double price){
        this.id = UUID.randomUUID().toString(); // unique identifier generate
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    @Override
    public String toString(){
        return id + " - " + name + " - " + stock + " - " + price;
    }

    public void save() {
        PRODUCTS.add(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public  void addStock(int stock){
        this.stock += stock;
    }

    public void removeStock(int stock){
        if (this.stock < stock){
            throw new IllegalArgumentException("Not enough stock");
        }
        this.stock -= stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static Product getProductById(String id){
        for (Product product : PRODUCTS){
            if (product.getId().equals(id)){
                return product;
            }
        }
        throw new IllegalArgumentException("Product not found");
    }

    public static void removeProduct(Product product){
        PRODUCTS.remove(product);
    }

    public static List<String> listAllProducts(){
        List<String> productNames = new ArrayList<>();
        for (Product product : PRODUCTS){
            productNames.add(product.toString());
        }
        return productNames;
    }

    public static void dump(ObjectOutputStream oos) throws Exception {
        oos.writeObject(PRODUCTS);
    }

    @SuppressWarnings("unchecked")
    public static void load(ObjectInputStream ois) throws Exception {
        ArrayList<Product> newList = (ArrayList<Product>) ois.readObject();
        PRODUCTS.clear();
        PRODUCTS.addAll(newList);
    }
    public static List<Product> getProductsList() {
        return new ArrayList<>(PRODUCTS);
    }
    public String getProductcategory() {
        if(id.toUpperCase().charAt(0)=='E')
            return ("Electronics");
        else if(id.toUpperCase().charAt(0)=='C')
            return ("Clothing");

        else
            return ("Invalid Category");
    }
}
