package application;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sqlData.Announcement;

public class HomepageController implements Initializable
{
	List<Announcement> announcements = new ArrayList<>();
	ObservableList<Announcement> announcementList;
	@FXML
	private TableView<Announcement> announcementTableView;
	@FXML
	private TableColumn<Announcement, String> idTableColumn;
	@FXML
	private TableColumn<Announcement, String> contentTableColumn;
	@FXML
	private TableColumn<Announcement, Date> dateTableColumn;
	 
	
	public void getAnnouncementData() throws SQLException
	{	
		DBConnection.connectToDB();
		// instantiate list
		announcementList = FXCollections.observableArrayList();
		// query statement and execute it
		String query = "SELECT * FROM announcement";		
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		ResultSet resultSet = pps.executeQuery();
		
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
			System.out.println(newAnnouncement);
			announcementList.add(newAnnouncement);
		}



		announcementTableView.setItems(announcementList);
		DBConnection.disconnectToDB();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{		
		try
		{
			getAnnouncementData();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
