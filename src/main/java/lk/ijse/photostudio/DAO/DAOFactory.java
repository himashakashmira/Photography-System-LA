package lk.ijse.photostudio.DAO;

import lk.ijse.photostudio.DAO.Admin.impl.AdminDAOimpl;
import lk.ijse.photostudio.DAO.Booking.impl.BookingDAOimpl;
import lk.ijse.photostudio.DAO.Customer.Impl.CustomerDAOImpl;
import lk.ijse.photostudio.DAO.Material.impl.MaterialDAOimpl;
import lk.ijse.photostudio.DAO.Order.impl.OrderDAOimpl;
import lk.ijse.photostudio.DAO.Package.impl.PackageDAOimpl;
import lk.ijse.photostudio.DAO.PackageAdditional.Impl.PackageAdditionalDAOImpl;
import lk.ijse.photostudio.DAO.QueryDAO.impl.QueryDAOimpl;
import lk.ijse.photostudio.DAO.Supplier.impl.SupplierDAOimpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMER, BOOKING, MATERIAL, PACKAGE, PACKAGEADDITIONAL, SUPPLIER, ORDER, ADMIN, QUERY
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
            case SUPPLIER:
                return new SupplierDAOimpl();
            case PACKAGE:
                return  new PackageDAOimpl();
            case ORDER:
                return new OrderDAOimpl();
            case ADMIN:
                return new AdminDAOimpl();
            case QUERY:
                return  new QueryDAOimpl();
            default:
                return null;
        }
    }
}