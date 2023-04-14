package sqlData;

public class OrderItem {
    int orderId;
    String productName;
    int productID;
    int quantity;
    double price;
    double subtotal;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getSubtotal() {
        return quantity * price;
    }


    public OrderItem(int orderId, String productName, int quantity, double price, double subtotal) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }
    
    public OrderItem(int orderId, String productName, int quantity, double price) 
    {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }
    
    @Override
	public String toString()
	{
		return productName + "-" + quantity + "-" + price;
	}

}
