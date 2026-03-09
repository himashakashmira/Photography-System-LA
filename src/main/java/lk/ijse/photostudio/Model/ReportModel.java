package lk.ijse.photostudio.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DTO.ReportDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class ReportModel {

    // ORDER / PROFIT REPORT (Confirmed Orders Only)
    public static ObservableList<OrderDTO> getOrderReport(LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT o.* FROM order_total o " +
                "JOIN booking b ON o.booking_id = b.booking_id " +
                "WHERE b.status = 'Confirmed'";

        if (from != null) {
            sql += " AND b.event_date >= '" + from + "'";
        }
        if (to != null) {
            sql += " AND b.event_date <= '" + to + "'";
        }

        ObservableList<OrderDTO> list = FXCollections.observableArrayList();
        try (var ps = CrudUtil.execute(sql)) {
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                list.add(new OrderDTO(
                        rs.getString("order_id"),
                        rs.getString("booking_id"),
                        rs.getString("customer_id"),
                        rs.getDouble("total_package_price"),
                        rs.getDouble("total_option_price"),
                        rs.getDouble("grand_total")
                ));
            }
        }
        return list;
    }

    // BOOKING REPORT
    public static ObservableList<BookingDTO> getBookingsByFilter(LocalDate from, LocalDate to, String customer) throws SQLException {
        String sql = "SELECT * FROM booking WHERE 1=1";
        if (from != null) sql += " AND event_date >= '" + from + "'";
        if (to != null) sql += " AND event_date <= '" + to + "'";
        if (customer != null && !customer.equals("All Customers")) sql += " AND customer_id = '" + customer + "'";
        sql += " ORDER BY event_date DESC";

        ObservableList<BookingDTO> list = FXCollections.observableArrayList();
        try (var ps = CrudUtil.execute(sql)) {
            ResultSet rs = ps.getResultSet();
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

    // PACKAGE REPORT
    public static ObservableList<ReportDTO> getPackageReport(String category) throws SQLException {
        String sql = "SELECT p.package_id, p.package_name, p.category, p.base_price, COUNT(b.booking_id) as usage_count " +
                "FROM package p LEFT JOIN booking b ON p.package_id = b.package_id";
        if (category != null && !category.equals("All Categories")) {
            sql += " WHERE p.category = '" + category + "'";
        }
        sql += " GROUP BY p.package_id ORDER BY usage_count DESC";

        ObservableList<ReportDTO> list = FXCollections.observableArrayList();
        try (var ps = CrudUtil.execute(sql)) {
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                list.add(new ReportDTO(
                        rs.getString("package_id"),
                        rs.getString("package_name"),
                        rs.getString("category"),
                        rs.getDouble("base_price"),
                        rs.getInt("usage_count")
                ));
            }
        }
        return list;
    }

    // STOCK REPORT
    public static ObservableList<ReportDTO> getStockReport(String supplier) throws SQLException {
        String sql = "SELECT * FROM material";

        if (supplier != null && !supplier.equals("All Suppliers") && !supplier.isEmpty()) {
            sql += " WHERE supplier_id = '" + supplier + "'";
        }

        ObservableList<ReportDTO> list = FXCollections.observableArrayList();

        try (var ps = CrudUtil.execute(sql)) {
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                ReportDTO dto = new ReportDTO();

                dto.setItemId(rs.getString("material_id"));
                dto.setItemName(rs.getString("material_type"));
                dto.setQty(rs.getInt("quantity"));
                dto.setSupplierId(rs.getString("supplier_id"));

                // Stock Check
                if (dto.getQty() < 10) {
                    dto.setStatus("LOW STOCK");
                } else {
                    dto.setStatus("NORMAL");
                }

                list.add(dto);
            }
        }
        return list;
    }


    // PRINT Report


    public static void printBookingReport() throws JRException, SQLException {
        InputStream reportStream = ReportModel.class.getResourceAsStream("/lk/ijse/photostudio/reports/BookingReport.jrxml");
        generateReport(reportStream);
    }

    public static void printPackageReport() throws JRException, SQLException {
        InputStream reportStream = ReportModel.class.getResourceAsStream("/lk/ijse/photostudio/reports/PackagesReport.jrxml");
        generateReport(reportStream);
    }

    public static void printOrderProfitReport() throws JRException, SQLException {
        InputStream reportStream = ReportModel.class.getResourceAsStream("/lk/ijse/photostudio/reports/OrderProfitReport.jrxml");
        generateReport(reportStream);
    }


    // Helper Method (Must be static)
    private static void generateReport(InputStream reportStream) throws JRException, SQLException {
        if (reportStream == null) {
            System.err.println("Report file not found! Check resources folder path.");
            return;
        }

        Connection conn = DBConnection.getInstance().getConnection();

        // Compile
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

        // View
        JasperViewer.viewReport(jasperPrint, false); // false = doesn't close app on exit
    }
}
