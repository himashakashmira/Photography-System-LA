package lk.ijse.photostudio.BO.Order.impl;

import lk.ijse.photostudio.BO.Order.OrderBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.Order.OrderDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.Entity.Order;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOimpl implements OrderBO {
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        // TRANSACTION

        try {
            connection.setAutoCommit(false); // Transaction Start

            // 1. Save Order
            boolean isSaved = orderDAO.save(new Order(dto.getOrderId(), dto.getBookingId(), dto.getCustomerId(), dto.getPkgPrice(), dto.getOptPrice(), dto.getGrandTotal()));

            if (isSaved) {
                // 2. Update Booking Status
                boolean isUpdated = CrudUtil.execute("UPDATE booking SET status = 'Confirmed' WHERE booking_id = ?", dto.getBookingId());

                if (isUpdated) {
                    connection.commit(); // both are correct can commit
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public String generateNextOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewID();
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<Order> entities = orderDAO.getAll();
        ArrayList<OrderDTO> dtos = new ArrayList<>();
        for (Order o : entities) {
            dtos.add(new OrderDTO(o.getOrderId(), o.getBookingId(), o.getCustomerId(), o.getPkgPrice(), o.getOptPrice(), o.getGrandTotal()));
        }
        return dtos;
    }

    @Override
    public ArrayList<String> getPendingBookingIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT booking_id FROM booking WHERE booking_id NOT IN (SELECT booking_id FROM order_total) AND status = 'Pending'");
        ArrayList<String> list = new ArrayList<>();
        while (rs.next()) { list.add(rs.getString(1)); }
        return list;
    }

    @Override
    public double getPackagePrice(String packageId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT base_price FROM package WHERE package_id = ?", packageId);
        return rs.next() ? rs.getDouble(1) : 0.0;
    }

    @Override
    public double getOptionPrice(String optionName) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT price FROM package_additional WHERE option_name = ?", optionName);
        return rs.next() ? rs.getDouble(1) : 0.0;
    }
}
