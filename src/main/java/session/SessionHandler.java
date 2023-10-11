package session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/haproxy";
    private static final String DB_USER = "valisoa";
    private static final String DB_PASSWORD = "Valis0a!";
    public static Connection getConnection() {
        Connection connection=null;
        try {
            Class.forName("org.postgresql.Driver");
            connection= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }

    private static byte[] sessionValuetoByte(HttpSession session){
        byte[] sessionBytes =null;
        Enumeration<String> attributes = session.getAttributeNames();
        Map<String, Object> serializedAttributes = new HashMap<>();
        while (attributes.hasMoreElements()) {
            String attribute = attributes.nextElement();
            String sessionValue=(String)session.getAttribute(attribute);
            serializedAttributes.put(attribute,sessionValue);
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            ObjectOutput objectOutput= new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(serializedAttributes);
            sessionBytes=byteArrayOutputStream.toByteArray();
            System.out.println(sessionBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sessionBytes;
    }
    public static void saveSession(HttpSession session) throws Exception{
        Connection connection=null;
        try {
            connection=getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO session_data (session_id, session_data, creation_time, last_accessed_time) VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (session_id) DO UPDATE SET session_data = EXCLUDED.session_data, last_accessed_time = EXCLUDED.last_accessed_time"
            );
            statement.setString(1, session.getId());
            statement.setBytes(2, sessionValuetoByte(session));
            statement.setTimestamp(3, new Timestamp(session.getCreationTime()));
            statement.setTimestamp(4, new Timestamp(session.getLastAccessedTime()));
            statement.executeUpdate();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.close();
        }
    }
    private static void byteValuestoSession(HttpSession session, byte[] data){
       try{
//           ObjectInputStream objectInput=new ObjectInputStream()
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public static HttpSession getSession(HttpServletRequest request) throws Exception{
        String sessionId= request.getSession().getId();
        Connection connection=null;
        try {
            connection=getConnection();
            PreparedStatement statement= connection.prepareStatement(
                    "select session_data from session_data where session_id=?"
            );
            statement.setString(1,sessionId);
            ResultSet rs= statement.executeQuery();
            if (rs.next()){

            }
        }catch (Exception e){

        }finally {
            connection.close();
        }
        return null;
    }

    public static void removeSession(HttpSession session) throws SQLException {
        Connection connection=null;
        try {
            connection=getConnection();
            PreparedStatement statement= connection.prepareStatement(
                    "DELETE from session_data where session_id=?"
            );
            statement.setString(1,session.getId());
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.close();
        }
    }

    public static void main(String[] args) throws SQLException {
//        Connection connection=getConnection();
//        System.out.println(connection);
//        connection.close();

    }


}
