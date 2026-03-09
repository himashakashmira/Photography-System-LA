package lk.ijse.photostudio.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.SupplierDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierModel {
    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {

        return CrudUtil.execute("INSERT INTO supplier (supplier_id, name, material_type, contact) VALUES (?, ?, ?, ?)",
                supplierDTO.getSupplierId(),
                supplierDTO.getSupplierName(),
                supplierDTO.getMaterialType(),
                supplierDTO.getSupplierContact()
        );
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {

        return CrudUtil.execute("UPDATE supplier SET name = ?, material_type = ?, contact = ? WHERE supplier_id = ?",
                supplierDTO.getSupplierName(),
                supplierDTO.getMaterialType(),
                supplierDTO.getSupplierContact(),
                supplierDTO.getSupplierId()
        );

    }

    public boolean deleteSupplier(String id) throws SQLException {

        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", id
        );

    }

    public SupplierDTO searchSupplier(String id) throws SQLException {

        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM supplier WHERE supplier_id = ?", id);

        if (rs.next()) {
            return new SupplierDTO(
            rs.getString("supplier_id"),
            rs.getString("name"),
            rs.getString("material_type"),
            rs.getString("contact")
            );

        }

        return null;
    }

    public ObservableList<SupplierDTO> getSuppliers() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier ORDER BY supplier_id DESC";
        PreparedStatement ptsm = conn.prepareStatement(sql);

        ResultSet rs = ptsm.executeQuery();

        ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList();

        while (rs.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rs.getString("supplier_id"),
                    rs.getString("name"),
                    rs.getString("material_type"),
                    rs.getString("contact")
            );

            supplierList.add(supplierDTO);
        }

        return supplierList;
    }

    public String getNextSupplierId() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1";

        PreparedStatement ptsm = conn.prepareStatement(sql);
        ResultSet rs = ptsm.executeQuery();
        if (rs.next()) {
            String lastId = rs.getString(1);
            int num = Integer.parseInt(lastId.substring(1)) + 1;

            return String.format("S%03d", num);
        }
        return "S001";
    }
}
