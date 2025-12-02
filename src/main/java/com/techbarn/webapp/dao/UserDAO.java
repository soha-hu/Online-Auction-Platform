package com.techbarn.webapp.dao;

import com.techbarn.webapp.ApplicationDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAO {

    public static boolean deleteUser(int userId) {
        Connection c = null;
        try {
            c = ApplicationDB.getConnection();
            
            // First check if user exists
            PreparedStatement checkPs = c.prepareStatement("SELECT COUNT(*) FROM `User` WHERE user_id = ?");
            checkPs.setInt(1, userId);
            java.sql.ResultSet rs = checkPs.executeQuery();
            rs.next();
            int userCount = rs.getInt(1);
            rs.close();
            checkPs.close();
            
            if (userCount == 0) {
                return false; // User doesn't exist
            }
            
            c.setAutoCommit(false); // Start transaction
            
            // Delete related records first to handle foreign key constraints
            // Delete user's questions
            PreparedStatement ps1 = c.prepareStatement("DELETE FROM Question WHERE user_id = ?");
            ps1.setInt(1, userId);
            ps1.executeUpdate();
            ps1.close();
            
            // Delete user's bids
            PreparedStatement ps2 = c.prepareStatement("DELETE FROM Bid WHERE user_id = ?");
            ps2.setInt(1, userId);
            ps2.executeUpdate();
            ps2.close();
            
            // Delete auctions where user is seller (and related bids/transactions)
            PreparedStatement ps3 = c.prepareStatement("DELETE FROM `Transaction` WHERE auction_id IN (SELECT auction_id FROM Auction WHERE seller_id = ?)");
            ps3.setInt(1, userId);
            ps3.executeUpdate();
            ps3.close();
            
            PreparedStatement ps4 = c.prepareStatement("DELETE FROM Bid WHERE auction_id IN (SELECT auction_id FROM Auction WHERE seller_id = ?)");
            ps4.setInt(1, userId);
            ps4.executeUpdate();
            ps4.close();
            
            PreparedStatement ps5 = c.prepareStatement("DELETE FROM Auction WHERE seller_id = ?");
            ps5.setInt(1, userId);
            ps5.executeUpdate();
            ps5.close();
            
            // Finally delete the user
            PreparedStatement ps6 = c.prepareStatement("DELETE FROM `User` WHERE user_id = ?");
            ps6.setInt(1, userId);
            int result = ps6.executeUpdate();
            ps6.close();
            
            c.commit(); // Commit transaction
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                try { c.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (c != null) {
                try { c.setAutoCommit(true); c.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    public static boolean updateEmail(int userId, String email) {
        String sql = "UPDATE `User` SET email = ? WHERE user_id = ?";
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
