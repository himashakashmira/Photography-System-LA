package lk.ijse.photostudio.BO.Report.impl;

import lk.ijse.photostudio.BO.Report.ReportBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.QueryDAO.QueryDAO;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DTO.ReportDTO;
import lk.ijse.photostudio.Entity.Booking;
import lk.ijse.photostudio.Entity.Order;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportBOimpl implements ReportBO {
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public ArrayList<OrderDTO> getOrderReport(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {

        ArrayList<Order> entities = queryDAO.getOrderReport(from, to);
        ArrayList<OrderDTO> dtos = new ArrayList<>();

        for (Order o : entities) {
            dtos.add(new OrderDTO(o.getOrderId(), o.getBookingId(), o.getCustomerId(), o.getPkgPrice(), o.getOptPrice(), o.getGrandTotal()));
        }
        return dtos;
    }

    @Override
    public ArrayList<BookingDTO> getBookingsByFilter(LocalDate from, LocalDate to, String customer) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> entities = queryDAO.getBookingsByFilter(from, to, customer);
        ArrayList<BookingDTO> dtos = new ArrayList<>();

        for (Booking b : entities) {
            dtos.add(new BookingDTO(b.getBookingId(), b.getCustomerId(), b.getPackageId(), b.getAdditionalOption(), b.getEventDate(), b.getTimeSlot(), b.getStatus()));
        }
        return dtos;
    }

    @Override
    public ArrayList<ReportDTO> getPackageReport(String category) throws SQLException, ClassNotFoundException {
        return queryDAO.getPackageReport(category);
    }

    @Override
    public ArrayList<ReportDTO> getStockReport(String supplier) throws SQLException, ClassNotFoundException {
        return queryDAO.getStockReport(supplier);
    }

    // JasperReports Methods
    private void generateReport(String path) throws Exception {
        InputStream reportStream = getClass().getResourceAsStream(path);
        if (reportStream == null) throw new RuntimeException("Report file not found!");

        Connection conn = DBConnection.getInstance().getConnection();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
        JasperViewer.viewReport(jasperPrint, false);
    }

    @Override
    public void printBookingReport() throws Exception {
        generateReport("/lk/ijse/photostudio/reports/BookingReport.jrxml");
    }

    @Override
    public void printPackageReport() throws Exception {
        generateReport("/lk/ijse/photostudio/reports/PackagesReport.jrxml");
    }

    @Override
    public void printOrderProfitReport() throws Exception {
        generateReport("/lk/ijse/photostudio/reports/OrderProfitReport.jrxml");
    }
}