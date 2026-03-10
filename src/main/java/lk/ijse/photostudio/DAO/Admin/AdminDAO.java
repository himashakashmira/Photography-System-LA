package lk.ijse.photostudio.DAO.Admin;

import lk.ijse.photostudio.DAO.CrudDAO;
import lk.ijse.photostudio.Entity.Admin;

import java.sql.SQLException;

public interface AdminDAO extends CrudDAO<Admin> {
    Admin getAdminByUsername(String username) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException;
}
