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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sqlData.Order;
import sqlData.OrderDetail;
import sqlData.OrderItem;
import sqlData.TimeRecord;

public class FinancialReportController implements Initializable
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private ComboBox monthComboBox;
	@FXML
	private ComboBox yearComboBox;
	@FXML
	private Button generateButton;
	@FXML
	private Button backButton;
	@FXML
	private Button homepageButton;
	@FXML
	private Text profitText;
	@FXML
	private Text bestSellingProductText;
	@FXML
	private Text bestSellingQuantityText;
	@FXML
	private TableView<OrderDetail> highestOrderTableView;
	@FXML
	private TableColumn<OrderDetail, Integer> idTableColumn;
	@FXML
	private TableColumn<OrderDetail, String> staffNameTableColumn;
	@FXML
	private TableColumn<OrderDetail, Double> totalTableColumn;
	@FXML
	private TableView<OrderItem> orderDetailTabelView;
	@FXML
	private TableColumn<OrderItem, String> productNameTableColumn;
	@FXML
	private TableColumn<OrderItem, Integer> quantityTableColumn;
	@FXML
	private TableColumn<OrderItem, Double> priceTableColumn;
	
	
	//------------
	ObservableList<OrderDetail> highestOrderDetailList;
	ObservableList<OrderItem> highestOrderItemList;
	
	String fromQuery = " FROM orders o "
			+ "JOIN orderItems oi USING (orderid) "
			+ "JOIN staff s USING (staffId) "
			+ "JOIN inventory iv USING (productid) ";
	
	String monthAndYearQuery = " EXTRACT(MONTH FROM order_date) = ? "
			+ "AND EXTRACT(YEAR FROM order_date) = ?";
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		monthComboBox.setItems(FXCollections.observableArrayList(1 ,2 ,3 ,4 ,5 ,6 ,7 ,8 ,9 ,10 ,11 ,12));
		yearComboBox.setItems(FXCollections.observableArrayList(2023, 2024, 2025, 2026));
	}
	
	public void generateReport(ActionEvent event) throws IOException
	{
		int month = (int) monthComboBox.getValue();
		int year = (int) yearComboBox.getValue();
		
		double profit;
		try 
		{
			DBConnection.connectToDB();
			profit = getWholeProfit(month, year);
			if (profit == 0)
			{
				noRecord();
			}
			else
			{
				profitText.setText("$" + Double.toString(profit));
				getHighestProfitOrder(month, year);
				displayOrderDetail(highestOrderDetailList, highestOrderItemList);
				DisplayBestSellingProduct(month, year);
			}
			

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
	
	private void getHighestProfitOrder(int month, int year) throws SQLException
	{
		highestOrderDetailList = FXCollections.observableArrayList();
		String staffName;
		Order highestOrder;
		OrderDetail highestOrderDetail;
		String query = "SELECT staff_name, orderId, Total, staffid, order_date "
				+ "FROM orders "
				+ "JOIN staff "
				+ "USING (staffid) "
				+ "WHERE total = ( "
				+ "SELECT MAX(total) "
				+ "FROM orders WHERE " + monthAndYearQuery
				+ ") AND " + monthAndYearQuery;
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setInt(1, month);
		pps.setInt(2, year);
		pps.setInt(3, month);
		pps.setInt(4, year);
		ResultSet result = pps.executeQuery();
		while (result.next())
		{
			staffName = result.getString(1);
			highestOrderItemList = getHighestProfitOrderItems(result.getInt(2));
			highestOrderDetail = new OrderDetail(result.getInt(2), staffName, result.getDouble(3), highestOrderItemList);
			highestOrderDetailList.add(highestOrderDetail);
		}
		return;		
	}
	
	public double getWholeProfit(int month, int year) throws SQLException
	{
		double profit = 0;
		String query = "SELECT SUM(total) FROM orders WHERE " + monthAndYearQuery;
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setInt(1, month);
		pps.setInt(2, year);
		ResultSet result = pps.executeQuery();
		while (result.next())
		{
			profit = result.getDouble(1);
		}
		return profit;
	}
	
	private ObservableList<OrderItem> getHighestProfitOrderItems(int orderId) throws SQLException
	{
		ObservableList<OrderItem> orderItemList = FXCollections.observableArrayList();
		String query = "SELECT orderid, product_name, quantity, price "
				+ "FROM orderitems "
				+ "JOIN inventory "
				+ "USING (productid) "
				+ "WHERE orderid = ?";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setInt(1, orderId);
		ResultSet result = pps.executeQuery();
		while (result.next())
		{
			orderItemList.add(new OrderItem(result.getInt(1), result.getString(2), result.getInt(3), result.getDouble(4)));
		}
		return orderItemList;
	}
	
	private void displayOrderDetail(ObservableList<OrderDetail> orderDetail, ObservableList<OrderItem> orderItemList)
	{
		setTableViewColumn();
		highestOrderTableView.setItems(orderDetail);
		orderDetailTabelView.setItems(orderItemList);
	}
	
	private void setTableViewColumn()
	{
		idTableColumn.setCellValueFactory(new PropertyValueFactory<OrderDetail, Integer>("orderId"));
		staffNameTableColumn.setCellValueFactory(new PropertyValueFactory<OrderDetail, String>("staffName"));
		totalTableColumn.setCellValueFactory(new PropertyValueFactory<OrderDetail, Double>("total"));
		
		productNameTableColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("productName"));
		quantityTableColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Integer>("quantity"));
		priceTableColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("price"));
	}
	
	private void DisplayBestSellingProduct(int month, int year) throws SQLException
	{
		String query = "SELECT i.product_name, SUM(oi.quantity) AS total_quantity "
				+ "FROM orderItems oi JOIN inventory i ON oi.productid = i.productid "
				+ "JOIN orders o ON o.orderid = oi.orderid "
				+ "WHERE EXTRACT(MONTH FROM order_date) = ? AND EXTRACT(YEAR FROM order_date) = ? "
				+ "GROUP BY oi.productid, i.product_name ORDER BY total_quantity DESC FETCH FIRST 1 ROW ONLY";
		PreparedStatement pps = DBConnection.connection.prepareStatement(query);
		pps.setInt(1, month);
		pps.setInt(2, year);
		ResultSet result = pps.executeQuery();
		while (result.next())
		{
			bestSellingProductText.setText(result.getString(1));
			bestSellingQuantityText.setText(Integer.toString(result.getInt(2)));
		}
	}
	
	public void noRecord()
	{
		JOptionPane.showMessageDialog(null, "No Record");
		profitText.setText("No Record");
		bestSellingProductText.setText("No Record");
		bestSellingQuantityText.setText("No Record");
		if (highestOrderDetailList != null)
		{
			highestOrderDetailList.clear();

		}
		if (highestOrderItemList != null)
		{
			highestOrderItemList.clear();

		}
	}
	
	
	// nav
	public void navToAccountant(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("/pages/AccountantPage.fxml"));
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
