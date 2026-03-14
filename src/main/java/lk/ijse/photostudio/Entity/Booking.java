package lk.ijse.photostudio.Entity;

import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private String customerId;
    private String packageId;
    private String additionalOption;
    private String status;
    private LocalDate eventDate;
    private String timeSlot;

    public Booking(String bookingId, String customerId, String packageId, String additionalOption, String status, LocalDate eventDate, String timeSlot) {
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

    public String getCustomerId() {
        return customerId;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getAdditionalOption() {
        return additionalOption;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }
}