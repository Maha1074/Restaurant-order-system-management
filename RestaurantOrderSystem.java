import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Class to represent an order
class Order {
    private int orderId;
    private String itemName;
    private String status;

    public Order(int orderId, String itemName) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.status = "Preparing";
    }

    public int getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Item: " + itemName + ", Status: " + status;
    }
}

// Class to manage the restaurant orders
class OrderManager {
    private ArrayList<Order> orders;
    private int nextOrderId;

    public OrderManager() {
        orders = new ArrayList<>();
        nextOrderId = 1;
    }

    public void addOrder(String itemName) {
        Order newOrder = new Order(nextOrderId++, itemName);
        orders.add(newOrder);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setStatus(status);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}

// Main class with GUI
public class RestaurantOrderSystem extends JFrame {
    private OrderManager orderManager;
    private JTextArea orderDisplayArea;
    private JTextField itemNameField;
    private JTextField orderIdField;
    private JComboBox<String> statusComboBox;

    public RestaurantOrderSystem() {
        orderManager = new OrderManager();

        // Setup the frame
        setTitle("Restaurant Order Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the order display area
        orderDisplayArea = new JTextArea();
        orderDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDisplayArea);

        // Create the input panel for adding orders
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Order"));

        inputPanel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField();
        inputPanel.add(itemNameField);

        JButton addButton = new JButton("Add Order");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText().trim();
                if (!itemName.isEmpty()) {
                    orderManager.addOrder(itemName);
                    refreshOrderDisplay();
                    itemNameField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Item name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(addButton);

        // Create the update panel for updating order status
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new GridLayout(4, 2, 10, 10));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Update Order Status"));

        updatePanel.add(new JLabel("Order ID:"));
        orderIdField = new JTextField();
        updatePanel.add(orderIdField);

        updatePanel.add(new JLabel("New Status:"));
        String[] statuses = {"Preparing", "Ready", "Delivered"};
        statusComboBox = new JComboBox<>(statuses);
        updatePanel.add(statusComboBox);

        JButton updateButton = new JButton("Update Status");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String orderIdText = orderIdField.getText().trim();
                if (!orderIdText.isEmpty()) {
                    try {
                        int orderId = Integer.parseInt(orderIdText);
                        String status = (String) statusComboBox.getSelectedItem();
                        boolean updated = orderManager.updateOrderStatus(orderId, status);
                        if (updated) {
                            refreshOrderDisplay();
                            orderIdField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Order ID not found.", "Update Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Order ID must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Order ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        updatePanel.add(new JLabel()); // Empty label for spacing
        updatePanel.add(updateButton);

        // Organize the panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(updatePanel, BorderLayout.SOUTH);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Initial display
        refreshOrderDisplay();
    }

    private void refreshOrderDisplay() {
        orderDisplayArea.setText("");
        ArrayList<Order> orders = orderManager.getOrders();
        if (orders.isEmpty()) {
            orderDisplayArea.append("No orders to display.\n");
        } else {
            for (Order order : orders) {
                orderDisplayArea.append(order.toString() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RestaurantOrderSystem system = new RestaurantOrderSystem();
                system.setVisible(true);
            }
        });
    }
}
