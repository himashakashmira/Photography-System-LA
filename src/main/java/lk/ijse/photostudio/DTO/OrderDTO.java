package lk.ijse.photostudio.DTO;

public class OrderDTO {
    private String orderId;
    private String bookingId;
    private String customerId;
    private double pkgPrice;
    private double optPrice;
    private double grandTotal;

    public OrderDTO() {}

    public OrderDTO(String orderId, String bookingId, String customerId, double pkgPrice, double optPrice, double grandTotal) {
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.pkgPrice = pkgPrice;
        this.optPrice = optPrice;
        this.grandTotal = grandTotal;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public double getPkgPrice() { return pkgPrice; }
    public void setPkgPrice(double pkgPrice) { this.pkgPrice = pkgPrice; }
    public double getOptPrice() { return optPrice; }
    public void setOptPrice(double optPrice) { this.optPrice = optPrice; }
    public double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }
}