package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sqlData.TimeRecord;

public class AccountantController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	ObservableList<TimeRecord> timeRecordList;
	
	// FXML elements;
	@FXML
	private TableView<TimeRecord> timeRecordTableView;
	@FXML
	private TableColumn<TimeRecord, String> idTableColumn;
	@FXML
	private TableColumn<TimeRecord, String> nameTableColumn;
	@FXML
	private TableColumn<TimeRecord, Date> dateTableColumn;
	@FXML
	private TableColumn<TimeRecord, Timestamp> checkInTableColumn;
	@FXML
	private TableColumn<TimeRecord, Timestamp> checkOutTableColumn;
	@FXML
	private Button searchButton;
	@FXML
	private Button homepageButton;
	@FXML
	private Button reportButton;
	@FXML
	private Button showAllButton;
	@FXML
	private TextField searchTextField;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		try 
		{
			DBConnection.connectToDB();
			ResultSet result = readEmployeeRecord();
			displayTimeRecordInTableView(result);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			DBConnection.disconnectToDB();
		}
	}
	
	public void searchByID(ActionEvent event) throws IOException
	{
		String staffId = searchTextField.getText();
		try 
		{
			DBConnection.connectToDB();
			ResultSet result = readEmployeeRecord(staffId);
			displayTimeRecordInTableView(result);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			DBConnection.disconnectToDB();
		}
	}
	
	public void showAllRecords(ActionEvent event) throws IOException
	{
		try
		{
			DBConnection.connectToDB();
			ResultSet result = readEmployeeRecord();
			displayTimeRecordInTableView(result);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			DBConnection.disconnectToDB();
		}
	}
	
	public void navToHomepage(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/Homepage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void navToReport(ActionEvent event) throws IOException
	{
		
		
	}
	
	public ResultSet readEmployeeRecord() throws SQLException
	{		
		String query = "SELECT chk.staffid, s.staff_name, chk.working_date, TO_CHAR(chk.check_in_time, 'HH:MI:SS AM') AS check_in_time, TO_CHAR(chk.check_out_time, 'HH:MI:SS AM') AS check_out_time\r\n"
				+ "FROM checkinandout chk\r\n"
				+ "JOIN staff s\r\n"
				+ "ON chk.staffid = s.staffid";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		ResultSet resultSet = pps.executeQuery();
		return resultSet;
	}
	
	public ResultSet readEmployeeRecord(String staffId) throws SQLException
	{
		String query = "SELECT chk.staffid, s.staff_name, chk.working_date, TO_CHAR(chk.check_in_time, 'HH:MI:SS AM') AS check_in_time, TO_CHAR(chk.check_out_time, 'HH:MI:SS AM') AS check_out_time\r\n"
				+ "FROM checkinandout chk\r\n"
				+ "JOIN staff s\r\n"
				+ "ON chk.staffid = s.staffid\r\n"
				+ "WHERE s.staffid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setString(1, staffId);
		ResultSet resultSet = pps.executeQuery();
		return resultSet;
	}
	
	public void displayTimeRecordInTableView(ResultSet result) throws SQLException
	{
		timeRecordList = FXCollections.observableArrayList();
		setTableViewColumn();
		while(result.next())
		{
			timeRecordList.add(new TimeRecord(result.getString(1), result.getString(2), result.getDate(3), result.getString(4), result.getString(5)));
		}
		
		timeRecordTableView.setItems(timeRecordList);
		timeRecordTableView.getSortOrder().add(dateTableColumn);
		timeRecordTableView.sort();			
	}
	
	public void setTableViewColumn()
	{
		idTableColumn.setCellValueFactory(new PropertyValueFactory<TimeRecord, String>("staffId"));
		nameTableColumn.setCellValueFactory(new PropertyValueFactory<TimeRecord, String>("staffName"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<TimeRecord, Date>("workingDate"));
		checkInTableColumn.setCellValueFactory(new PropertyValueFactory<TimeRecord, Timestamp>("checkInTime"));
		checkOutTableColumn.setCellValueFactory(new PropertyValueFactory<TimeRecord, Timestamp>("checkOutTime"));
	}

}
