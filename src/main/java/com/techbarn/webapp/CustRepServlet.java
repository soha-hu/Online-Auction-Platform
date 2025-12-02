package com.techbarn.webapp;

import com.techbarn.webapp.dao.AuctionDAO;
import com.techbarn.webapp.dao.BidDAO;
import com.techbarn.webapp.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/custrep")
public class CustRepServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
        }
        req.getRequestDispatcher("/custrep_home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        String role = (s!=null) ? (String) s.getAttribute("role") : null;
        if (!"rep".equals(role) && !"admin".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
            return;
        }
        String action = req.getParameter("action");
        boolean success = false;
        String message = "";
        
        try {
            if ("removeBid".equals(action)) {
                int bidNo = Integer.parseInt(req.getParameter("bid_no"));
                int auctionId = Integer.parseInt(req.getParameter("auction_id"));
                success = BidDAO.removeBid(bidNo, auctionId);
                message = success ? "Bid removed successfully" : "Failed to remove bid - bid may not exist";
            } else if ("removeAuction".equals(action)) {
                int auctionId = Integer.parseInt(req.getParameter("auction_id"));
                success = AuctionDAO.removeAuction(auctionId);
                message = success ? "Auction removed successfully" : "Failed to remove auction - auction may not exist";
            } else if ("deleteUser".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("user_id"));
                success = UserDAO.deleteUser(userId);
                message = success ? "User deleted successfully" : "Failed to delete user - user ID may not exist";
            } else if ("updateUser".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("user_id"));
                String email = req.getParameter("email");
                success = UserDAO.updateEmail(userId, email);
                message = success ? "User email updated successfully" : "Failed to update email - user may not exist";
            }
        } catch (NumberFormatException e) {
            message = "Invalid input - please enter valid numbers";
        } catch (Exception e) {
            message = "Error: " + e.getMessage();
        }
        
        if (success) {
            req.getSession().setAttribute("successMessage", message);
        } else {
            req.getSession().setAttribute("errorMessage", message);
        }
        resp.sendRedirect(req.getContextPath() + "/custrep");
    }
}
