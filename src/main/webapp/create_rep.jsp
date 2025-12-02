<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Create Rep</title></head>
<body>
<h1>Create Customer Representative</h1>
<form method="post" action="<%=request.getContextPath()%>/admin">
    Username: <input type="text" name="username" /><br/>
    Email: <input type="email" name="email" /><br/>
    Password: <input type="password" name="password" /><br/>
    <button type="submit">Create</button>
</form>
</body>
</html>
