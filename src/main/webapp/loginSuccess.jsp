<%--
  Created by IntelliJ IDEA.
  User: valisoa
  Date: 09/10/2023
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="session.SessionHandler" %>
<%@ page import="java.sql.Timestamp" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<%
    HttpSession session1= SessionHandler.getSession(request);
%>
    <h1>Login Success</h1>
    <br>
    <p>User: <%=session1.getAttribute("user")%></p>
    <p>Last access: <%=new Timestamp(session1.getLastAccessedTime())%></p>
    <a href="LogoutServlet">Logout</a>
</body>
</html>
