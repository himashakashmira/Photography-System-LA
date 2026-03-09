package lk.ijse.photostudio.Model;

import lk.ijse.photostudio.DTO.AdminDTO;
import lk.ijse.photostudio.DAO.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {

    public static AdminDTO getAdminByUsername(String username) throws SQLException {

        ResultSet rs = CrudUtil.execute("SELECT username, password FROM admin WHERE username = ?", username).getResultSet();
        if (rs.next()) {
            return new AdminDTO(
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
        return null;
    }

    // Updates the password for a given username
    public static boolean updatePassword(String username, String newPassword) throws SQLException {

        return CrudUtil.execute("UPDATE admin SET password = ? WHERE username = ?", newPassword, username).getUpdateCount() > 0;
    }

    public static String getPasswordByEmail(String email) throws SQLException {
        String sql = "SELECT password FROM admin WHERE email = ?";
        ResultSet rs = CrudUtil.execute(sql, email).getResultSet();

        if (rs.next()) {
            return rs.getString("password");
        }
        return null;
    }
}