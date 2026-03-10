package lk.ijse.photostudio.BO.Supplier;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO extends SuperBO {
    ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException;

    boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException;

    String generateNextSupplierId() throws SQLException, ClassNotFoundException;

    SupplierDTO searchSupplier(String id) throws SQLException, ClassNotFoundException;
}
