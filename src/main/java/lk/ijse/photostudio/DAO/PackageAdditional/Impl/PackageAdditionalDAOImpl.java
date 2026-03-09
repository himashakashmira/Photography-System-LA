package lk.ijse.photostudio.DAO.PackageAdditional.Impl;

import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DAO.PackageAdditional.PackageAdditionalDAO;
import lk.ijse.photostudio.Entity.PackageAdditional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageAdditionalDAOImpl implements PackageAdditionalDAO {
    @Override
    public ArrayList<PackageAdditional> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM package_additional");
        ArrayList<PackageAdditional> allOptions = new ArrayList<>();
        while (rst.next()) {
            allOptions.add(new PackageAdditional(rst.getString(1), rst.getString(2), rst.getDouble(3), rst.getString(4)));
        }
        return allOptions;
    }

    @Override
    public boolean save(PackageAdditional entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO package_additional VALUES (?,?,?,?)",
                entity.getOptionId(), entity.getOptionName(), entity.getPrice(), entity.getDescription());
    }

    @Override
    public boolean update(PackageAdditional entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE package_additional SET option_name=?, price=?, description=? WHERE option_id=?",
                entity.getOptionName(), entity.getPrice(), entity.getDescription(), entity.getOptionId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM package_additional WHERE option_id=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT option_id FROM package_additional ORDER BY option_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("OPT", "")) + 1;
            return String.format("OPT%03d", newId);
        }
        return "OPT001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT option_id FROM package_additional WHERE option_id=?", id);
        return rst.next();
    }

    @Override
    public PackageAdditional search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM package_additional WHERE option_id=?", id);
        if (rst.next()) {
            return new PackageAdditional(rst.getString(1), rst.getString(2), rst.getDouble(3), rst.getString(4));
        }
        return null;
    }
}
