package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sqlData.Product;

public class InventoryController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private ObservableList<Product> data;
	
    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> stockLevelColumn;
    
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;
    



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
        data = FXCollections.observableArrayList();
        table.setItems(data);
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        try {
            loadProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	//being called in initialize
	public void loadProducts() throws SQLException {
        DBConnection.connectToDB();
        String sql = "SELECT * FROM inventory";
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("productid");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                data.add(new Product(id, name, price, stock));
            }
            } catch (SQLException ex) {
                // Handle any SQL exceptions that occur
                ex.printStackTrace();
            } finally {
                DBConnection.disconnectToDB();
            }
            
    }
	@FXML
	public void handleEditButtonAction(ActionEvent event) throws SQLException {
	    // Get the selected product from the table
	    Product selectedProduct = table.getSelectionModel().getSelectedItem();

	    if (selectedProduct != null) {
	        // Create a new instance of the inventory update dialog
	        Dialog<Product> dialog = new Dialog<>();
	        dialog.setTitle("Edit Product");
	        
	        

	        // Load the inventory update dialog FXML file
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/InventoryUpdateDialog.fxml"));
	        try {
	            dialog.getDialogPane().setContent(loader.load());
	            InventoryUpdateDialogController controller = loader.getController();

	            if (controller != null) {
	                // Set the initial values of the text fields and spinners
	                controller.setProduct(selectedProduct);

	                // Show the inventory update dialog and wait for the user to click OK or Cancel
	                //The dialog must close and run the below codes
	                dialog.showAndWait();

	                data.clear();
	                loadProducts();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	        // If no product is selected, show an error message
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Product Selected");
	        alert.setContentText("Please select a product in the table.");
	        alert.showAndWait();
	    }
	}
	
	public void handleAddButtonAction(ActionEvent event) throws SQLException{
		
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        // Load the inventory update dialog FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/InventoryAddDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
            InventoryAddDialogController controller = loader.getController();
            if (controller != null) {
            	dialog.showAndWait();
            	
            	data.clear();
            	loadProducts();
            }
	   } catch (IOException e) {
	            e.printStackTrace();
	   }
		
	}
	
	
	public void handleDeleteButtonAction(ActionEvent event) throws SQLException {
	    // Get the selected product from the table view
	    Product selectedProduct = table.getSelectionModel().getSelectedItem();
	    if (selectedProduct != null) {
	        // Show a confirmation dialog before deleting the product
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Delete Product");
	        alert.setHeaderText("Are you sure you want to delete the selected product?");
	        alert.setContentText("This action cannot be undone.");
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            // Delete the selected product from the database
	            DBConnection.connectToDB();
	            String sql = "DELETE FROM inventory WHERE productid = ?";
	            try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql)) {
	                statement.setInt(1, selectedProduct.getId());
	                statement.executeUpdate();
	            } catch (SQLException ex) {
	                // Handle any SQL exceptions that occur
	                ex.printStackTrace();
	            } finally {
	                DBConnection.disconnectToDB();
	            }

	            // Remove the selected product from the table view
	            data.remove(selectedProduct);
	        }
	    } else {
	        // If no product is selected, show an error message
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Delete Product");
	        alert.setHeaderText("No product selected");
	        alert.setContentText("Please select a product to delete.");
	        alert.showAndWait();
	    }
	}

	
	public void navToHomepage(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/Homepage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}