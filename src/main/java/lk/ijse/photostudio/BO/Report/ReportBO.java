package lk.ijse.photostudio.BO.Report;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DTO.ReportDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ReportBO extends SuperBO {
    ArrayList<OrderDTO> getOrderReport(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException;

    ArrayList<BookingDTO> getBookingsByFilter(LocalDate from, LocalDate to, String customer) throws SQLException, ClassNotFoundException;

    ArrayList<ReportDTO> getPackageReport(String category) throws SQLException, ClassNotFoundException;

    ArrayList<ReportDTO> getStockReport(String supplier) throws SQLException, ClassNotFoundException;

    // Printing Methods
    void printBookingReport() throws Exception;

    void printPackageReport() throws Exception;

    void printOrderProfitReport() throws Exception;
}
