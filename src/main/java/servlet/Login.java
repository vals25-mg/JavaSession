package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        Enumeration<String> attributes = req.getSession().getAttributeNames();
        String valeur="";
        Map<String, Object> serializedAttributes = new HashMap<>();
        while (attributes.hasMoreElements()) {
            String attribute = attributes.nextElement();
            String sessionValue=(String)req.getSession().getAttribute(attribute);
            int sessionLength=sessionValue.length();
            valeur+=attribute+"|s:"+sessionLength+":\""+sessionValue+"\";";
            System.out.println(attribute+" : "+req.getSession().getAttribute(attribute));
            serializedAttributes.put(attribute,req.getSession().getAttribute(attribute));
        }
        String forme="ip|s:10:\"shlkhfsx\";";
        System.out.println(forme);
        System.out.println(valeur);
        // Create an output stream to write the session data
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(byteStream)) {
            // Serialize the HttpSession object to the output stream
            objectOutput.writeObject(serializedAttributes);
            // Convert the output stream to a byte array
            byte[] sessionBytes = byteStream.toByteArray();
            System.out.println(sessionBytes);
            // Now you have the session data in the byte array
            ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(sessionBytes));
            Map<String, Object> deserializedAttributes = (Map<String, Object>) objectInput.readObject();
            // Assuming you have the HttpSession object (session) available
            // Restore the attributes in the HttpSession
            for (Map.Entry<String, Object> entry : deserializedAttributes.entrySet()) {
//                session.setAttribute(entry.getKey(), entry.getValue());
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("loginSuccess.jsp");
    }
}
