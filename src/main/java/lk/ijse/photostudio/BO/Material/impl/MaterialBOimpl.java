package lk.ijse.photostudio.BO.Material.impl;

import lk.ijse.photostudio.BO.Material.MaterialBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.Material.MaterialDAO;
import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DTO.MaterialDTO;
import lk.ijse.photostudio.Entity.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialBOimpl implements MaterialBO {
    MaterialDAO materialDAO = (MaterialDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MATERIAL);

    @Override
    public ArrayList<MaterialDTO> getAllMaterials() throws SQLException, ClassNotFoundException {
        ArrayList<Material> entities = materialDAO.getAll();
        ArrayList<MaterialDTO> dtos = new ArrayList<>();
        for (Material m : entities) {
            dtos.add(new MaterialDTO(m.getMaterialId(), m.getMaterialName(), m.getQty(), m.getSupplierId()));
        }
        return dtos;
    }

    @Override
    public boolean saveMaterial(MaterialDTO dto) throws SQLException, ClassNotFoundException {
        return materialDAO.save(new Material(dto.getMaterialId(), dto.getMaterialName(), dto.getQty(), dto.getSupplierId()));
    }

    @Override
    public boolean updateMaterial(MaterialDTO dto) throws SQLException, ClassNotFoundException {
        return materialDAO.update(new Material(dto.getMaterialId(), dto.getMaterialName(), dto.getQty(), dto.getSupplierId()));
    }

    @Override
    public boolean deleteMaterial(String id) throws SQLException, ClassNotFoundException {
        return materialDAO.delete(id);
    }

    @Override
    public String generateNextMaterialId() throws SQLException, ClassNotFoundException {
        return materialDAO.generateNewID();
    }

    @Override
    public MaterialDTO searchMaterial(String id) throws SQLException, ClassNotFoundException {
        Material m = materialDAO.search(id);
        if (m != null) {
            return new MaterialDTO(m.getMaterialId(), m.getMaterialName(), m.getQty(), m.getSupplierId());
        }
        return null;
    }

    @Override
    public ArrayList<String> getAllSupplierIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT supplier_id FROM supplier");
        ArrayList<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }
}
