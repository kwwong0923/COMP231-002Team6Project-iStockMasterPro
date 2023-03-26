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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sqlData.Announcement;

import static application.DBConnection.connection;
import static controllers.OrderItemsPageController.Nav;

public class OrderPageController {
    @FXML
    public Button btnAddOrder;

    @FXML
    public Button btnHomePage;

    public void navHomePage(ActionEvent actionEvent) throws IOException {
        Nav(actionEvent,"/pages/Homepage.fxml");
    }

    public void navOrderItems(ActionEvent actionEvent) throws IOException {
        Nav(actionEvent,"/pages/orderItemsPage.fxml");
    }
}
