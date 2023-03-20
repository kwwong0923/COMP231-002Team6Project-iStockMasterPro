package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class AnnouncementController implements Initializable
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	ObservableList<Announcement> announcementList;
	
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
