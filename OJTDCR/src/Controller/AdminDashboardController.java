package Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.DBConfig;

public class AdminDashboardController {
    private DBConfig dbConnection;

    public AdminDashboardController() {
        dbConnection = new DBConfig();
    }
    
    public List<Map<String, String>> getAdminData() throws SQLException {
        List<Map<String, String>> admins = new ArrayList<>();
        String query = "SELECT a.admName, d.depName " +
                       "FROM admin a " +
                       "JOIN department d ON a.dep_id = d.dep_id";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, String> admin = new HashMap<>();
                admin.put("name", rs.getString("admName"));
                admin.put("department", rs.getString("depName"));
                admins.add(admin);
            }
        }
        return admins;
    }

    public boolean deleteAdmin(String adminName) throws SQLException {
        String query = "DELETE FROM admin WHERE admName = '" + adminName + "'";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            return rowsAffected > 0;
        }
    }
}