package announcementPage;

import java.io.IOException;
import java.sql.SQLException;

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
	
	public void publishButtonClick(ActionEvent event) throws IOException
	{
		
	}
	
	public void clearButtonClick(ActionEvent event) throws IOException
	{		
		announcementTextArea.clear();
	}
	
	public void backButtonClick(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("AnnouncementPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
