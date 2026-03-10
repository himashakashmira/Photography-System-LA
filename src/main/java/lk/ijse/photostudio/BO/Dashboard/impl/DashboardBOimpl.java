package lk.ijse.photostudio.BO.Dashboard.impl;

import lk.ijse.photostudio.BO.Dashboard.DashboardBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.QueryDAO.QueryDAO;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.Entity.Booking;

import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardBOimpl implements DashboardBO {
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public ArrayList<BookingDTO> getTodaysBookings() throws SQLException, ClassNotFoundException {
        ArrayList<Booking> entities = queryDAO.getTodaysPendingBookings();
        ArrayList<BookingDTO> dtos = new ArrayList<>();
        for (Booking b : entities) {
            dtos.add(new BookingDTO(b.getBookingId(), b.getCustomerId(), b.getPackageId(), b.getAdditionalOption(), b.getStatus(), b.getEventDate(), b.getTimeSlot()));
        }
        return dtos;
    }

    @Override
    public int getTotalBookings() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalBookingsCount();
    }

    @Override
    public int getPendingBookings() throws SQLException, ClassNotFoundException {
        return queryDAO.getPendingBookingsCount();
    }

    @Override
    public double getTotalIncome() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalIncome();
    }
}
