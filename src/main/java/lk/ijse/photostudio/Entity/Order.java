package lk.ijse.photostudio.Entity;

public class Order {
    private String orderId;
    private String bookingId;
    private String customerId;
    private double pkgPrice;
    private double optPrice;
    private double grandTotal;

    public Order() {
    }

    public Order(String orderId, String bookingId, String customerId, double pkgPrice, double optPrice, double grandTotal) {
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.pkgPrice = pkgPrice;
        this.optPrice = optPrice;
        this.grandTotal = grandTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getPkgPrice() {
        return pkgPrice;
    }

    public double getOptPrice() {
        return optPrice;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}
