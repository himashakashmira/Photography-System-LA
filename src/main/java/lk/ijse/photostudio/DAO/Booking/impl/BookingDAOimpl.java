package lk.ijse.photostudio.DAO.Booking.impl;

import lk.ijse.photostudio.DAO.Booking.BookingDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Booking;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingDAOimpl implements BookingDAO {
    @Override
    public ArrayList<Booking> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM booking ORDER BY booking_id DESC");
        ArrayList<Booking> allBookings = new ArrayList<>();
        while (rst.next()) {
            allBookings.add(new Booking(
                    rst.getString("booking_id"),
                    rst.getString("customer_id"),
                    rst.getString("package_id"),
                    rst.getString("additional_option"),
                    rst.getString("status"),
                    rst.getDate("event_date").toLocalDate(),
                    rst.getString("time")
            ));
        }
        return allBookings;
    }

    @Override
    public boolean save(Booking entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO booking VALUES (?,?,?,?,?,?,?)",
                entity.getBookingId(), entity.getCustomerId(), entity.getPackageId(),
                entity.getAdditionalOption(), entity.getEventDate(), entity.getTimeSlot(), entity.getStatus());
    }

    @Override
    public boolean update(Booking entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE booking SET customer_id=?, package_id=?, additional_option=?, event_date=?, time=?, status=? WHERE booking_id=?",
                entity.getCustomerId(), entity.getPackageId(), entity.getAdditionalOption(),
                entity.getEventDate(), entity.getTimeSlot(), entity.getStatus(), entity.getBookingId());
    }

    @Override
    public Booking search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM booking WHERE booking_id=?", id);
        if (rst.next()) {
            return new Booking(
                    rst.getString("booking_id"),
                    rst.getString("customer_id"),
                    rst.getString("package_id"),
                    rst.getString("additional_option"),
                    rst.getString("status"),
                    rst.getDate("event_date").toLocalDate(),
                    rst.getString("time")
            );
        }
        return null;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM booking WHERE booking_id=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT booking_id FROM booking ORDER BY booking_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("B", "")) + 1;
            return String.format("B%03d", newId);
        }
        return "B001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT booking_id FROM booking WHERE booking_id=?", id).next();
    }
}