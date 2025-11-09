package com.buyme.webapp;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    // Java logic activates once the user clicks the login button
    try{

        HttpSession session = request.getSession(); //create a session object
        
        //Check if the user is already logged in
        if (session.getAttribute("username") != null) {
            response.sendRedirect ("welcome.jsp"); //Redirect to welcome page if already logged in
            return;
        }

        String errorMessage = null;
        String successMessage = null;

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Check if the username and password exist in the database
            if (username != null && password != null){

                //Get the database connection
                ApplicationDB db = new ApplicationDB();	
                Connection con = db.getConnection();

                String query = "Select * from user WHERE username = ? AND pwd = ?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    session.setAttribute ("username", username);
                    session.setAttribute ("password", password);
                    session.setAttribute ("firstName", rs.getString ("firstName"));
                    session.setAttribute ("lastName", rs.getString ("lastName"));
                    
                    rs.close();
                    ps.close();
                    db.closeConnection(con);
                    
                    successMessage = "Login successful! Redirecting...";


                    return;

                }
                else{
                    errorMessage = "Invalid username or password";
                }
                rs.close()
                ps.close();
                db.closeConnection(con);
                con = null;
                return;
            }
    }   catch (Exception e) {
                System.out.println(e);
                System.out.println ("Connection failed");
    }




            }
        }