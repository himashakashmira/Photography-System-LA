package lk.ijse.photostudio.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModel {


    // TRANSACTION - Save Order & Update Booking Status

    public static boolean placeOrder(OrderDTO dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();

            // Disable Auto Commit (Start Transaction)
            connection.setAutoCommit(false);

            // Save the Order
            String orderSql = "INSERT INTO order_total(order_id, booking_id, customer_id, total_package_price, total_option_price, grand_total) VALUES (?,?,?,?,?,?)";
            boolean isOrderSaved = CrudUtil.execute(orderSql,
                    dto.getOrderId(),
                    dto.getBookingId(),
                    dto.getCustomerId(),
                    dto.getPkgPrice(),
                    dto.getOptPrice(),
                    dto.getGrandTotal()
            ).getUpdateCount() > 0;

            if (isOrderSaved) {
                // Update Booking Status 'Confirmed'
                String updateBookingSql = "UPDATE booking SET status = 'Confirmed' WHERE booking_id = ?";
                boolean isBookingUpdated = CrudUtil.execute(updateBookingSql, dto.getBookingId()).getUpdateCount() > 0;

                if (isBookingUpdated) {
                    connection.commit();
                    return true;
                }
            }

            // Rollback any step fails
            connection.rollback();
            return false;

        } catch (SQLException e) {
            // Rollback error
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            // Reset Auto Commit
            if (connection != null) connection.setAutoCommit(true);
        }
    }

    public static String getNextOrderId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT order_id FROM order_total ORDER BY order_id DESC LIMIT 1").getResultSet();
        if (rs.next()) {
            String currentId = rs.getString(1);
            int nextIdNum = Integer.parseInt(currentId.substring(3)) + 1;
            return String.format("ORD%03d", nextIdNum);
        }
        return "ORD001";
    }

    public static ObservableList<OrderDTO> getAllOrders() throws SQLException {
        ObservableList<OrderDTO> list = FXCollections.observableArrayList();
        ResultSet rs = CrudUtil.execute("SELECT * FROM order_total").getResultSet();

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
        return list;
    }

    // Only load bookings that are NOT in order_total yet (and are Pending)
    public static ObservableList<String> getPendingBookingIds() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();

        String sql = "SELECT booking_id FROM booking WHERE booking_id NOT IN (SELECT booking_id FROM order_total) AND status = 'Pending'";
        ResultSet rs = CrudUtil.execute(sql).getResultSet();
        while (rs.next()) {
            list.add(rs.getString("booking_id"));
        }
        return list;
    }

    public static double getPackagePrice(String packageId) throws SQLException {
        String sql = "SELECT base_price FROM package WHERE package_id = ?";
        ResultSet rs = CrudUtil.execute(sql, packageId).getResultSet();
        if (rs.next()) {
            return rs.getDouble("base_price");
        }
        return 0.0;
    }
}