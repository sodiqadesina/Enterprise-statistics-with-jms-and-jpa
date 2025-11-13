package ec.stats.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatsDBDelete {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    public static void main(String[] args) throws Exception {
        Map arguments = parseArguments(args);

        if ("ecuser".equalsIgnoreCase((String) arguments.get("table"))) {
            deleteFromEcuser((String) arguments.get("name"));
        } else if ("ecmodel".equalsIgnoreCase((String) arguments.get("table"))) {
            deleteFromEcmodel((String) arguments.get("name"));
        } else {
            System.out.println("Invalid table name.");
        }
    }

    private static void deleteFromEcuser(String name) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM ecuser WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Deleted user: " + name);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static void deleteFromEcmodel(String name) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM ecmodel WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Deleted model: " + name);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static Map parseArguments(String[] args) {
        Map arguments = new HashMap();
        for (int i = 0; i < args.length; i += 2) {
            arguments.put(args[i].substring(1), args[i + 1]);
        }
        return arguments;
    }
}
