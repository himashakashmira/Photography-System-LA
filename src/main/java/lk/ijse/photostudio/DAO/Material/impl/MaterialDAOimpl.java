package lk.ijse.photostudio.DAO.Material.impl;

import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DAO.Material.MaterialDAO;
import lk.ijse.photostudio.Entity.Material;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialDAOimpl implements MaterialDAO {
    @Override
    public ArrayList<Material> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM material ORDER BY material_id DESC");
        ArrayList<Material> allMaterials = new ArrayList<>();
        while (rst.next()) {
            allMaterials.add(new Material(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getString(4)));
        }
        return allMaterials;
    }

    @Override
    public boolean save(Material entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO material (material_id, material_type, quantity, supplier_id) VALUES (?, ?, ?, ?)",
                entity.getMaterialId(), entity.getMaterialName(), entity.getQty(), entity.getSupplierId());
    }

    @Override
    public boolean update(Material entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE material SET material_type = ?, quantity = ?, supplier_id = ? WHERE material_id = ?",
                entity.getMaterialName(), entity.getQty(), entity.getSupplierId(), entity.getMaterialId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM material WHERE material_id = ?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT material_id FROM material ORDER BY material_id DESC LIMIT 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            String numericPart = lastId.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numericPart) + 1;
            return String.format("MAT%03d", num);
        }
        return "MAT001";
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT material_id FROM material WHERE material_id=?", id).next();
    }

    @Override
    public Material search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM material WHERE material_id = ?", id);
        if (rst.next()) {
            return new Material(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getString(4));
        }
        return null;
    }
}
