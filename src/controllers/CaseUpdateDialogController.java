package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sqlData.Case;

public class CaseUpdateDialogController implements Initializable{

	@FXML
	private TextField idTextField;
	
	@FXML
	private ChoiceBox<String> statusChoiceBox;
	
	@FXML
	private Button updateButton;
	
	@FXML
	private Button cancelButton;
	
	
	private Case caseObj;
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		idTextField.setDisable(true);
		
	    ObservableList<String> options = FXCollections.observableArrayList("Settled", "Pending","Unassigned");
	    statusChoiceBox.setItems(options);
	   
	    
	    if (caseObj != null) {
	        idTextField.setText(Integer.valueOf(caseObj.getCaseId()).toString());
	        statusChoiceBox.setValue(caseObj.isFinished());
	    }

	    
	}
	
	public void setCase(Case caseObj) {
	    this.caseObj = caseObj;
	    if (caseObj != null) {
	        setIdFieldValue(caseObj.getCaseId());
	        setStatusFieldValue(caseObj.isFinished());
	    }
	}

	public void setIdFieldValue(int id) {
	    idTextField.setText(Integer.toString(id));
	}

	public void setStatusFieldValue(String status) {
	    statusChoiceBox.setValue(status);
	}

   
	public int getIdFieldValue() {
		return Integer.parseInt(idTextField.getText());
	}
	
	public String getStatusFieldValue() {
	    return statusChoiceBox.getValue().toString();
	}
	
    private void updateCase() {
    	// Retrieve the data from the UI
    	int id = getIdFieldValue();
    	String status = getStatusFieldValue();
    	// Create a new Product object with the updated data
    	Case updatedCase = new Case(id, status);
    	
        // Update the database with the new product details
        DBConnection.connectToDB();
        String sql = "UPDATE case SET finished = ? WHERE caseid = ?";
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql)) {
            statement.setString(1, updatedCase.isFinished());
            statement.setInt(2, updatedCase.getCaseId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            // Handle any SQL exceptions that occur
            ex.printStackTrace();
        } finally {
            DBConnection.disconnectToDB();
        }
        
        JOptionPane.showMessageDialog(null,"The case ID: " + updatedCase.getCaseId() + " has been updated Successfully.");

        // TODO: Save the updated product to the database or update an existing record

        // Close the dialog
        closeDialog();
        
    }

	private void closeDialog() {
		// TODO Auto-generated method stub
        // Retrieve the stage from the update button
        Stage stage = (Stage) updateButton.getScene().getWindow();

        // Close the stage
        stage.close();
	}
	
	@FXML
	private void handleUpdateBtnAction(ActionEvent event)throws IOException {
		updateCase();
	}
    @FXML
	private void handleCancelBtnAction(ActionEvent event)throws IOException {
		closeDialog();
	}
}
    


