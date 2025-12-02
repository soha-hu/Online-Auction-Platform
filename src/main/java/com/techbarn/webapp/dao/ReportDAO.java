package com.techbarn.webapp.dao;

import com.techbarn.webapp.ApplicationDB;

import java.sql.*;
import java.util.*;

public class ReportDAO {

    public static double totalEarnings() {
        String sql = "SELECT COALESCE(SUM(t.amount),0) as total FROM `Transaction` t";
        // Note: your Transaction table doesn't have amount column in the schema. Try to sum winning bids as fallback.
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) { /* ignore */ }

        // fallback: sum highest bid per closed auction
        String sql2 = "SELECT COALESCE(SUM(x.max_bid),0) AS total FROM ( " +
                "SELECT a.auction_id, MAX(b.amount) AS max_bid " +
                "FROM Auction a JOIN Bid b ON a.auction_id = b.auction_id " +
                "WHERE a.status = 'closed' GROUP BY a.auction_id) x";
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public static List<Map<String,Object>> earningsByItem() {
        String sql = "SELECT i.item_id, i.title, COALESCE(SUM(b.amount),0) AS earnings " +
                "FROM Auction a JOIN Item i ON a.item_id = i.item_id " +
                "LEFT JOIN Bid b ON b.auction_id = a.auction_id " +
                "WHERE a.status = 'closed' GROUP BY i.item_id, i.title ORDER BY earnings DESC";
        return runList(sql);
    }

    public static List<Map<String,Object>> earningsBySeller() {
        String sql = "SELECT u.user_id, CONCAT(u.first_name,' ',u.last_name) AS seller, COALESCE(SUM(b.amount),0) AS earnings " +
                "FROM Auction a JOIN `User` u ON a.seller_id = u.user_id LEFT JOIN Bid b ON b.auction_id = a.auction_id " +
                "WHERE a.status='closed' GROUP BY u.user_id, seller ORDER BY earnings DESC";
        return runList(sql);
    }

    public static List<Map<String,Object>> bestSellingItems() {
        String sql = "SELECT i.item_id, i.title, COUNT(t.trans_id) AS sold_count " +
                "FROM `Transaction` t JOIN Auction a ON t.auction_id = a.auction_id JOIN Item i ON a.item_id = i.item_id " +
                "GROUP BY i.item_id, i.title ORDER BY sold_count DESC";
        return runList(sql);
    }

    private static List<Map<String,Object>> runList(String sql) {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c = ApplicationDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String,Object> row = new LinkedHashMap<>();
                for (int i=1;i<=md.getColumnCount();i++) row.put(md.getColumnName(i), rs.getObject(i));
                out.add(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }
}

