package lk.ijse.photostudio.DAO.QueryDAO;

import lk.ijse.photostudio.DAO.SuperDAO;
import lk.ijse.photostudio.DTO.ReportDTO;
import lk.ijse.photostudio.Entity.Booking;
import lk.ijse.photostudio.Entity.Order;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<Booking> getTodaysPendingBookings() throws SQLException, ClassNotFoundException;

    int getTotalBookingsCount() throws SQLException, ClassNotFoundException;

    int getPendingBookingsCount() throws SQLException, ClassNotFoundException;

    double getTotalIncome() throws SQLException, ClassNotFoundException;

    ArrayList<Order> getOrderReport(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException;

    ArrayList<Booking> getBookingsByFilter(LocalDate from, LocalDate to, String customer) throws SQLException, ClassNotFoundException;

    ArrayList<ReportDTO> getPackageReport(String category) throws SQLException, ClassNotFoundException;

    ArrayList<ReportDTO> getStockReport(String supplier) throws SQLException, ClassNotFoundException;
}
