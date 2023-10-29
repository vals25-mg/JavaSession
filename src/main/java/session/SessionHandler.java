package session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionHandler {
    protected HttpServletResponse response;
    protected HttpServletRequest request;

    private final String DB_URL = "jdbc:postgresql://localhost:5432/haproxy";
    private final String DB_USER = "valisoa";
    private final String DB_PASSWORD = "Valis0a!";
    public Connection getConnection() {
        Connection connection=null;
        try {
            Class.forName("org.postgresql.Driver");
            connection= DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }

    private byte[] sessionValuetoByte(HttpServletRequest request){
        byte[] sessionBytes =null;
        Enumeration<String> parameters = request.getParameterNames();
        HashMap<String, Object> serializedAttributes = new HashMap<>();
        while (parameters.hasMoreElements()) {
            String attribute = parameters.nextElement();
            String sessionValue=request.getParameter(attribute);
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
    public void sessionStart(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Connection connection=null;
        String sessionId=getSessionId();
        System.out.println("Session Id: "+sessionId);
        try {
            connection=getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO session_data (session_id, session_data, creation_time, last_accessed_time,ipaddress) VALUES (?, ?, ?, ?,?) " +
                            "ON CONFLICT (session_id) DO UPDATE SET session_data = EXCLUDED.session_data, last_accessed_time = EXCLUDED.last_accessed_time"
            );
            statement.setString(1, sessionId);
            statement.setBytes(2, sessionValuetoByte(this.request));
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setString(5, getIpAddress());
            statement.executeUpdate();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.close();
        }
    }

    public void deleteSession(Connection connection,String session_id,String ipaddress) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "delete from session_data where session_id=? and ipaddress=?"
        );
        statement.setString(1, session_id);
        statement.setString(2, ipaddress);
        statement.executeUpdate();
        statement.close();
    }
    private HashMap<String,Object> byteValuestoSession(byte[] data){
        HashMap<String,Object> deserializedAttributes=new HashMap<>();
       try{
           ObjectInputStream objectInput=new ObjectInputStream(new ByteArrayInputStream(data));
           deserializedAttributes= (HashMap<String, Object>) objectInput.readObject();
//           for(Map.Entry<String,Object> entry: deserializedAttributes.entrySet()){
//               this.request.setAttribute(entry.getKey(), entry.getValue());
//           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return deserializedAttributes;
    }
    public String getIpAddress() throws IOException {
        String ip=null;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        ip=socket.getLocalAddress().toString();
        System.out.println(ip);
        return ip.substring(1,ip.length());
    }
    public HashMap<String,Object> getSession() throws Exception{
        String sessionId= getSessionId();
        HashMap<String,Object> sessionData=new HashMap<>();
        Connection connection=null;
        try {
            connection=getConnection();
//            Miverifier hoe tokony ho expirer ve sa aona
            String isExpired="select extract(EPOCH from (last_accessed_time-creation_time))>(2*60) from session_data where session_id=? and ipaddress=?";
            try(PreparedStatement selectExpired= connection.prepareStatement(isExpired)) {
                selectExpired.setString(1, getSessionId());
                selectExpired.setString(2, getIpAddress());
                ResultSet resultSet = selectExpired.executeQuery();
                boolean isexpired=false;
                if(resultSet.next())
                 isexpired= resultSet.getBoolean(1);
                resultSet.close();
                if (isexpired) {
//                    Mamafa anleh session ao anaty base de mitenty amny leh olona oe mila manao login vaovao
                    deleteSession(connection,getSessionId(),getIpAddress());
                    sessionData=null;
                } else {
//            Manao Update anleh last Access
                    PreparedStatement statement = connection.prepareStatement(
                            "update session_data set last_accessed_time=? where session_id=? and ipaddress=?"
                    );
                    statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    statement.setString(2, sessionId);
                    statement.setString(3, getIpAddress());
                    statement.executeUpdate();
                    statement = connection.prepareStatement(
                            "select session_data from session_data where session_id=?"
                    );
                    statement.setString(1, sessionId);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        byte[] datas = rs.getBytes("session_data");
                        sessionData = byteValuestoSession(datas);
                    }
                    statement.close();
                }
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sessionData;
    }

    public void updateSession(){

    }

    public void removeSession() throws SQLException {
        String sessionId=getSessionId();
        Connection connection=null;
        try {
            Cookie[]cookies=request.getCookies();
            for (Cookie cookie:cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            connection=getConnection();
            System.out.println("Delete: "+sessionId);
            PreparedStatement statement= connection.prepareStatement(
                    "DELETE from session_data where session_id=?"
            );
            statement.setString(1,sessionId);
            System.out.println(statement);
            statement.executeUpdate();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
//

        }finally {
            connection.close();
        }
    }

    protected String getSessionId(){
        String sessionId=null;
        Cookie[] cookies= request.getCookies();
        if (cookies!=null){
            for (Cookie cookie: cookies){
                System.out.println(cookie.getName()+"");
                if(cookie.getName().equals("sessionId")){
                    sessionId=cookie.getValue();
                    break;
                }
            }
        }
        return sessionId;
    }
    public SessionHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request=request;
        this.response=response;
        if(getSessionId()==null){
            String sessionId= UUID.randomUUID().toString();
            Cookie cookie=new Cookie("sessionId",sessionId);
            response.addCookie(cookie);
         }
    }

    public void main(String[] args) throws SQLException {
//        Connection connection=getConnection();
//        System.out.println(connection);
//        connection.close();

    }


}
