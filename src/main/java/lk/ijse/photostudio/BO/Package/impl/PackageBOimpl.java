package lk.ijse.photostudio.BO.Package.impl;

import lk.ijse.photostudio.BO.Package.PackageBO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DAO.Package.PackageDAO;
import lk.ijse.photostudio.DTO.PackageDTO;
import lk.ijse.photostudio.Entity.Package;

import java.sql.SQLException;
import java.util.ArrayList;

public class PackageBOimpl implements PackageBO {
    PackageDAO packageDAO = (PackageDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PACKAGE);

    @Override
    public ArrayList<PackageDTO> getAllPackages() throws SQLException, ClassNotFoundException {
        ArrayList<Package> entities = packageDAO.getAll();
        ArrayList<PackageDTO> dtos = new ArrayList<>();
        for (Package p : entities) {
            dtos.add(new PackageDTO(p.getPackageId(), p.getPackageName(), p.getCategory(), p.getPackageDescription(), p.getBasePrice()));
        }
        return dtos;
    }

    @Override
    public boolean savePackage(PackageDTO dto) throws SQLException, ClassNotFoundException {
        return packageDAO.save(new Package(dto.getPackageId(), dto.getPackageName(), dto.getCategory(), dto.getPackageDescription(), dto.getBasePrice()));
    }

    @Override
    public boolean updatePackage(PackageDTO dto) throws SQLException, ClassNotFoundException {
        return packageDAO.update(new Package(dto.getPackageId(), dto.getPackageName(), dto.getCategory(), dto.getPackageDescription(), dto.getBasePrice()));
    }

    @Override
    public boolean deletePackage(String id) throws SQLException, ClassNotFoundException {
        return packageDAO.delete(id);
    }

    @Override
    public String generateNextPackageId() throws SQLException, ClassNotFoundException {
        return packageDAO.generateNewID();
    }

    @Override
    public PackageDTO searchPackage(String id) throws SQLException, ClassNotFoundException {
        Package p = packageDAO.search(id);
        if (p != null) {
            return new PackageDTO(p.getPackageId(), p.getPackageName(), p.getCategory(), p.getPackageDescription(), p.getBasePrice());
        }
        return null;
    }
}
