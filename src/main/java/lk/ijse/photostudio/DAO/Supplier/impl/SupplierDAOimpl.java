package lk.ijse.photostudio.DAO.Supplier.impl;

import lk.ijse.photostudio.DAO.Supplier.SupplierDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.Entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAOimpl implements SupplierDAO {
    @Override
    public ArrayList<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM supplier ORDER BY supplier_id DESC");
        ArrayList<Supplier> allSuppliers = new ArrayList<>();
        while (rst.next()) {
            allSuppliers.add(new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4)));
        }
        return allSuppliers;
    }

    @Override
    public boolean save(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplier (supplier_id, name, material_type, contact) VALUES (?, ?, ?, ?)",
                entity.getSupplierId(), entity.getSupplierName(), entity.getMaterialType(), entity.getSupplierContact());
    }

    @Override
    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE supplier SET name = ?, material_type = ?, contact = ? WHERE supplier_id = ?",
                entity.getSupplierName(), entity.getMaterialType(), entity.getSupplierContact(), entity.getSupplierId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            int num = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("S%03d", num);
        }
        return "S001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT supplier_id FROM supplier WHERE supplier_id=?", id).next();
    }

    @Override
    public Supplier search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM supplier WHERE supplier_id = ?", id);
        if (rst.next()) {
            return new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4));
        }
        return null;
    }
}
