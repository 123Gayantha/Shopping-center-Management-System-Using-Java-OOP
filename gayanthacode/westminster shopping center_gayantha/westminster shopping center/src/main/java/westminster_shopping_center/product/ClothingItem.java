package westminster_shopping_center.product;

public class ClothingItem extends Product {
    private final double size;
    private final String color;

    public ClothingItem(String name, int stock, double price, double size, String color) {
        super(name, stock, price);
        this.size = size;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Clothing: " + super.toString() + " - " + size + " - " + color;
    }

    public double getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }
}
