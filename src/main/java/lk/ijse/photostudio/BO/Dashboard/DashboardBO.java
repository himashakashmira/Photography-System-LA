package lk.ijse.photostudio.BO.Dashboard;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.BookingDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DashboardBO extends SuperBO {
    ArrayList<BookingDTO> getTodaysBookings() throws SQLException, ClassNotFoundException;

    int getTotalBookings() throws SQLException, ClassNotFoundException;

    int getPendingBookings() throws SQLException, ClassNotFoundException;

    double getTotalIncome() throws SQLException, ClassNotFoundException;
}
