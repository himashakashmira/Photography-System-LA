package lk.ijse.photostudio.BO.Booking;

import javafx.collections.ObservableList;
import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.BookingDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BookingBO extends SuperBO {
    ArrayList<BookingDTO> getAllBookings() throws SQLException, ClassNotFoundException;

    boolean saveBooking(BookingDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateBooking(BookingDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteBooking(String id) throws SQLException, ClassNotFoundException;

    String generateNextBookingId() throws SQLException, ClassNotFoundException;

    BookingDTO searchBooking(String id) throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllPackageIds() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllOptionNames() throws SQLException, ClassNotFoundException;
}
