package lk.ijse.photostudio.DAO.Package.impl;

import lk.ijse.photostudio.DAO.Package.PackageDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Package;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageDAOimpl implements PackageDAO {
    @Override
    public ArrayList<Package> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM package ORDER BY package_id DESC");
        ArrayList<Package> allPackages = new ArrayList<>();
        while (rst.next()) {
            allPackages.add(new Package(rst.getString(1), rst.getString(2), rst.getString(5), rst.getString(4), rst.getDouble(3)));
        }
        return allPackages;
    }

    @Override
    public boolean save(Package entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO package (package_id, package_name, base_price, description, category) VALUES (?, ?, ?, ?, ?)",
                entity.getPackageId(), entity.getPackageName(), entity.getBasePrice(), entity.getPackageDescription(), entity.getCategory());
    }

    @Override
    public boolean update(Package entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE package SET package_name = ?, base_price = ?, description = ?, category = ? WHERE package_id = ?",
                entity.getPackageName(), entity.getBasePrice(), entity.getPackageDescription(), entity.getCategory(), entity.getPackageId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM package WHERE package_id = ?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT package_id FROM package ORDER BY package_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int idNum = Integer.parseInt(id.substring(1)) + 1;
            return String.format("P%03d", idNum);
        }
        return "P001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT package_id FROM package WHERE package_id=?", id).next();
    }

    @Override
    public Package search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM package WHERE package_id = ?", id);
        if (rst.next()) {
            return new Package(rst.getString(1), rst.getString(2), rst.getString(5), rst.getString(4), rst.getDouble(3));
        }
        return null;
    }
}
