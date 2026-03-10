package lk.ijse.photostudio.BO;

import lk.ijse.photostudio.BO.Admin.impl.AdminBOimpl;
import lk.ijse.photostudio.BO.Booking.impl.BookingBOimpl;
import lk.ijse.photostudio.BO.Customer.impl.CustomerBOimpl;
import lk.ijse.photostudio.BO.Dashboard.impl.DashboardBOimpl;
import lk.ijse.photostudio.BO.Material.impl.MaterialBOimpl;
import lk.ijse.photostudio.BO.Order.impl.OrderBOimpl;
import lk.ijse.photostudio.BO.Package.impl.PackageBOimpl;
import lk.ijse.photostudio.BO.PackageAdditional.impl.PackageAdditionalBOimpl;
import lk.ijse.photostudio.BO.Report.impl.ReportBOimpl;
import lk.ijse.photostudio.BO.Supplier.impl.SupplierBOimpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMER, BOOKING, MATERIAL, PACKAGE, PACKAGEADDITIONAL, ORDER,  SUPPLIER, ADMIN, DASHBOARD, REPORT
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOimpl();
            case BOOKING:
                return new BookingBOimpl();
            case PACKAGEADDITIONAL:
                return new PackageAdditionalBOimpl();
            case MATERIAL:
                return new MaterialBOimpl();
            case SUPPLIER:
                return new SupplierBOimpl();
            case PACKAGE:
                return new PackageBOimpl();
            case ORDER:
                return  new OrderBOimpl();
            case ADMIN:
                return new AdminBOimpl();
            case DASHBOARD:
                return new DashboardBOimpl();
            case REPORT:
                return new ReportBOimpl();
            default:
                return null;
        }
    }
}
