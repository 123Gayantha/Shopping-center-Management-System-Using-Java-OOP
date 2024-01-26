import input.ConsoleInput;
import westminster_shopping_center.product.ClothingItem;
import westminster_shopping_center.product.ElectronicItem;
import westminster_shopping_center.product.Product;
import westminster_shopping_center.user.BaseUser;
import westminster_shopping_center.user.WestminsterShoppingManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final String SAVE_FILE_NAME = "westminster_shopping_center.dat";
    static Product[] productlist;

    public static void main(String[] args) {
        System.out.println("Welcome to Westminster Shopping Center!");
        // creating an input stream
        ConsoleInput<String> stringInput = new ConsoleInput<>((input) -> input);
        ConsoleInput<Integer> intInput = new ConsoleInput<>(Integer::parseInt);

        // Resuming from save file
        resume();

        try {
            new WestminsterShoppingManager("admin", "admin").save();
        }
        catch (Exception ignored) {}

        System.out.println("Please sign in to continue");

        // Authenticating manager
        String username = stringInput.readUserInput("Username: ");
        String password = stringInput.readUserInput("Password: ");
        try {
            WestminsterShoppingManager.signIn(username, password);
        }
        catch (Exception e) {
            System.out.println("Invalid username or password");
            System.exit(-1);
        }

        System.out.println("Welcome " + username);

        while (true) {
            System.out.println("Please select an option");
            System.out.println("1. Add a new product");
            System.out.println("2. Remove a product");
            System.out.println("3. View all products");
            System.out.println("4. Save");
            System.out.println("5. Exit");

            Integer answer = intInput.readUserInput(
                    "Enter your choice: ",
                    (input) -> List.of(1, 2, 3, 4, 5).contains(input),
                    "Only 1, 2, 3, 4, 5 are allowed"
            );

            switch (answer) {
                case 1: {
                    addNewProduct();
                    break;
                }
                case 2: {
                    removeProduct();
                    break;
                }
                case 3: {
                    viewAllProducts();
                    break;
                }
                case 4: {
                    save();
                    break;
                }
                case 5: {
                    System.exit(0);
                }
            }
        }
    }

    private static void addNewProduct() {
        ConsoleInput<String> stringInput = new ConsoleInput<>((input) -> input);
        ConsoleInput<Double> doubleInput = new ConsoleInput<>(Double::parseDouble);
        ConsoleInput<Integer> integerInput = new ConsoleInput<>(Integer::parseInt);
        ConsoleInput<LocalDate> dateInput = new ConsoleInput<>(LocalDate::parse);

        System.out.println("Please select a product type");
        System.out.println("1. Clothing");
        System.out.println("2. Electronics");
        Integer type = integerInput.readUserInput(
                "Your choice: ",
                (input) -> List.of(1, 2).contains(input),
                "Only 1, 2 are allowed"
        );

        switch (type) {
            case 1: {
                new ClothingItem(
                        stringInput.readUserInput("Enter product name: "),
                        integerInput.readUserInput("Enter product quantity: "),
                        doubleInput.readUserInput("Enter product price: "),
                        doubleInput.readUserInput("Enter product size: "),
                        stringInput.readUserInput("Enter product color: ")
                ).save();
                break;
            }
            case 2: {
                new ElectronicItem(
                        stringInput.readUserInput("Enter product name: "),
                        integerInput.readUserInput("Enter product quantity: "),
                        doubleInput.readUserInput("Enter product price: "),
                        stringInput.readUserInput("Enter product brand: "),
                        dateInput.readUserInput("Enter product warranty: ")
                ).save();
                break;
            }
        }

        System.out.println("Product added successfully");
    }

    private static void removeProduct() {
        ConsoleInput<String> stringInput = new ConsoleInput<>((input) -> input);

        System.out.println("Please enter the product id");
        String id = stringInput.readUserInput("Product id: ");

        try {
            Product.removeProduct(Product.getProductById(id));
            System.out.println("Product removed successfully");
        }
        catch (Exception e) {
            System.out.println("Product not found");
        }
    }

    private static void viewAllProducts() {
        for (String product : Product.listAllProducts()) {
            System.out.println(product);
        }
    }

    private static void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_NAME))) {
            Product.dump(oos);
            BaseUser.dump(oos);

            System.out.println("Saved successfully");
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private static void resume() {
        try (
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME))) {
            Product.load(ois);
            BaseUser.load(ois);

            System.out.println("Resumed successfully");
        }
        catch (Exception ignored) {}
    }
    public  Product[] getProductlist() {
        return productlist;
    }

    public Product getProductById(String productId) {
        for (Product product : productlist) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}
