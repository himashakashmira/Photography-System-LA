package lk.ijse.photostudio.BO.Material;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.MaterialDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MaterialBO extends SuperBO {
    ArrayList<MaterialDTO> getAllMaterials() throws SQLException, ClassNotFoundException;

    boolean saveMaterial(MaterialDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateMaterial(MaterialDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteMaterial(String id) throws SQLException, ClassNotFoundException;

    String generateNextMaterialId() throws SQLException, ClassNotFoundException;

    MaterialDTO searchMaterial(String id) throws SQLException, ClassNotFoundException;

    ArrayList<String> getAllSupplierIds() throws SQLException, ClassNotFoundException;
}
