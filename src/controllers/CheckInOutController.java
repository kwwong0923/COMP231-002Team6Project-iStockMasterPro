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
		String staffIdQuery = "SELECT * FROM staff WHERE staffid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(staffIdQuery);
		pps.setString(1, staffId);
		ResultSet resultSet = pps.executeQuery();
		
		if(!resultSet.next())
		{
			JOptionPane.showMessageDialog(null, "The Staff Id is invalid");
		}
		else
		{
			String staffName = resultSet.getString(2);
			String query = "INSERT INTO checkinandout (staffid, time, is_check_in) VALUES (?, SYSDATE, 'T')";
			pps = DBConnection.connection.prepareStatement(query);
			pps.setString(1, staffId);
			int count = pps.executeUpdate();
			if (count > 0)
			{
				JOptionPane.showMessageDialog(null, staffName + " Checked-In");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Failed to check in");
			}
		}
		
		DBConnection.disconnectToDB();		
	}
	
	public void checkOutButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();
		
		String staffId = staffNumberTextField.getText();
		String staffIdQuery = "SELECT * FROM staff WHERE staffid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(staffIdQuery);
		pps.setString(1, staffId);
		ResultSet resultSet = pps.executeQuery();
		
		if(!resultSet.next())
		{
			JOptionPane.showMessageDialog(null, "The Staff Id is invalid");
		}
		else
		{
			String staffName = resultSet.getString(2);
			String query = "INSERT INTO checkinandout (staffid, time, is_check_in) VALUES (?, SYSDATE, 'F')";
			pps = DBConnection.connection.prepareStatement(query);
			pps.setString(1, staffId);
			int count = pps.executeUpdate();
			if (count > 0)
			{
				JOptionPane.showMessageDialog(null, staffName + "Checked-Out");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Failed to check out");
			}
		}
		
		DBConnection.disconnectToDB();	
	}
	
	public void homeButtonClick(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/Homepage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	
}
