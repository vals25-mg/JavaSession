package com.example.test;

public class Main {
    public static void main(String[] args) {
        String data="password|s:3:\"123\";user|s:14:\"vals@gmail.com\";";
        data=data.substring(0,data.length()-1);
        System.out.println(data);
        String[] datas=data.split(";");
        for (String s:datas) {
            int barindex=s.indexOf("|");
            System.out.println(s.substring(0,barindex));
            int quote1=s.indexOf("\"")+1;
            int quote2=s.lastIndexOf("\"");
            System.out.println(s.substring(quote1,quote2));
        }
    }
}
