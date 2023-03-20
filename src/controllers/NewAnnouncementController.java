package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import javax.swing.JOptionPane;

import application.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class NewAnnouncementController 
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button publishButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button backButton;
	@FXML
	private TextArea announcementTextArea;
	
	public void publishButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();
		String content = announcementTextArea.getText();
		
		String query = "INSERT INTO announcement (announcement_id, content, pub_date) VALUES ('A' || LPAD(announcement_id_seq.NEXTVAL, 4, '0'), ? , CURRENT_TIMESTAMP)";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, content);
		int count = pps.executeUpdate();
		if (count > 0)
		{
			JOptionPane.showMessageDialog(null, "Announcement is published");
			announcementTextArea.clear();
//			DBConnection.connection.commit();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Failed!");
		}
		
		DBConnection.disconnectToDB();
	}
	
	public void clearButtonClick(ActionEvent event) throws IOException
	{		
		announcementTextArea.clear();
	}
	
	public void backButtonClick(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/AnnouncementPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
