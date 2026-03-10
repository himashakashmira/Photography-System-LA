package lk.ijse.photostudio.DAO.Admin.impl;

import lk.ijse.photostudio.DAO.Admin.AdminDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAOimpl implements AdminDAO {
    @Override
    public Admin getAdminByUsername(String username) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.executeQuery("SELECT username, password FROM admin WHERE username = ?", username);
        if (rs.next()) return new Admin(rs.getString(1), rs.getString(2));
        return null;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE admin SET password = ? WHERE username = ?", newPassword, username);
    }

    @Override
    public ArrayList<Admin> getAll() {
        return null;
    }

    @Override
    public boolean save(Admin entity) {
        return false;
    }

    @Override
    public boolean update(Admin entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public String generateNewID() {
        return null;
    }

    @Override
    public boolean exist(String id) {
        return false;
    }

    @Override
    public Admin search(String id) {
        return null;
    }
}
