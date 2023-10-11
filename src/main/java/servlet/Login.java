package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.SessionHandler;

import java.io.*;
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
        String user= req.getParameter("user");
        String password=req.getParameter("password");
        HttpSession session= req.getSession();
        session.setAttribute("user",user);
        session.setAttribute("password",password);
        System.out.println("User: "+user+" / Password: "+password);
        System.out.println("Session id: "+session.getId());
        System.out.println("Creation time: "+session.getCreationTime());
        System.out.println("Last access time: "+session.getLastAccessedTime());
        try {
            SessionHandler.saveSession(session);
        }catch (Exception e){
            e.printStackTrace();
        }
        resp.sendRedirect("loginSuccess.jsp");
    }
}
