package lk.ijse.photostudio.BO.Admin;

import lk.ijse.photostudio.BO.SuperBO;

import java.sql.SQLException;

public interface AdminBO extends SuperBO {
    boolean authenticate(String username, String password) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException;
}
