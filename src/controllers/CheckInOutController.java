package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;

public class CheckInOutController implements Initializable
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Timeline timeline;
	
	// FXML elements
	@FXML
	private Button checkInButton;
	@FXML
	private Button checkOutButton;
	@FXML
	private Button homeButton;
	@FXML
	private TextField staffNumberTextField;
	@FXML
	private Text currentTimeLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		// Create a timeline that triggers every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Get the current time
            LocalTime currentTime = LocalTime.now();

            // Format the time in the desired format (HH:MM:SS)
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // Set the time label text to the formatted time
            currentTimeLabel.setText(formattedTime);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

	}
	
	public void checkInButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();
		
		String staffId = staffNumberTextField.getText();
		ResultSet resultSet = checkStaffId(staffId);
		
		if(!resultSet.next())
		{
			JOptionPane.showMessageDialog(null, "The Staff Id is invalid");
			DBConnection.disconnectToDB();
			return;
		}
		
		String staffName = resultSet.getString(2);

		if (isCheckInOrOutAlready(staffId, true))
		{
			JOptionPane.showMessageDialog(null, staffName + " has checked in today");
			DBConnection.disconnectToDB();	
			return;
		}
		
		int count = addCheckInOrOutRecord(staffId, true);
		if (count > 0)
		{
			JOptionPane.showMessageDialog(null, staffName + " Check In Successfully");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Failed to check in");
		}		
		
		DBConnection.disconnectToDB();		
	}
	
	public void checkOutButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();
		
		String staffId = staffNumberTextField.getText();
		ResultSet resultSet = checkStaffId(staffId);
		
		if(!resultSet.next())
		{
			JOptionPane.showMessageDialog(null, "The Staff Id is invalid");
			DBConnection.disconnectToDB();
			return;
		}
		
		String staffName = resultSet.getString(2);
		
		int count = addCheckInOrOutRecord(staffId, false);
		if (count > 0)
		{
			JOptionPane.showMessageDialog(null, staffName + " Check Out Successfully");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Failed to check out");
		}		
		
		DBConnection.disconnectToDB();		

	}
	
	public ResultSet checkStaffId(String staffId) throws SQLException
	{
		String staffIdQuery = "SELECT * FROM staff WHERE staffid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(staffIdQuery);
		pps.setString(1, staffId);
		ResultSet resultSet = pps.executeQuery();
		
		return resultSet;
	}
	
	public int addCheckInOrOutRecord(String staffId, boolean checkIn) throws SQLException
	{
		String query = "";
		if (checkIn)
		{
			query = "INSERT INTO checkinandout (staffid, working_date, check_in_time) VALUES "
					+ "(?, CAST(CURRENT_TIMESTAMP AS DATE), CURRENT_TIMESTAMP)";
		}
		else
		{
			query = "UPDATE checkinandout SET check_out_time = CURRENT_TIMESTAMP "
					+ "WHERE staffid = ? AND TRUNC(working_date) = TRUNC(CURRENT_TIMESTAMP)";
		}
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, staffId);
		int count = pps.executeUpdate();
		return count;
	}
	
	public boolean isCheckInOrOutAlready(String staffId, boolean checkIn) throws SQLException
	{
		String query = "";

		if (checkIn)
		{
			query = "SELECT * FROM checkinandout WHERE staffid = ? AND TRUNC(working_date) = TRUNC(CURRENT_TIMESTAMP) AND check_in_time IS NOT NULL";
		}
		else
		{
			query = "SELECT * FROM checkinandout WHERE staffid = ? AND TRUNC(working_date) = TRUNC(CURRENT_TIMESTAMP) AND check_out_time IS NOT NULL";
		}
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, staffId);
		ResultSet resultSet = pps.executeQuery();
		if (resultSet.next())
		{
			return true;
		}
		return false;
	}
	
	// Navigation
	public void homeButtonClick(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/Homepage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	
}
