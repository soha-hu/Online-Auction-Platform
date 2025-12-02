package com.techbarn.webapp.dao;

import com.techbarn.webapp.ApplicationDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BidDAO {

    // remove by composite key (bid_no, auction_id)
    public static boolean removeBid(int bidNo, int auctionId) {
        String sql = "DELETE FROM Bid WHERE bid_no = ? AND auction_id = ?";
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bidNo);
            ps.setInt(2, auctionId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
