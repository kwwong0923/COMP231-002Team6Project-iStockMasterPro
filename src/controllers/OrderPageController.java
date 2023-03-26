package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateData();
    }

    public void populateData(){
        DBConnection.connectToDB();
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String sql = "SELECT * FROM orders";
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int orderId = rs.getInt("orderID");
                double total = rs.getDouble("total");
                int staffId = rs.getInt("staffID");
                Date date =rs.getDate("order_date");
                Order order = new Order(orderId,total,staffId,date);
                orders.add(order);

            }
            tc_orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            tc_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            tc_staffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
            tc_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            //table.getItems().clear();
            table.getItems().addAll(orders);

            tc_orderId.setSortType(TableColumn.SortType.ASCENDING);
            table.getSortOrder().add(tc_orderId);
            table.sort();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBConnection.disconnectToDB();
        }
    }
    public void filterData(){
        DBConnection.connectToDB();
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String sql = "SELECT * FROM orders WHERE orderId= "+tf_orderID.getText();
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery())
        {
            while (rs.next()) {
                int orderId = rs.getInt("orderID");
                double total = rs.getDouble("total");
                int staffId = rs.getInt("staffID");
                Date date =rs.getDate("order_date");
                Order order = new Order(orderId,total,staffId,date);
                orders.add(order);

            }
            tc_orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            tc_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            tc_staffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
            tc_date.setCellValueFactory(new PropertyValueFactory<>("date"));

            table.getItems().clear();
            table.getItems().addAll(orders);
        } catch (SQLException ex) {
            ex.printStackTrace();
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
}
