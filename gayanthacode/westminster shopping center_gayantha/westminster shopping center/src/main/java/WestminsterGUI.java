import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import westminster_shopping_center.ShoppingCart;
import westminster_shopping_center.product.Product;
import westminster_shopping_center.product.ClothingItem;
import westminster_shopping_center.product.ElectronicItem;
import java.util.List;


public class WestminsterGUI extends JFrame {
    private JFrame frame;
    private JTable productTable;
    private static JComboBox<String> categoryComboBox;
    private static DefaultListModel<String> shoppingCartModel;
    private static DefaultTableModel cartTableModel;
    private static JScrollPane cartScrollPane;
    private static JPanel centerPanel;
    private static JTextArea productDetailsTextArea;
    private static JButton addToCartButton;

    private static Main products;
    private static ShoppingCart cartItems = new ShoppingCart();

    public WestminsterGUI() {
        products = new Main(); // Initialize the 'products' object

        frame = new JFrame("Westminster Shopping Center");
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        createProductDisplayPanel();
        createShoppingCartPanel();

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void createProductDisplayPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        categoryComboBox = new JComboBox<>(new String[]{"All", "Clothing", "Electronics"});
        topPanel.add(new JLabel("Select product category "));
        topPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        topPanel.add(categoryComboBox);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(Box.createRigidArea(new Dimension(310, 0)));

        JButton viewShoppingCartButton = new JButton("Shopping Cart");
        viewShoppingCartButton.addActionListener(e -> viewShoppingCart());
        topPanel.add(viewShoppingCartButton);

        frame.add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Price($)");
        model.addColumn("Info");
        if (products.getProductlist() != null) {
            for (Product product : products.getProductlist()) {
                model.addRow(new Object[]{product.getId(), product.getName(), product.getProductcategory(), product.getPrice()});
            }
        } else {
            System.out.println("No products found");
        }

        productTable = new JTable(model);
        JScrollPane productScrollPane = new JScrollPane(productTable);

        productDetailsTextArea = new JTextArea();
        productDetailsTextArea.setEditable(false);
        JScrollPane productDetailsScrollPane = new JScrollPane(productDetailsTextArea);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(this::addToShoppingCart);

        centerPanel.add(productScrollPane, BorderLayout.CENTER);
        centerPanel.add(productDetailsScrollPane, BorderLayout.SOUTH);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel addToCartPanel = new JPanel(new BorderLayout());
        addToCartPanel.add(addToCartButton);
        bottomPanel.add(addToCartPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !productTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = productTable.getSelectedRow();
                displayAdditionalInfo(getProductFromSelectedRow(selectedRow));
            }
        });

        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            updateProductTable(selectedCategory);
        });
    }

    private void createShoppingCartPanel() {
        shoppingCartModel = new DefaultListModel<>();
        JList<String> shoppingCartList = new JList<>(shoppingCartModel);
        JScrollPane shoppingCartScrollPane = new JScrollPane(shoppingCartList);
        cartTableModel = new DefaultTableModel();
        cartTableModel.addColumn("Product");
        cartTableModel.addColumn("Quantity");
        cartTableModel.addColumn("Price");

        JTable cartTable = new JTable(cartTableModel);
        cartScrollPane = new JScrollPane(cartTable);

        JButton viewShoppingCartButton = new JButton("View Shopping Cart");
        viewShoppingCartButton.addActionListener(e -> viewShoppingCart());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel addToCartPanel = new JPanel(new BorderLayout());
        addToCartPanel.add(viewShoppingCartButton);
        bottomPanel.add(addToCartPanel, BorderLayout.SOUTH);
        bottomPanel.add(cartScrollPane, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateProductTable(String selectedCategory) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        List<Product> productList = List.of(products.getProductlist());
        if (productList != null) {
            for (Product product : productList) {
                if ("All".equals(selectedCategory) || product.getProductcategory().equalsIgnoreCase(selectedCategory)) {
                    model.addRow(new Object[]{product.getId(), product.getName(), product.getProductcategory(), product.getPrice()});
                }
            }
        } else {
            System.out.println("No products found");
        }
    }

    private void addToShoppingCart(ActionEvent e) {
        try {
            int selectedRow = productTable.getSelectedRow();

            if (selectedRow != -1) {
                Product selectedProduct = getProductFromSelectedRow(selectedRow);

                if (selectedProduct != null) {
                    // Check if the selected product is in stock
                    if (selectedProduct.getStock() > 0) {
                        // Assuming adding to cart reduces stock by 1
                        selectedProduct.removeStock(1);
                        cartItems.addProduct(selectedProduct, 1);
                        shoppingCartModel.addElement(selectedProduct.getName());
                        JOptionPane.showMessageDialog(frame, "Added to Shopping Cart!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "This product is out of stock.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: Unable to retrieve product information.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to add to the cart.");
            }
        } catch (Exception ex) {
            // Handle other unexpected exceptions
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();  // Print the stack trace for debugging purposes
        }
    }

    private void displayAdditionalInfo(Product selectedProduct) {
        StringBuilder additionalInfo = new StringBuilder();

        additionalInfo.append("\t\t\t\t\tProduct ID: ").append(selectedProduct.getId()).append("\n\n");
        additionalInfo.append("\t\t\t\t\tType: ").append(selectedProduct.getProductcategory()).append("\n\n");
        additionalInfo.append("\t\t\t\t\tName: ").append(selectedProduct.getName()).append("\n\n");

        if (selectedProduct instanceof ElectronicItem) {
            ElectronicItem electronicProduct = (ElectronicItem) selectedProduct;
            additionalInfo.append("\t\t\t\t\tBrand: ").append(electronicProduct.getBrand()).append("\n\n");
            additionalInfo.append("\t\t\t\t\tWarranty Period: ").append(electronicProduct.getWarranty()).append(" Weeks\n\n");
        } else if (selectedProduct instanceof ClothingItem) {
            ClothingItem clothProduct = (ClothingItem) selectedProduct;
            additionalInfo.append("\t\t\t\t\tSize: ").append(clothProduct.getSize()).append("\n\n");
            additionalInfo.append("\t\t\t\t\tColor: ").append(clothProduct.getColor()).append("\n\n");
        }

        productDetailsTextArea.setText(additionalInfo.toString());
    }

    private Product getProductFromSelectedRow(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < productTable.getRowCount()) {
            String productId = (String) productTable.getValueAt(selectedRow, 0);
            return products.getProductById(productId);
        }
        return null;
    }

    private void viewShoppingCart() {
        double finalPrice = calculateFinalPrice();
        JOptionPane.showMessageDialog(frame, cartScrollPane, "Shopping Cart - Total Price: $" + finalPrice, JOptionPane.PLAIN_MESSAGE);
    }

    private double calculateFinalPrice() {
        double finalPrice = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            double productPrice = (double) cartTableModel.getValueAt(i, 2);
            int quantity = (int) cartTableModel.getValueAt(i, 1);
            finalPrice += productPrice * quantity;
        }
        return finalPrice;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(WestminsterGUI::new);
    }
}
