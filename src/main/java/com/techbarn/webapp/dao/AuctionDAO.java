package com.techbarn.webapp.dao;

import com.techbarn.webapp.ApplicationDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AuctionDAO {

    public static boolean removeAuction(int auctionId) {
        Connection c = null;
        try {
            c = ApplicationDB.getConnection();
            c.setAutoCommit(false); // Start transaction
            
            // Delete related records first to handle foreign key constraints
            // Delete transactions for this auction
            PreparedStatement ps1 = c.prepareStatement("DELETE FROM `Transaction` WHERE auction_id = ?");
            ps1.setInt(1, auctionId);
            ps1.executeUpdate();
            ps1.close();
            
            // Delete all bids for this auction
            PreparedStatement ps2 = c.prepareStatement("DELETE FROM Bid WHERE auction_id = ?");
            ps2.setInt(1, auctionId);
            ps2.executeUpdate();
            ps2.close();
            
            // Finally delete the auction
            PreparedStatement ps3 = c.prepareStatement("DELETE FROM Auction WHERE auction_id = ?");
            ps3.setInt(1, auctionId);
            int result = ps3.executeUpdate();
            ps3.close();
            
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
}
