package ec.stats.db;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatsDBInsert {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    public static void main(String[] args) throws Exception {
        Map arguments = parseArguments(args);

        if ("ecuser".equalsIgnoreCase((String) arguments.get("table"))) {
            insertIntoEcuser((String) arguments.get("name"), (String) arguments.get("password"), (String) arguments.get("role"));
        } else if ("ecmodel".equalsIgnoreCase((String) arguments.get("table"))) {
            insertIntoEcmodel((String) arguments.get("name"), (String) arguments.get("modelfile"));
        } else {
            System.out.println("Invalid table name.");
        }
    }

    private static void insertIntoEcuser(String name, String password, String role) throws Exception {
        String encryptedPassword = digestMD5(password);
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO ecuser (name, password, role) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, encryptedPassword);
            stmt.setInt(3, Integer.parseInt(role));
            stmt.executeUpdate();
            System.out.println("Inserted user: " + name);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static void insertIntoEcmodel(String name, String modelfile) throws Exception {
        byte[] objectData = readFileAsByteArray(modelfile);
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO ecmodel (name, object, classname, date) VALUES (?, ?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setBytes(2, objectData);
            stmt.setString(3, "StatsSummary");
            stmt.executeUpdate();
            System.out.println("Inserted model: " + name);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private static String digestMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(String.format("%02x", digest[i]));
        }
        return sb.toString();
    }

    private static byte[] readFileAsByteArray(String filepath) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } finally {
            if (fis != null) fis.close();
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
