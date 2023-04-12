package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sqlData.Announcement;

public class AnnouncementController implements Initializable
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	ObservableList<Announcement> announcementList;
	
	Announcement selectedItem;
	// FXML elements
	@FXML
	private TableView<Announcement> announcementTableView;
	@FXML
	private TableColumn<Announcement, String> idTableColumn;
	@FXML
	private TableColumn<Announcement, String> contentTableColumn;
	@FXML
	private TableColumn<Announcement, Date> dateTableColumn;
	@FXML
	private Button newAnnouncementButton;
	@FXML
	private Button homepageButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button addButton;
	@FXML
	private Button clearButton;
	@FXML
	private TextArea announcementTextArea;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		System.out.println("HEY");
		try 
		{
			getAnnouncementData();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getAnnouncementData() throws SQLException
	{
		DBConnection.connectToDB();
		// instantiate list
		announcementList = FXCollections.observableArrayList();
		// query statement and execution
		String query = "SELECT * FROM announcement";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		ResultSet resultSet = pps.executeQuery();
		
		// set column properties
		idTableColumn.setCellValueFactory(new PropertyValueFactory<Announcement, String>("announcementId"));
	    contentTableColumn.setCellValueFactory(new PropertyValueFactory<Announcement, String>("content"));
	    dateTableColumn.setCellValueFactory(new PropertyValueFactory<Announcement, Date>("publishDate"));
	    
	    while(resultSet.next())
	    {
	    	Announcement newAnnouncement = new Announcement(
	    			resultSet.getString(1),
	    			resultSet.getString(2),
	    			resultSet.getDate(3)
	    			);
			announcementList.add(newAnnouncement);	    			
	    }
	    announcementTableView.setItems(announcementList);
	    announcementTableView.getSortOrder().add(idTableColumn);
		announcementTableView.sort();
		DBConnection.disconnectToDB();
	}
	
	public void addButtonClick(ActionEvent event) throws IOException, SQLException
	{
		if (announcementTextArea.getText() != "")
		{
			DBConnection.connectToDB();
			String content = announcementTextArea.getText();
			
			String query = "INSERT INTO announcement (announceid, content, pub_date) VALUES (announceid_seq.NEXTVAL, ? , CURRENT_TIMESTAMP)";
			PreparedStatement pps = DBConnection.connection.prepareStatement(query);
			pps.setString(1, content);
			int count = pps.executeUpdate();
			if (count > 0)
			{
				JOptionPane.showMessageDialog(null, "Announcement is published");
				announcementTextArea.clear();
				getAnnouncementData();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Failed!");
			}			
			DBConnection.disconnectToDB();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The content cannot be blank");
		}
	}
	
	public void clearButtonClick(ActionEvent event) throws IOException, SQLException
	{
		announcementTextArea.clear();
	}

	public void editButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();
		System.out.println(selectedItem);
		String query = "UPDATE announcement SET content = ? WHERE announceid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, announcementTextArea.getText());
		pps.setString(2, selectedItem.getAnnouncementId());
		System.out.println(pps);
		System.out.println(query);
		int count = pps.executeUpdate();
		System.out.println(count);
		if (count > 0)
		{
			JOptionPane.showMessageDialog(null, "The announcement was edited");
			getAnnouncementData();
			announcementTextArea.clear();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Failed to edit announcement");
		}		
		
		DBConnection.disconnectToDB();
	}


	public void deleteButtonClick(ActionEvent event) throws IOException, SQLException
	{
		DBConnection.connectToDB();

		String query = "DELETE FROM announcement WHERE announceid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, selectedItem.getAnnouncementId());
		int count = pps.executeUpdate();
		
		if (count > 0)
		{
			JOptionPane.showMessageDialog(null, "The announcement was delted");
			getAnnouncementData();
			announcementTextArea.clear();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Failed to delete announcement");
		}
		
		DBConnection.disconnectToDB();

	}
	
	public void selectItem(MouseEvent event) throws IOException
	{
		System.out.println("SELECT");
		selectedItem = announcementTableView.getSelectionModel().getSelectedItem();
		System.out.println(selectedItem.getAnnouncementId());
		announcementTextArea.setText(selectedItem.getContent());
		
	}
	// Navigation
	public void navToNewAnnouncement(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/NewAnnouncementPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
