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
<%@ page import="java.util.HashMap" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.Enumeration" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<%
    SessionHandler sessionHandler=new SessionHandler(request, response);
    HashMap<String,Object> sessionData=sessionHandler.getSession();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        System.out.println(headerName + ": " + headerValue);
    }
%>
    <h1>Login Success</h1>
<%
    if(sessionData!=null) {
        for (HashMap.Entry<String, Object> entry : sessionData.entrySet()) {
%>
    <p><%=entry.getKey()+": "+entry.getValue()%></p>
<%
    }
%>
    <p><%=sessionHandler.getIpAddress()%></p>
<p><%=System.getProperty("java.rmi.server.hostname")%></p>
<p><%=request.getHeader("X-FORWARDED-FOR")%></p>
    <a href="LogoutServlet">Logout</a>
<%
    } else {
%>
Session expirée, <a href="login.jsp">Veuillez vous reconnectez</a>
<%
    }
%>
</body>
</html>
