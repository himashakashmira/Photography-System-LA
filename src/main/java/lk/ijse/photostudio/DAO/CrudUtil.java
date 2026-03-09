package lk.ijse.photostudio.DAO;

import lk.ijse.photostudio.DBConnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    private static PreparedStatement prepare(String sql, Object... params) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    public static boolean execute(String sql, Object... params) throws SQLException {
        return prepare(sql, params).executeUpdate() > 0;
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        return prepare(sql, params).executeQuery();
    }
}
