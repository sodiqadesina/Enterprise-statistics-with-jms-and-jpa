package ec.stats.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatsDBDrop {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();

            // Drop ecuser table
            String dropEcuser = "DROP TABLE IF EXISTS ecuser";
            stmt = conn.prepareStatement(dropEcuser);
            stmt.executeUpdate();

            // Drop ecmodel table
            String dropEcmodel = "DROP TABLE IF EXISTS ecmodel";
            stmt = conn.prepareStatement(dropEcmodel);
            stmt.executeUpdate();

            System.out.println("Tables dropped successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
