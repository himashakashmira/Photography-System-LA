package lk.ijse.photostudio.BO.Package;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.PackageDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PackageBO extends SuperBO {
    ArrayList<PackageDTO> getAllPackages() throws SQLException, ClassNotFoundException;
    boolean savePackage(PackageDTO dto) throws SQLException, ClassNotFoundException;
    boolean updatePackage(PackageDTO dto) throws SQLException, ClassNotFoundException;
    boolean deletePackage(String id) throws SQLException, ClassNotFoundException;
    String generateNextPackageId() throws SQLException, ClassNotFoundException;
    PackageDTO searchPackage(String id) throws SQLException, ClassNotFoundException;
}
