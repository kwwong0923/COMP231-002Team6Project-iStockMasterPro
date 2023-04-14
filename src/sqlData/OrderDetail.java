package sqlData;

import javafx.collections.ObservableList;

public class OrderDetail 
{
	public  int orderId;
	public  String staffName;
	public  double total;
	public  ObservableList<OrderItem> orderItemList;
	
	public OrderDetail(int orderId, String staffName, double total, ObservableList<OrderItem> orderItemList)
	{
		this.orderId = orderId;
		this.staffName = staffName;
		this.total = total;
		this.orderItemList = orderItemList;
	}
	
	
	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getStaffName() {
		return staffName;
	}


	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


	public ObservableList<OrderItem> getOrderItemList() {
		return orderItemList;
	}


	public void setOrderItemList(ObservableList<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}


	@Override
	public String toString()
	{
		return orderId + "-" + staffName + "-" + total;
	}
}
