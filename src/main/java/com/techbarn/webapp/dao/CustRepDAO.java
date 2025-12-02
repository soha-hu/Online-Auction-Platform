package com.techbarn.webapp.dao;

import com.techbarn.webapp.ApplicationDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustRepDAO {

    public static boolean createRep(String firstName, String lastName, String username, String password, String email, int adminId) {
        String sql = "INSERT INTO Cust_Rep (first_name, last_name, username, password, email, admin_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, email);
            ps.setInt(6, adminId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public static boolean existsByUsername(String username) {
        String sql = "SELECT rep_id FROM Cust_Rep WHERE username = ?";
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}

