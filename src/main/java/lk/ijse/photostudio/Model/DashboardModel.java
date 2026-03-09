package lk.ijse.photostudio.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardModel {

    public static ObservableList<BookingDTO> getTodaysPendingBookings() throws SQLException {
        ObservableList<BookingDTO> list = FXCollections.observableArrayList();

        String sql = "SELECT * FROM booking WHERE event_date = CURDATE()";

        try (ResultSet rs = CrudUtil.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new BookingDTO(
                        rs.getString("booking_id"),
                        rs.getString("customer_id"),
                        rs.getString("package_id"),
                        rs.getString("additional_option"),
                        rs.getString("status"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getString("time")
                ));
            }
        }
        return list;
    }

    // Get Total Booking Count
    public static int getTotalBookingsCount() throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) FROM booking")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Get Pending Orders Count
    public static int getPendingBookingsCount() throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) FROM booking WHERE status = 'Pending'")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Get Total Income
    public static double getTotalIncome() throws SQLException {
        try (ResultSet rs = CrudUtil.executeQuery("SELECT SUM(grand_total) FROM order_total")) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }
}