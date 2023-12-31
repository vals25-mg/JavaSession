package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.SessionHandler;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/LogoutServlet")
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            SessionHandler sessionHandler=new SessionHandler(req,resp);
            try {
                sessionHandler.removeSession();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


}
