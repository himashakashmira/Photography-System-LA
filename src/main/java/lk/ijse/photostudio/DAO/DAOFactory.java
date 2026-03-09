package lk.ijse.photostudio.DAO;

import lk.ijse.photostudio.DAO.Booking.impl.BookingDAOimpl;
import lk.ijse.photostudio.DAO.Customer.Impl.CustomerDAOImpl;
import lk.ijse.photostudio.DAO.Material.impl.MaterialDAOimpl;
import lk.ijse.photostudio.DAO.PackageAdditional.Impl.PackageAdditionalDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMER, BOOKING, MATERIAL, PACKAGE, PACKAGEADDITIONAL, SUPPLIER, ADMIN
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case BOOKING:
                return new BookingDAOimpl();
            case PACKAGEADDITIONAL:
                return new PackageAdditionalDAOImpl();
            case MATERIAL:
                return new MaterialDAOimpl();
            default:
                return null;
        }
    }
}