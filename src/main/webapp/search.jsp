<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.techbarn.webapp.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tech Barn — SearchPage </title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=search" />
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <style>
        * { 
            margin: 0; 
            padding: 0; 
            box-sizing: border-box; 
            font-family: 'Roboto', sans-serif; 
        }
        
        body, html {
            height: 100%;
            width: 100%;
            overflow-x: hidden;
        }
        .category-hero {
            width: 100%;
            height: 200px;
            /*background: #334b31*/
            background-image: url('./Images/aurora-borealis.jpg'); 
            background-size: cover;
            background-position: center;
            display: flex;
            justify-content: left;
            align-items: center;
        }

        /* CSS for search bar */
        .search {
            --padding: 14px;
            margin-left: 290px; /*filter + filter-margin + 10px*/
            width: max-content;
            display: flex;
            align-items: center;
            padding: var(--padding);
            border-radius: 28px;
            background: #f6f6f6;
            width: 800px;
            /*transition: background 0.25s;*/
        }
        .search:focus-within{
            box-shadow: 0 0 2px rgba(0,0,0,0.75);

        }
        .search-input {
            font-size: 28px;
            font-family: 'Lexend', sans-serif;
            margin-left: var(--padding);
            outline: none;
            border: none;
            flex: 1;
            background: transparent;
        }

        .search-input::placeholder,
        .search-icon {
            color: rgba(255, 0, 0, 0.5);
        }
        /* CSS for the filters sidebar */
        .filter-container{
            display: block;
            width: 250px;
            margin: 20px 0px 0px 20px; /*top right bottom left*/
        }
        .filters {
            background: #eeeeee;
            border-radius: 12px;
            padding: 20px 18px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
            font-size: 14px;
            
        }

        .filters h3 {
            margin-bottom: 16px;
            font-size: 18px;
            font-weight: 700;
            color: #333;
        }

        .filter-group {
            margin-bottom: 20px;
            display: block;
        }

        .filter-title {
            display: block;
            font-weight: 600;
            margin-bottom: 10px;
            color: #444;
        }

        /* “Pill” style checkboxes */
        .filter-pill {
            display: block; /*inline-flex*/
            align-items: center;
            gap: 6px;
            margin: 4px 6px 4px 0;
            padding: 6px 10px;
            border-radius: 999px;
            border: 1px solid #d1d5db;
            cursor: crosshair; /*pointer;*/
            user-select: none;
            font-size: 13px;
        }

        .filter-pill input[type="checkbox"] {
            accent-color: #6b9080;
        }

        .apply-button {
            margin-top: 8px;
            width: 100%;
            padding: 8px 0;
            border: none;
            border-radius: 999px;
            background: #6b9080;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
        }

        /* CSS for the search results grid (same as category.jsp) */
        .item-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            padding: 30px;
        }

        .item-card {
            background: #fff;
            padding: 15px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
        }
        .card-phone img {
            height: 180px;
        }

        .card-tv img {
            height: 220px;
        }

        .card-headphones img {
            height: 180px;
        }

        .item-card img {
            width: 100%;
            height: 250px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 10px;
        }
        
        .item-card a {
            text-decoration: none;
            color: inherit;
        }
        
        .item-card p {
            margin: 5px 0;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div style="padding: 20px; background-color: #ffebee; color: #c62828; margin: 20px; border-radius: 5px; border-left: 4px solid #c62828;">
        <strong>Error:</strong> <%= errorMessage %>
    </div>
    <%
        }
    %>
    <div class="category-hero">
        <form>
            <div class="search">
                <span class="search-icon material-symbols-outlined">search</span>
                <input class = "search-input" type="search" placeholder="Search our inventory..."></input>
            </div>
        </form>
    </div>
    <div class ="search-layout">
        <!-- Sidebar (left)-->
         <aside class="filter-container">
            <form method="get" action="search">
                <div class="filters">
                    <div class="filter-group">
                        <!-- dynamically loop through and render the properties as h4? -->
                        <span class="filter-title">Condition: </span>
                        <label class="filter-pill">
                             <!-- dynamically render the possible values for each property as h6? -->
                            <input type="checkbox" name="condition" value="New">
                            <span>New</span>
                        </label>
                        <label class="filter-pill">
                            <input type="checkbox" name="condition" value="Like New">
                            <span>Like New</span>
                        </label>
                        <label class="filter-pill">
                            <input type="checkbox" name="condition" value="Used">
                            <span>Used</span>
                        </label>
                    </div>
                </div>
            </form>
        </aside>
        <!-- Results Grid (right)-->
         
        <div class="item-grid">
            <%
                ArrayList<ItemBean> items = (ArrayList<ItemBean>) request.getAttribute("items");
                if (items != null && !items.isEmpty()) {
                    for (ItemBean item : items) {
            %>
            <div class="item-card"> <!--${cardClass}-->
                <a href="item?itemId=<%= item.getId() %>">
                    <img src="<%= item.getImagePath() %>" alt="<%= item.getName() %>">
                    <p><%= item.getName() %></p>
                    <p style="color: #666; font-size: 14px;"><%= item.getBrand() %> - <%= item.getColor() %></p>
                </a>
            </div>
            <%
                    }
                } else {
            %>
            <div style="grid-column: 1 / -1; text-align: center; padding: 40px;">
                <p>No items found matching the search criteria.</p>
            </div>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>