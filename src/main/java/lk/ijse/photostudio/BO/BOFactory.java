package lk.ijse.photostudio.BO;

import lk.ijse.photostudio.BO.Booking.impl.BookingBOimpl;
import lk.ijse.photostudio.BO.Custom.impl.CustomerBOimpl;
import lk.ijse.photostudio.BO.Material.impl.MaterialBOimpl;
import lk.ijse.photostudio.BO.PackageAdditional.impl.PackageAdditionalBOimpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMER, BOOKING, MATERIAL, PACKAGE, PACKAGEADDITIONAL, SUPPLIER, ADMIN
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
            default:
                return null;
        }
    }
}
