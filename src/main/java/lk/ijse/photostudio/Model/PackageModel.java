package lk.ijse.photostudio.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.photostudio.DBConnection.DBConnection;
import lk.ijse.photostudio.DTO.PackageDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageModel {

    public boolean savePackage(PackageDTO packageDTO) throws SQLException {

        return CrudUtil.execute("INSERT INTO package (package_id, package_name, base_price, description, category) VALUES (?, ?, ?, ?, ?)",

                packageDTO.getPackageId(),
                packageDTO.getPackageName(),
                packageDTO.getBasePrice(),
                packageDTO.getPackageDescription(),
                packageDTO.getCategory()
        ).getUpdateCount() > 0;

    }

    public boolean updatePackage(PackageDTO packageDTO) throws SQLException {

        return CrudUtil.execute("UPDATE package SET package_name = ?, base_price = ?, description = ?, category = ? WHERE package_id = ?",

                packageDTO.getPackageName(),
                packageDTO.getBasePrice(),
                packageDTO.getPackageDescription(),
                packageDTO.getCategory(),
                packageDTO.getPackageId()
        ).getUpdateCount() > 0;

    }

    public boolean deletePackage(String id) throws SQLException {

        return CrudUtil.execute("DELETE FROM package WHERE package_id = ?", id
        ).getUpdateCount() > 0;

    }

    public PackageDTO searchPackage(String id) throws SQLException {

        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM package WHERE package_id = ?", id);

        if (rs.next()) {
            return new PackageDTO(
                    rs.getString("package_id"),
                    rs.getString("package_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("base_price"));

        }

        return null;
    }

    public ObservableList<PackageDTO> getAllPackages() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT * FROM package ORDER BY package_id DESC ";

        PreparedStatement pstm = conn.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();

        ObservableList<PackageDTO> packageList = FXCollections.observableArrayList();

        while (rs.next()) {
            PackageDTO packageDTO = new PackageDTO(
                    rs.getString("package_id"),
                    rs.getString("package_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("base_price")
            );

            packageList.add(packageDTO);
        }
        return packageList;
    }

    public String getNextPackageId() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT package_id FROM package ORDER BY package_id DESC LIMIT 1";

        PreparedStatement pstm = conn.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            String id = rs.getString(1);
            int idNum = Integer.parseInt(id.substring(1)) + 1;
            return String.format("P%03d", idNum);
        }
        return "P001";
    }
}




