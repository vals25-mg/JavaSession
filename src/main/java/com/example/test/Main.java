package com.example.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws SocketException {
//        String data="password|s:3:\"123\";user|s:14:\"vals@gmail.com\";";
//        data=data.substring(0,data.length()-1);
//        System.out.println(data);
//        String[] datas=data.split(";");
//        for (String s:datas) {
//            int barindex=s.indexOf("|");
//            System.out.println(s.substring(0,barindex));
//            int quote1=s.indexOf("\"")+1;
//            int quote2=s.lastIndexOf("\"");
//            System.out.println(s.substring(quote1,quote2));
//        }
//        for (;;)
//            System.out.println(UUID.randomUUID().toString());
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    System.out.println("IP Address: " + inetAddress.getHostAddress());
                }
            }
        }
    }
}
