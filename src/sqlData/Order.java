package sqlData;
import java.sql.Date;

public class Order {
    int orderId;
    double total;
    int staffId;
    Date date;

    public Order(int orderId, double total, int staffId, Date date) {
        this.orderId = orderId;
        this.total = total;
        this.staffId = staffId;
        this.date = date;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotal() {
        return total;
    }

    public int getStaffId() {
        return staffId;
    }

    public Date getDate() {
        return date;
    }
}
