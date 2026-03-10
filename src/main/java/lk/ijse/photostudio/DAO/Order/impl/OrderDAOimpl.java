package lk.ijse.photostudio.DAO.Order.impl;

import lk.ijse.photostudio.DAO.Order.OrderDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOimpl implements OrderDAO {
    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM order_total");
        ArrayList<Order> allOrders = new ArrayList<>();
        while (rs.next()) {
            allOrders.add(new Order(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)));
        }
        return allOrders;
    }

    @Override
    public boolean save(Order entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO order_total VALUES (?,?,?,?,?,?)",
                entity.getOrderId(), entity.getBookingId(), entity.getCustomerId(),
                entity.getPkgPrice(), entity.getOptPrice(), entity.getGrandTotal());
    }

    @Override
    public boolean update(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT order_id FROM order_total ORDER BY order_id DESC LIMIT 1");
        if (rs.next()) {
            String currentId = rs.getString(1);
            int nextIdNum = Integer.parseInt(currentId.substring(3)) + 1;
            return String.format("ORD%03d", nextIdNum);
        }
        return "ORD001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }
}
