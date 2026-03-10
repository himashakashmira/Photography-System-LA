package lk.ijse.photostudio.BO.Order;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.OrderDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBO extends SuperBO {
    boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;

    String generateNextOrderId() throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;

    ArrayList<String> getPendingBookingIds() throws SQLException, ClassNotFoundException;

    double getPackagePrice(String packageId) throws SQLException, ClassNotFoundException;

    double getOptionPrice(String optionName) throws SQLException, ClassNotFoundException;
}
