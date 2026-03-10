package lk.ijse.photostudio.DAO.QueryDAO.impl;

import lk.ijse.photostudio.DAO.QueryDAO.QueryDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import lk.ijse.photostudio.DTO.ReportDTO;

import lk.ijse.photostudio.Entity.Order;

import java.time.LocalDate;

public class QueryDAOimpl implements QueryDAO {

    // Dashboard Methods
    @Override
    public int getTotalBookingsCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) FROM booking");
        return rs.next() ? rs.getInt(1) : 0;
    }

    @Override
    public int getPendingBookingsCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT COUNT(*) FROM booking WHERE status = 'Pending'");
        return rs.next() ? rs.getInt(1) : 0;
    }

    @Override
    public double getTotalIncome() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT SUM(grand_total) FROM order_total");
        return rs.next() ? rs.getDouble(1) : 0.0;
    }

    @Override
    public ArrayList<Booking> getTodaysPendingBookings() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM booking WHERE event_date = CURDATE()");
        ArrayList<Booking> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Booking(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7)));
        }
        return list;
    }

    // Report Methods

    @Override
    public ArrayList<Order> getOrderReport(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        String sql = "SELECT o.* FROM order_total o JOIN booking b ON o.booking_id = b.booking_id WHERE b.status = 'Confirmed'";
        if (from != null) sql += " AND b.event_date >= '" + from + "'";
        if (to != null) sql += " AND b.event_date <= '" + to + "'";

        ResultSet rs = CrudUtil.executeQuery(sql);
        ArrayList<Order> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)));
        }
        return list;
    }

    @Override
    public ArrayList<Booking> getBookingsByFilter(LocalDate from, LocalDate to, String customer) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM booking WHERE 1=1";
        if (from != null) sql += " AND event_date >= '" + from + "'";
        if (to != null) sql += " AND event_date <= '" + to + "'";
        if (customer != null && !customer.equals("All Customers")) sql += " AND customer_id = '" + customer + "'";
        sql += " ORDER BY event_date DESC";

        ResultSet rs = CrudUtil.executeQuery(sql);
        ArrayList<Booking> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Booking(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7)));
        }
        return list;
    }

    @Override
    public ArrayList<ReportDTO> getPackageReport(String category) throws SQLException, ClassNotFoundException {
        String sql = "SELECT p.package_id, p.package_name, p.category, p.base_price, COUNT(b.booking_id) as usage_count " +
                "FROM package p LEFT JOIN booking b ON p.package_id = b.package_id";
        if (category != null && !category.equals("All Categories")) sql += " WHERE p.category = '" + category + "'";
        sql += " GROUP BY p.package_id ORDER BY usage_count DESC";

        ResultSet rs = CrudUtil.executeQuery(sql);
        ArrayList<ReportDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new ReportDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)));
        }
        return list;
    }

    @Override
    public ArrayList<ReportDTO> getStockReport(String supplier) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM material";
        if (supplier != null && !supplier.equals("All Suppliers") && !supplier.isEmpty())
            sql += " WHERE supplier_id = '" + supplier + "'";

        ResultSet rs = CrudUtil.executeQuery(sql);
        ArrayList<ReportDTO> list = new ArrayList<>();
        while (rs.next()) {
            ReportDTO dto = new ReportDTO();
            dto.setItemId(rs.getString("material_id"));
            dto.setItemName(rs.getString("material_type"));
            dto.setQty(rs.getInt("quantity"));
            dto.setSupplierId(rs.getString("supplier_id"));
            dto.setStatus(dto.getQty() < 10 ? "LOW STOCK" : "NORMAL");
            list.add(dto);
        }
        return list;
    }
}
