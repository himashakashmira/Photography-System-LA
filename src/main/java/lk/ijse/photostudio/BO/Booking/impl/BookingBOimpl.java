package lk.ijse.photostudio.BO.Booking.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.BO.Booking.BookingBO;
import lk.ijse.photostudio.DAO.Booking.BookingDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.Entity.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingBOimpl implements BookingBO {
    BookingDAO bookingDAO = (BookingDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BOOKING);

    @Override
    public ArrayList<BookingDTO> getAllBookings() throws SQLException, ClassNotFoundException {
        ArrayList<Booking> entities = bookingDAO.getAll();
        ArrayList<BookingDTO> dtos = new ArrayList<>();
        for (Booking b : entities) {
            dtos.add(new BookingDTO(b.getBookingId(), b.getCustomerId(), b.getPackageId(), b.getAdditionalOption(), b.getStatus(), b.getEventDate(), b.getTimeSlot()));
        }
        return dtos;
    }

    @Override
    public boolean saveBooking(BookingDTO dto) throws SQLException, ClassNotFoundException {
        return bookingDAO.save(new Booking(dto.getBookingId(), dto.getCustomerId(), dto.getPackageId(), dto.getAdditionalOption(), dto.getStatus(), dto.getEventDate(), dto.getTimeSlot()));
    }

    @Override
    public boolean updateBooking(BookingDTO dto) throws SQLException, ClassNotFoundException {
        return bookingDAO.update(new Booking(dto.getBookingId(), dto.getCustomerId(), dto.getPackageId(), dto.getAdditionalOption(), dto.getStatus(), dto.getEventDate(), dto.getTimeSlot()));
    }

    @Override
    public boolean deleteBooking(String id) throws SQLException, ClassNotFoundException {
        return bookingDAO.delete(id);
    }

    @Override
    public String generateNextBookingId() throws SQLException, ClassNotFoundException {
        return bookingDAO.generateNewID();
    }

    @Override
    public BookingDTO searchBooking(String id) throws SQLException, ClassNotFoundException {
        Booking b = bookingDAO.search(id);
        if (b != null) {
            return new BookingDTO(b.getBookingId(), b.getCustomerId(), b.getPackageId(), b.getAdditionalOption(), b.getStatus(), b.getEventDate(), b.getTimeSlot());
        }
        return null;
    }

    @Override
    public ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT customer_id, name FROM customer");
        ObservableList<String> ids = FXCollections.observableArrayList();
        while (rst.next()) { ids.add(rst.getString(1) + " - " + rst.getString(2)); }
        return ids;
    }

    @Override
    public ObservableList<String> getAllPackageIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT package_id, name FROM package");
        ObservableList<String> ids = FXCollections.observableArrayList();
        while (rst.next()) { ids.add(rst.getString(1) + " - " + rst.getString(2)); }
        return ids;
    }

    @Override
    public ObservableList<String> getAllOptionNames() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT option_name FROM package_additional");
        ObservableList<String> options = FXCollections.observableArrayList();
        while (rst.next()) { options.add(rst.getString(1)); }
        return options;
    }
}
