package lk.ijse.photostudio.DTO;

import java.time.LocalDate;

public class BookingDTO {
    private String bookingId;
    private String customerId;
    private String packageId;
    private String additionalOption;
    private String status;
    private LocalDate eventDate;
    private String timeSlot; // New Field

    public BookingDTO() {
    }

    public BookingDTO(String bookingId, String customerId, String packageId, String additionalOption, String status, LocalDate eventDate, String timeSlot) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.additionalOption = additionalOption;
        this.status = status;
        this.eventDate = eventDate;
        this.timeSlot = timeSlot;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId; }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getAdditionalOption() {
        return additionalOption;
    }

    public void setAdditionalOption(String additionalOption) {
        this.additionalOption = additionalOption;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId='" + bookingId + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                '}';
    }
}