package controllers;

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
import sqlData.OrderItem;
import sqlData.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static application.DBConnection.connection;

public class OrderItemsPageController implements Initializable {

    @FXML
    public TableView table;
    @FXML
    public Label label_OrderId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderID = getOrderID();
        total = 0.0;
        label_OrderId.setText(String.valueOf(orderID));
        //tf_OrderID.setText(String.valueOf(orderID));

    }
    private static Stage stage;
    private static Scene scene;
    private static Parent root;
    @FXML public TableColumn tc_OrderID;
    @FXML public TableColumn tc_ProductName;
    @FXML public TableColumn tc_Price;
    @FXML public TableColumn tc_Quantity;
    @FXML public TableColumn tc_SubTotal;
    @FXML
    public TextField tf_OrderID;
    @FXML
    public Button btnHomePage;
    @FXML
    public Button btnOrderList;
    @FXML
    public TextField tf_StaffID;
    @FXML
    public TextField tf_ProductID;
    @FXML
    public TextField tf_Quantity;
    @FXML
    public Button btn_Clear;
    @FXML
    public Button btn_AddItem;

    public static void Nav(ActionEvent actionEvent, String path) throws IOException {
        root = FXMLLoader.load(OrderItemsPageController.class.getResource(path));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public int getOrderID() {
        DBConnection.connectToDB();
        int orderId = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT orderID_seq.nextval as order_id FROM dual");
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnection.disconnectToDB();
        }
        return orderId;
    }

    public void navHomePage(ActionEvent actionEvent) throws IOException {
        Nav(actionEvent, "/pages/Homepage.fxml");
    }

    public void navOrderList(ActionEvent actionEvent) throws IOException {
        addOrder();
        Nav(actionEvent,"/pages/orderPage.fxml");
    }

    public void btnClearClick(ActionEvent actionEvent) {
        tf_StaffID.clear();
        tf_ProductID.clear();
        tf_Quantity.clear();
    }

    public void btnAddItemClick(ActionEvent actionEvent) {
        addItem();
        tf_ProductID.clear();
        tf_Quantity.clear();
    }
    LocalDate date = LocalDate.now();
    public void addOrder(){
        String staffIDText = tf_StaffID.getText();

        if (staffIDText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing information");
            alert.setContentText("Please enter your staff ID.");
            alert.showAndWait();
            return;
        }
        DBConnection.connectToDB();
        int staffID = Integer.parseInt(tf_StaffID.getText());
        String sql = "INSERT INTO ORDERS (orderID, total, staffID, order_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertOrderItemStatement = connection.prepareStatement(sql)) {
            insertOrderItemStatement.setInt(1, orderID);
            insertOrderItemStatement.setDouble(2, total);
            insertOrderItemStatement.setInt(3, staffID);
            insertOrderItemStatement.setDate(4, Date.valueOf(date));
            insertOrderItemStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int orderID;
    private double total;
    public void addItem(){
        String productIDText = tf_ProductID.getText();
        String quantityText = tf_Quantity.getText();

        if (productIDText.isEmpty() || quantityText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing information");
            alert.setContentText("Please enter both the product ID and quantity.");
            alert.showAndWait();
            return;
        }

        int productID = Integer.parseInt(tf_ProductID.getText());
        int quantity = Integer.parseInt(tf_Quantity.getText());

        DBConnection.connectToDB();

        String getProductSql = "SELECT product_name, price, stock FROM INVENTORY WHERE productID = ?";
        try (PreparedStatement getProductStatement = connection.prepareStatement(getProductSql)) {
            getProductStatement.setInt(1, productID);
            ResultSet productResult = getProductStatement.executeQuery();
            if (productResult.next()) {
                String productName = productResult.getString("product_name");
                double price = productResult.getDouble("price");
                double subtotal = price * quantity;

                // insert order item into the ORDERITEMS table
                String insertOrderItemSql = "INSERT INTO ORDERITEMS (orderID, productID, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertOrderItemStatement = connection.prepareStatement(insertOrderItemSql)) {
                    insertOrderItemStatement.setInt(1, orderID);
                    insertOrderItemStatement.setInt(2, productID);
                    insertOrderItemStatement.setInt(3, quantity);
                    insertOrderItemStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();

                OrderItem orderItem = new OrderItem(orderID,productName,quantity,price,subtotal);
                orderItems.add(orderItem);

                total += subtotal;

                tc_ProductName.setCellValueFactory(new PropertyValueFactory("productName"));
                tc_OrderID.setCellValueFactory(new PropertyValueFactory("orderId"));
                tc_Quantity.setCellValueFactory(new PropertyValueFactory("quantity"));
                tc_Price.setCellValueFactory(new PropertyValueFactory("price"));
                tc_SubTotal.setCellValueFactory(new PropertyValueFactory("subtotal"));
                table.getItems().addAll(orderItems);

                int currentStock = productResult.getInt("stock");
                int updatedStock = currentStock - quantity;


                String updateStockSql = "UPDATE INVENTORY SET stock = ? WHERE productID = ?";
                try (PreparedStatement updateStockStatement = connection.prepareStatement(updateStockSql)) {
                    updateStockStatement.setInt(1, updatedStock);
                    updateStockStatement.setInt(2, productID);
                    updateStockStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Product not found");
                alert.setContentText("The product ID entered does not exist.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.disconnectToDB();
        }
    }

}
