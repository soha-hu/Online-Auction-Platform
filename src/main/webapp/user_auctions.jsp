<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<%
    String username = (String) request.getAttribute("username");
    Integer userId = (Integer) request.getAttribute("userId");
    List<Map<String, Object>> auctions = (List<Map<String, Object>>) request.getAttribute("auctions");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>User Auctions - Tech Barn</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700&display=swap" rel="stylesheet">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }
    
    body {
      font-family: 'Roboto', sans-serif;
      background: #f5f7fb;
      min-height: 100vh;
      padding-bottom: 40px;
    }
    
    .page-container {
      max-width: 1200px;
      margin: 40px auto;
      padding: 0 20px;
    }
    
    .page-header {
      background: linear-gradient(135deg, #ffffff, #f8f9fa);
      border-radius: 16px;
      padding: 32px 40px;
      margin-bottom: 30px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #e5e7eb;
    }
    
    h1 {
      font-size: 28px;
      font-weight: 700;
      color: #111827;
      margin-bottom: 8px;
      letter-spacing: -0.02em;
    }
    
    .subtitle {
      color: #6b7280;
      font-size: 15px;
      font-weight: 400;
      margin: 0;
    }
    
    .error {
      background: linear-gradient(135deg, #fee2e2, #fef2f2);
      border-left: 4px solid #ef4444;
      color: #991b1b;
      padding: 16px 20px;
      border-radius: 12px;
      margin-bottom: 24px;
      box-shadow: 0 2px 8px rgba(239, 68, 68, 0.1);
      font-weight: 500;
    }
    
    .empty {
      background: #ffffff;
      text-align: center;
      padding: 60px 40px;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #e5e7eb;
    }
    
    .empty p {
      color: #6b7280;
      font-size: 16px;
      margin: 0;
    }
    
    .table-container {
      background: #ffffff;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }
    
    table {
      width: 100%;
      border-collapse: collapse;
    }
    
    thead {
      background: linear-gradient(135deg, #f9fafb, #f3f4f6);
    }
    
    th {
      padding: 16px 20px;
      text-align: left;
      font-weight: 600;
      font-size: 13px;
      color: #374151;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      border-bottom: 2px solid #e5e7eb;
    }
    
    td {
      padding: 18px 20px;
      border-bottom: 1px solid #f3f4f6;
      color: #111827;
      font-size: 14px;
    }
    
    tbody tr {
      transition: all 0.2s ease;
    }
    
    tbody tr:hover {
      background-color: #f9fafb;
      transform: translateX(2px);
    }
    
    tbody tr:last-child td {
      border-bottom: none;
    }
    
    .auction-id {
      font-weight: 600;
      color: #6b9080;
      font-size: 13px;
    }
    
    .item-title {
      font-weight: 600;
      color: #111827;
    }
    
    .item-brand {
      color: #6b7280;
      font-size: 13px;
      margin-top: 4px;
    }
    
    .role {
      display: inline-block;
      padding: 6px 12px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: 0.02em;
      text-transform: uppercase;
    }
    
    .role-seller {
      background: linear-gradient(135deg, #dbeafe, #bfdbfe);
      color: #1e40af;
      box-shadow: 0 2px 4px rgba(30, 64, 175, 0.1);
    }
    
    .role-buyer {
      background: linear-gradient(135deg, #dcfce7, #bbf7d0);
      color: #166534;
      box-shadow: 0 2px 4px rgba(22, 101, 52, 0.1);
    }
    
    .status {
      display: inline-block;
      padding: 6px 12px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: 0.02em;
      text-transform: uppercase;
    }
    
    .status-active {
      background: linear-gradient(135deg, #dcfce7, #bbf7d0);
      color: #166534;
      box-shadow: 0 2px 4px rgba(22, 101, 52, 0.1);
    }
    
    .status-closed {
      background: linear-gradient(135deg, #fee2e2, #fecaca);
      color: #991b1b;
      box-shadow: 0 2px 4px rgba(153, 27, 27, 0.1);
    }
    
    .price {
      font-weight: 700;
      color: #6b9080;
      font-size: 15px;
    }
    
    .end-time {
      color: #6b7280;
      font-size: 13px;
    }
    
    .view-link {
      display: inline-block;
      padding: 8px 16px;
      background: linear-gradient(135deg, #6b9080, #5a7a6b);
      color: #ffffff;
      text-decoration: none;
      border-radius: 8px;
      font-weight: 600;
      font-size: 13px;
      transition: all 0.2s ease;
      box-shadow: 0 2px 8px rgba(107, 144, 128, 0.25);
    }
    
    .view-link:hover {
      background: linear-gradient(135deg, #5a7a6b, #4a6a5b);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(107, 144, 128, 0.35);
      text-decoration: none;
    }
    
    .view-link:active {
      transform: translateY(0);
    }
    
    @media (max-width: 768px) {
      .page-container {
        margin: 20px auto;
        padding: 0 16px;
      }
      
      .page-header {
        padding: 24px 20px;
      }
      
      h1 {
        font-size: 24px;
      }
      
      .table-container {
        overflow-x: auto;
      }
      
      table {
        min-width: 800px;
      }
      
      th, td {
        padding: 12px 16px;
        font-size: 13px;
      }
    }
  </style>
</head>
<body>
  <%@ include file="navbar.jsp" %>
  <div class="page-container">
    <div class="page-header">
      <h1><%= username != null ? username + "'s Auctions" : "User Auctions" %></h1>
      <p class="subtitle">List of auctions started and auctions bid on</p>
    </div>

    <% if (errorMessage != null) { %>
      <div class="error"><%= errorMessage %></div>
    <% } %>

    <% if (auctions == null || auctions.isEmpty()) { %>
      <div class="empty">
        <p>No auctions found. This user hasn't started any auctions or placed any bids yet.</p>
      </div>
    <% } else { %>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>Auction ID</th>
              <th>Item</th>
              <th>Brand</th>
              <th>Role</th>
              <th>Status</th>
              <th>Current Price</th>
              <th>End Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <% for (Map<String, Object> auction : auctions) { 
                String role = (String) auction.get("role");
                String status = (String) auction.get("status");
            %>
              <tr>
                <td>
                  <span class="auction-id">#<%= auction.get("auction_id") %></span>
                </td>
                <td>
                  <div class="item-title"><%= auction.get("title") %></div>
                </td>
                <td>
                  <span class="item-brand"><%= auction.get("brand") %></span>
                </td>
                <td>
                  <span class="role <%= "seller".equals(role) ? "role-seller" : "role-buyer" %>">
                    <%= "seller".equals(role) ? "Started" : "Bid On" %>
                  </span>
                </td>
                <td>
                  <span class="status <%= "ACTIVE".equals(status) ? "status-active" : "status-closed" %>">
                    <%= status != null ? status : "UNKNOWN" %>
                  </span>
                </td>
                <td>
                  <span class="price">
                    <% if (auction.get("current_price") != null) { %>
                      $<%= auction.get("current_price") %>
                    <% } else { %>
                      $<%= auction.get("starting_price") %>
                    <% } %>
                  </span>
                </td>
                <td>
                  <span class="end-time"><%= auction.get("end_time") %></span>
                </td>
                <td>
                  <a href="Buyer_View_Auction_Page.jsp?auctionId=<%= auction.get("auction_id") %>" class="view-link">View</a>
                </td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    <% } %>
  </div>
</body>
</html>
