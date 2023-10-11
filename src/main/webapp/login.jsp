<%--
  Created by IntelliJ IDEA.
  User: valisoa
  Date: 09/10/2023
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body style="display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f0f0;">
    <div style="border: 1px solid #333; padding: 20px; background-color: #fff;">
        <h1>Login</h1>
        <form action="LoginServlet" method="post" style="text-align: center;">

            Username: <input type="text" name="user" style="margin: 10px;">
            <br>
            Password: <input type="password" name="password" style="margin: 10px;">
            <br>
            <input type="submit" value="Login" style="margin-top: 10px; padding: 5px 15px; background-color: #333; color: #fff; border: none; cursor: pointer;">
        </form>
    </div>
</body>
</html>
