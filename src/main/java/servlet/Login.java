package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.SessionHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/LoginServlet")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionHandler sessionHandler=new SessionHandler(req,resp);
        String user= req.getParameter("user");
        String password=req.getParameter("password");
        System.out.println("User: "+user+" / Password: "+password);
//        System.out.println("Creation time: "+session.getCreationTime());
//        System.out.println("Last access time: "+session.getLastAccessedTime());
        try {
            sessionHandler.sessionStart(req,resp);
            resp.sendRedirect("loginSuccess.jsp");
        }catch (Exception e){
            PrintWriter out =resp.getWriter();
            out.print(e.getMessage());
        }
    }
}
