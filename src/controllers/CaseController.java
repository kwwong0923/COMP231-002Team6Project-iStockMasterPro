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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sqlData.Case;
import sqlData.Product;

import java.sql.Date;


public class CaseController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private ObservableList<Case> data;
	
    @FXML
    private TableView<Case> table;

    @FXML
    private TableColumn<Case, Integer> caseIdColumn;

    @FXML
    private TableColumn<Case, Date> dateColumn;

    @FXML
    private TableColumn<Case, String> caseTypeColumn;

    @FXML
    private TableColumn<Case, String> caseDetailColumn;
    
    @FXML
    private TableColumn<Case, String> finishedColumn;
    
    @FXML
    private TextField searchBar;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button searchButton;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
        data = FXCollections.observableArrayList();
        table.setItems(data);
        
        caseIdColumn.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("caseDate"));
        caseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("caseType"));
        caseDetailColumn.setCellValueFactory(new PropertyValueFactory<>("caseDetail"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));
        
        try {
            loadCases();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	//being called in initialize
	public void loadCases() throws SQLException {
        DBConnection.connectToDB();
        String sql = "SELECT * FROM case";
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int caseId = rs.getInt("caseid");
                Date caseDate = rs.getDate("casedate");
                String caseType = rs.getString("casetype");
                String caseDetail = rs.getString("casedetail");
                String finished = rs.getString("finished");
                
                data.add(new Case(caseId, caseDate, caseType, caseDetail, finished));
            }
            } catch (SQLException ex) {
                // Handle any SQL exceptions that occur
                ex.printStackTrace();
            } finally {
                DBConnection.disconnectToDB();
            }
            
    }
	
	
	public void handleSearchButtonAction(ActionEvent event) throws SQLException{
		String caseId = searchBar.getText();
		
		if(!caseId .isEmpty()) {
        DBConnection.connectToDB();
       
        String sql = "SELECT * FROM case WHERE caseid = ?";
        try {
        	PreparedStatement statement = DBConnection.connection.prepareStatement(sql);
        	statement.setString(1, caseId);
        	ResultSet rs = statement.executeQuery();
        	
        
            while (rs.next()) {
                int id = rs.getInt("caseid");
                Date caseDate = rs.getDate("casedate");
                String caseType = rs.getString("casetype");
                String caseDetail = rs.getString("casedetail");
                String finished = rs.getString("finished");
                
                data.clear();
                data.add(new Case(id, caseDate, caseType, caseDetail, finished));
            }
            } catch (SQLException ex) {
                // Handle any SQL exceptions that occur
                ex.printStackTrace();
            } finally {
                DBConnection.disconnectToDB();
            }
		}
		else {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText("Please enter a Case Number.");
		alert.showAndWait();
		
		}
    }
	
	
	
	
	public void handleAddButtonAction(ActionEvent event) throws SQLException{
        Dialog<Case> dialog = new Dialog<>();
        dialog.setTitle("Add Case");
        // Load the case update dialog FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/CaseAddDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
            CaseAddDialogController controller = loader.getController();
            if (controller != null) {
            	dialog.showAndWait();
            	
            	data.clear();
            	loadCases();
            }
	   } catch (IOException e) {
	            e.printStackTrace();
	   }
	}
	
	
	public void handleUpdateButtonAction(ActionEvent event) throws SQLException{
	    // Get the selected product from the table
	    Case selectedCase = table.getSelectionModel().getSelectedItem();

	    if (selectedCase != null) {
	        // Create a new instance of the inventory update dialog
	        Dialog<Case> dialog = new Dialog<>();
	        dialog.setTitle("Manage Case");
	        
	        

	        // Load the case update dialog FXML file
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/CaseUpdateDialog.fxml.fxml"));
	        try {
	            dialog.getDialogPane().setContent(loader.load());
	            CaseUpdateDialogController controller = loader.getController();

	            if (controller != null) {

	                controller.setCase(selectedCase);

	                // Show the inventory update dialog and wait for the user to click OK or Cancel
	                //The dialog must close and run the below codes
	                dialog.showAndWait();

	                data.clear();
	                loadCases();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	        // If no product is selected, show an error message
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Case Selected");
	        alert.setContentText("Please select a Case in the table.");
	        alert.showAndWait();
	    }
	}
	
	public void handleDeleteButtonAction(ActionEvent event) throws SQLException {
	    // Get the selected product from the table view
	    Case selectedCase = table.getSelectionModel().getSelectedItem();
	    if (selectedCase != null) {
	        // Show a confirmation dialog before deleting the product
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Delete Case");
	        alert.setHeaderText("Are you sure you want to delete the selected Case?");
	        alert.setContentText("This action cannot be undone.");
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            // Delete the selected product from the database
	            DBConnection.connectToDB();
	            String sql = "DELETE FROM case WHERE caseid = ?";
	            try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql)) {
	                statement.setInt(1, selectedCase.getCaseId());
	                statement.executeUpdate();
	            } catch (SQLException ex) {
	                // Handle any SQL exceptions that occur
	                ex.printStackTrace();
	            } finally {
	                DBConnection.disconnectToDB();
	            }

	            // Remove the selected product from the table view
	            data.remove(selectedCase);
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

