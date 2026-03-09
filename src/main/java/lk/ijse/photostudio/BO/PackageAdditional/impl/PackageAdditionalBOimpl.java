package lk.ijse.photostudio.BO.PackageAdditional.impl;

import lk.ijse.photostudio.BO.PackageAdditional.PackageAdditionalBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.PackageAdditional.PackageAdditionalDAO;
import lk.ijse.photostudio.DTO.PackageAdditionalDTO;
import lk.ijse.photostudio.Entity.PackageAdditional;

import java.sql.SQLException;
import java.util.ArrayList;

public class PackageAdditionalBOimpl implements PackageAdditionalBO {
    PackageAdditionalDAO additionalDAO = (PackageAdditionalDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PACKAGE);

    @Override
    public ArrayList<PackageAdditionalDTO> getAllOptions() throws SQLException, ClassNotFoundException {
        ArrayList<PackageAdditional> entities = additionalDAO.getAll();
        ArrayList<PackageAdditionalDTO> dtos = new ArrayList<>();
        for (PackageAdditional e : entities) {
            dtos.add(new PackageAdditionalDTO(e.getOptionId(), e.getOptionName(), e.getPrice(), e.getDescription()));
        }
        return dtos;
    }

    @Override
    public boolean saveOption(PackageAdditionalDTO dto) throws SQLException, ClassNotFoundException {
        return additionalDAO.save(new PackageAdditional(dto.getOptionId(), dto.getOptionName(), dto.getPrice(), dto.getDescription()));
    }

    @Override
    public boolean updateOption(PackageAdditionalDTO dto) throws SQLException, ClassNotFoundException {
        return additionalDAO.update(new PackageAdditional(dto.getOptionId(), dto.getOptionName(), dto.getPrice(), dto.getDescription()));
    }

    @Override
    public boolean deleteOption(String id) throws SQLException, ClassNotFoundException {
        return additionalDAO.delete(id);
    }

    @Override
    public String generateNextOptionId() throws SQLException, ClassNotFoundException {
        return additionalDAO.generateNewID();
    }

    @Override
    public PackageAdditionalDTO searchOption(String id) throws SQLException, ClassNotFoundException {
        PackageAdditional e = additionalDAO.search(id);
        if (e != null) {
            return new PackageAdditionalDTO(e.getOptionId(), e.getOptionName(), e.getPrice(), e.getDescription());
        }
        return null;
    }
}
