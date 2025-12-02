<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Edit User</title></head>
<body>
<h1>Edit User</h1>
<form method="post" action="<%=request.getContextPath()%>/custrep">
    <input type="hidden" name="action" value="updateUser" />
    User ID: <input type="text" name="user_id"/><br/>
    Email: <input type="email" name="email"/><br/>
    <button type="submit">Save</button>
</form>
</body>
</html>
