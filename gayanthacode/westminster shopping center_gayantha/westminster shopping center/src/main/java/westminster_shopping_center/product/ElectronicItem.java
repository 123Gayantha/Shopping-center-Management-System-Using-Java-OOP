package westminster_shopping_center.product;

import java.time.LocalDate;

public class ElectronicItem extends Product{
    private final String brand;
    private final LocalDate warranty;

    public ElectronicItem(String name, int stock, double price, String brand, LocalDate warranty) {
        super(name, stock, price);
        this.brand = brand;
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return "Electronic: " + super.toString() + " - " + brand + " - " + warranty;
    }

    public String getBrand() {
        return brand;
    }

    public LocalDate getWarranty() {
        return warranty;
    }
}
