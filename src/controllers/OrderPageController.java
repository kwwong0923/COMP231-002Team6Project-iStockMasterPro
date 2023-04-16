package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sqlData.Announcement;
import sqlData.Order;
import sqlData.OrderItem;
import sqlData.Product;
import java.sql.Date;


import static application.DBConnection.connection;
import static controllers.OrderItemsPageController.Nav;

public class OrderPageController implements Initializable{
    @FXML
    public Button btnAddOrder;

    @FXML
    public Button btnHomePage;
    @FXML
    public TableColumn tc_orderId;
    @FXML
    public TableColumn tc_staffId;
    @FXML
    public TableColumn tc_total;
    @FXML
    public TableColumn tc_date;
    @FXML
    public TableView table;
    @FXML
    public TextField tf_orderID;
    @FXML
    public Button btnSearch;
    @FXML
    public Button btnPrintReceipt;
    @FXML
    public TextField tf_staffID;
    @FXML
    public DatePicker dp_orderDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateData();
    }

    public void populateData() {
        ObservableList<Order> orders = fetchOrders("SELECT * FROM orders");
        displayOrders(orders);
    }


    private ObservableList<Order> fetchOrders(String sql) {
        DBConnection.connectToDB();
        ObservableList<Order> orders = FXCollections.observableArrayList();
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int orderId = rs.getInt("orderID");
                double total = rs.getDouble("total");
                int staffId = rs.getInt("staffID");
                Date date = rs.getDate("order_date");
                Order order = new Order(orderId,total,staffId,date);
                orders.add(order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBConnection.disconnectToDB();
        }
        return orders;
    }
    String pattern = "dd-MMM-yy";
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public void filterData() {


        String orderID = tf_orderID.getText().isEmpty() ? null : tf_orderID.getText();
        LocalDate orderDate = dp_orderDate.getValue();
        String staffID = tf_staffID.getText().isEmpty() ? null : tf_staffID.getText();

        ObservableList<Order> orders = fetchOrders(buildQuery(orderID, orderDate, staffID));
        displayOrders(orders);
    }

    private String buildQuery(String orderID, LocalDate orderDate, String staffID) {
        StringBuilder sb = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        if (orderID != null) {
            sb.append(" AND orderId=").append(orderID);
        }
        if (orderDate != null) {
            String formattedDate = dateFormatter.format(orderDate);
            sb.append(" AND order_date='").append(formattedDate.trim()).append("'");
        }
        if (staffID != null) {
            sb.append(" AND staffID=").append(staffID);
        }
        return sb.toString();
    }


    private void displayOrders(ObservableList<Order> orders) {
        tc_orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tc_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        tc_staffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        tc_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getItems().clear();
        table.getItems().addAll(orders);

        tc_orderId.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(tc_orderId);
        table.sort();
    }

    public void showOrderInfo(int orderID) {
        DBConnection.connectToDB();
        // retrieve order information from the ORDERS table
        String getOrderSql = "SELECT total, staffID, order_date FROM ORDERS WHERE orderID = ?";
        try (PreparedStatement getOrderStatement = connection.prepareStatement(getOrderSql)) {
            getOrderStatement.setInt(1, orderID);
            ResultSet orderResult = getOrderStatement.executeQuery();
            if (orderResult.next()) {
                double total = orderResult.getDouble("total");
                int staffID = orderResult.getInt("staffID");
                LocalDate orderDate = orderResult.getDate("order_date").toLocalDate();

                // retrieve order item information from the ORDERITEMS table
                String getOrderItemsSql = "SELECT INVENTORY.product_name, ORDERITEMS.quantity, INVENTORY.price, (ORDERITEMS.quantity * INVENTORY.price) as subtotal " +
                        "FROM ORDERITEMS INNER JOIN INVENTORY ON ORDERITEMS.productID = INVENTORY.productID " +
                        "WHERE ORDERITEMS.orderID = ?";
                try (PreparedStatement getOrderItemsStatement = connection.prepareStatement(getOrderItemsSql)) {
                    getOrderItemsStatement.setInt(1, orderID);
                    ResultSet orderItemsResult = getOrderItemsStatement.executeQuery();
                    ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
                    while (orderItemsResult.next()) {
                        String productName = orderItemsResult.getString("product_name");
                        int quantity = orderItemsResult.getInt("quantity");
                        double price = orderItemsResult.getDouble("price");
                        double subtotal = orderItemsResult.getDouble("subtotal");

                        OrderItem orderItem = new OrderItem(orderID, productName, quantity, price, subtotal);
                        orderItems.add(orderItem);
                    }

                    // display order information in a popup dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order Receipt");
                    alert.setHeaderText("Order ID: " + orderID);
                    alert.setContentText("Order Date: " + orderDate + "\n" +
                            "Staff ID: " + staffID + "\n" +
                            "Total: $" + total + "\n\n" +
                            "Order Items:\n" +
                            "-------------------------------------\n");
                    for (OrderItem orderItem : orderItems) {
                        alert.setContentText(alert.getContentText() +
                                orderItem.getProductName() + " (" + orderItem.getQuantity() + " x $" + orderItem.getPrice() + " = $" + orderItem.getSubtotal() + ")\n");
                    }
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Order not found");
                alert.setContentText("The order ID entered does not exist.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.disconnectToDB();
        }
    }


    public void navHomePage(ActionEvent actionEvent) throws IOException {
        Nav(actionEvent,"/pages/Homepage.fxml");
    }

    public void navOrderItems(ActionEvent actionEvent) throws IOException {
        Nav(actionEvent,"/pages/orderItemsPage.fxml");
    }

    public void btnSearchClick(ActionEvent actionEvent) {
        filterData();
    }

    public void btnPrintReceiptClick(ActionEvent actionEvent) {
        Order order = (Order) table.getSelectionModel().getSelectedItem();
        showOrderInfo(order.getOrderId());
    }
}
