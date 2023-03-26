package sqlData;

public class Order {
    int orderId;
    double total;
    int staffId;
    String date;

    public int getOrderId() {
        return orderId;
    }

    public double getTotal() {
        return total;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getDate() {
        return date;
    }
}
