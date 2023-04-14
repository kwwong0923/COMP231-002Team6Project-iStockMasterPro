package controllers;

import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sqlData.Case;


public class CaseAddDialogController implements Initializable{

	
    @FXML
    private TextField idTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField caseTypeTextField;

    @FXML
    private TextArea caseDetailTextArea;
    
    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;
    

    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	    ObservableList<String> options = FXCollections.observableArrayList("Pending", "Settled");
	    statusChoiceBox.setItems(options);
	}
	
	
	public int getIdFieldValue() {
		return Integer.parseInt(idTextField.getText());
	}

	public Date getDateFieldValue() {
	    return Date.valueOf(datePicker.getValue());
	}

	public String getTypeFieldValue() {
	    return caseTypeTextField.getText();
	}

	public String getDetailFieldValue() {
		return caseDetailTextArea.getText();
	}
	
	public String getStatusFieldValue() {
	    return statusChoiceBox.getValue().toString();
	}
	

	private void addCase() {
	    // Retrieve the data from the UI
	    int id = getIdFieldValue();
	    Date date = getDateFieldValue();
	    String type = getTypeFieldValue();
	    String detail = getDetailFieldValue();
	    String status = getStatusFieldValue();

	    // Create a new Case object with the data
	    Case newCase = new Case(id, date, type, detail, status);

	    // Insert the new case into the database
	    DBConnection.connectToDB();
	    String sql = "INSERT INTO case (caseid, casedate, casetype, casedetail, finished) VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement statement = DBConnection.connection.prepareStatement(sql)) {
	        statement.setInt(1, newCase.getCaseId());
	        statement.setDate(2, newCase.getCaseDate());
	        statement.setString(3, newCase.getCaseType());
	        statement.setString(4, newCase.getCaseDetail());
	        statement.setString(5, newCase.isFinished());
	        statement.executeUpdate();
	    } catch (SQLException ex) {
	        // Handle any SQL exceptions that occur
	        ex.printStackTrace();
	    } finally {
	        DBConnection.disconnectToDB();
	    }

	    JOptionPane.showMessageDialog(null, "The case ID: " + newCase.getCaseId() + " has been added successfully.");

	    // Close the dialog
	    closeButtonDialog();
	}

	@FXML
	private void closeButtonDialog() {
	    // Retrieve the stage from the update button
	    Stage stage = (Stage) addButton.getScene().getWindow();

	    // Close the stage
	    stage.close();
	}
	
	
	@FXML
	private void handleAddButtonAction(ActionEvent event) {
	   addCase();
	}
	
	@FXML
	private void handleCancelButtonAction(ActionEvent event) {
		closeButtonDialog();
	}




}
