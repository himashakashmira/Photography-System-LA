package lk.ijse.photostudio.DTO;

import java.time.LocalDate;

public class BookingDTO {
    private String bookingId;
    private String customerId;
    private String packageId;
    private String additionalOption;
    private LocalDate eventDate;
    private String timeSlot;
    private String status;

    public BookingDTO() {
    }

    public BookingDTO(String bookingId, String customerId, String packageId, String additionalOption, LocalDate eventDate, String timeSlot, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.additionalOption = additionalOption;
        this.eventDate = eventDate;
        this.timeSlot = timeSlot;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId='" + bookingId + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                '}';
    }
}