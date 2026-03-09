package lk.ijse.photostudio.BO.PackageAdditional;

import lk.ijse.photostudio.BO.SuperBO;
import lk.ijse.photostudio.DTO.PackageAdditionalDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PackageAdditionalBO extends SuperBO {
    ArrayList<PackageAdditionalDTO> getAllOptions() throws SQLException, ClassNotFoundException;

    boolean saveOption(PackageAdditionalDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateOption(PackageAdditionalDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteOption(String id) throws SQLException, ClassNotFoundException;

    String generateNextOptionId() throws SQLException, ClassNotFoundException;

    PackageAdditionalDTO searchOption(String id) throws SQLException, ClassNotFoundException;
}
