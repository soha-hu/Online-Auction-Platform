package com.techbarn.webapp;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet ("/category")
public class CategoryServlet extends HttpServlet{
    //no need for post method, we only handle get request
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
            try{
                //Get the database connection
                Connection con = ApplicationDB.getConnection();

                //get the categoryId
                //Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
                //replaced with:
                Integer categoryId;
                try {
                    String categoryIdParam = request.getParameter("categoryId");
                    if (categoryIdParam == null || categoryIdParam.isEmpty()) {
                        categoryId = 1; // Default to Phones
                    } else {
                        categoryId = Integer.parseInt(categoryIdParam);
                    }
                } catch (NumberFormatException e) {
                    categoryId = 1; // Default to Phones if invalid
                }

                String pageTitle;
                String bannerImage;
                String cardStyle;

                switch (categoryId) {
                    case 1:
                        pageTitle = "Phones";
                        bannerImage = "Images/backgrounds/phone_banner_original.png";
                        cardStyle = "card-phone";
                        break;
                    case 2:
                        pageTitle = "TVs";
                        bannerImage = "Images/backgrounds/tv_banner_cropped.jpg";
                        cardStyle = "card-tv";
                        break;
                    case 3:
                        pageTitle = "Headphones";
                        bannerImage = "Images/backgrounds/headphones_banner_edited.jpeg";
                        cardStyle = "card-headphones";
                        break;
                    default:
                        pageTitle = "Phones";
                        bannerImage = "Images/backgrounds/phone_banner_original.png";
                        cardStyle = "card-phone";
                        break;
                }

                String query = "SELECT i.*, " +
                "COALESCE(MIN(b.amount), a.starting_price) as min_price, " +
                "COALESCE(MAX(b.amount), a.starting_price) as max_price, " +
                "a.starting_price, " +
                "CASE WHEN a.auction_id IS NOT NULL THEN 1 ELSE 0 END as has_active_auction " +
                "FROM Item i " +
                "LEFT JOIN Phone p ON p.item_id = i.item_id " +
                "LEFT JOIN TV t ON t.item_id = i.item_id " +
                "LEFT JOIN Headphones h ON h.item_id = i.item_id " +
                "LEFT JOIN Auction a ON a.item_id = i.item_id AND a.status = 'active' " +
                "LEFT JOIN Bid b ON b.auction_id = a.auction_id " +
                "WHERE i.category_id = ? " +
                "GROUP BY i.item_id, a.auction_id, a.starting_price";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, categoryId);

                ResultSet rs = ps.executeQuery();
                ArrayList<ItemBean> items = new ArrayList<>(); //ItemBean defined in the same package

                while (rs.next()) {
                    ItemBean item = new ItemBean();
                    item.setId(rs.getInt("item_id"));
                    item.setName(rs.getString("title"));
                    item.setBrand(rs.getString("brand"));
                    item.setColor(rs.getString("color"));

                    // Get image path from database
                    String imagePath = rs.getString("image_path");
                    if (imagePath == null || imagePath.isEmpty()) {
                        // Fallback to default image if not set
                        imagePath = "Images/item_photos/z_stock/stock_phone.jpg";
                    }
                    item.setImagePath(imagePath);
                    
                    // Get price information from auction
                    boolean hasActiveAuction = rs.getInt("has_active_auction") == 1;
                    item.setHasActiveAuction(hasActiveAuction);
                    
                    if (hasActiveAuction) {
                        // COALESCE in query handles case where no bids exist (uses starting_price)
                        java.math.BigDecimal minPrice = rs.getBigDecimal("min_price");
                        java.math.BigDecimal maxPrice = rs.getBigDecimal("max_price");
                        item.setMinPrice(minPrice);
                        item.setMaxPrice(maxPrice);
                    } else {
                        item.setMinPrice(null);
                        item.setMaxPrice(null);
                    }
                    
                    items.add(item);
                }
                
                rs.close();
                ps.close();
                ApplicationDB.closeConnection(con);
                request.setAttribute("pageTitle", pageTitle);
                request.setAttribute("bannerImage", bannerImage);
                request.setAttribute("items", items);
                request.setAttribute ("cardStyle", cardStyle);
                request.getRequestDispatcher("category.jsp").forward(request, response);
            }
            catch(Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error loading category: " + e.getMessage());
                request.getRequestDispatcher("category.jsp").forward(request, response);
            }
    }
}