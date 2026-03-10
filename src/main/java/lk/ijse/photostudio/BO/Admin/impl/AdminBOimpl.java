package lk.ijse.photostudio.BO.Admin.impl;

import lk.ijse.photostudio.BO.Admin.AdminBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.Admin.AdminDAO;
import lk.ijse.photostudio.Entity.Admin;

import java.sql.SQLException;

public class AdminBOimpl implements AdminBO {
    AdminDAO adminDAO = (AdminDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ADMIN);

    @Override
    public boolean authenticate(String username, String password) throws SQLException, ClassNotFoundException {
        Admin admin = adminDAO.getAdminByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        return adminDAO.updatePassword(username, newPassword);
    }
}
