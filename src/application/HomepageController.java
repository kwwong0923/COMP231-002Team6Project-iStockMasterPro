package application;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import sqlData.Announcement;

public class HomepageController implements Initializable
{
	@FXML
	private ListView<Announcement> announcementListView;
	
	public void getAnnouncementData() throws SQLException
	{
		String query = "SELECT * FROM announcement";
		
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		ResultSet resultSet = pps.executeQuery();
		
		while(resultSet.next())
		{
			System.out.println("Result");
			String announcementId = resultSet.getString(1);
			String content = resultSet.getString(2);
			Date publishDate = resultSet.getDate(3);
			System.out.println(announcementId + " " + content + " " + publishDate);
			Announcement newAnnouncement = new Announcement(announcementId, content, publishDate);
			System.out.println(newAnnouncement);
			announcementListView.getItems().add(newAnnouncement);
		}		
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
