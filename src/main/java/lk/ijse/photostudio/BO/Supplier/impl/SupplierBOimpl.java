package lk.ijse.photostudio.BO.Supplier.impl;

import lk.ijse.photostudio.BO.Supplier.SupplierBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.Supplier.SupplierDAO;
import lk.ijse.photostudio.DTO.SupplierDTO;
import lk.ijse.photostudio.Entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOimpl implements SupplierBO {
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> entities = supplierDAO.getAll();
        ArrayList<SupplierDTO> dtos = new ArrayList<>();
        for (Supplier s : entities) {
            dtos.add(new SupplierDTO(s.getSupplierId(), s.getSupplierName(), s.getMaterialType(), s.getSupplierContact()));
        }
        return dtos;
    }

    @Override
    public boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(dto.getSupplierId(), dto.getSupplierName(), dto.getMaterialType(), dto.getSupplierContact()));
    }

    @Override
    public boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(dto.getSupplierId(), dto.getSupplierName(), dto.getMaterialType(), dto.getSupplierContact()));
    }

    @Override
    public boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    @Override
    public String generateNextSupplierId() throws SQLException, ClassNotFoundException {
        return supplierDAO.generateNewID();
    }

    @Override
    public SupplierDTO searchSupplier(String id) throws SQLException, ClassNotFoundException {
        Supplier s = supplierDAO.search(id);
        if (s != null) {
            return new SupplierDTO(s.getSupplierId(), s.getSupplierName(), s.getMaterialType(), s.getSupplierContact());
        }
        return null;
    }
}
