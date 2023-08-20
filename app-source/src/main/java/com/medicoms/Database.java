package com.medicoms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Database {

    final int size = 3;
    private String[] conf;
    private PreparedStatement ps;
    private ResultSet rs;
    private static Connection conn;

    public Database() {
        readFile();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.toString() + "  we   have problem here");

        }
    }

    private void readFile() {
        conf = new String[size];
//Path pathOne = Paths.get("/src/main/resources/conf.txt");
        try {
            InputStream is = Database.class.getClassLoader().getResourceAsStream("conf.txt");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader file = new BufferedReader(isr);
            String line = null; // line read from file
            int i = 0;
            while ((line = file.readLine()) != null) {
                conf[i++] = line;
                System.out.println(line);
            }

            file.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void initializeConnection() {
        conn = getMySqlConnection();
    }

    public Connection getMySqlConnection() {
        try {
            return DriverManager.getConnection(
             "jdbc:mysql://" + conf[0] + "/testdb?" + 
                     "autoReconnect=true&useSSL=false", conf[1], conf[2]);

        } catch (SQLException e) {
            System.out.println("SQL connection failure");
        }
        return null;
    }

    public void addUser(int id, String fName, String lName) {
        String sql = "insert into Person values (?,?,?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, fName);
            ps.setString(3, lName);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL execution    failure");
        }
    }

    public boolean doesUserExist(int id) {
        String sql = "select * from Person where ID=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("SQL execution failure");
        }
        return false;
    }

    public boolean isAdmin(int id, String password) {
        String sql = "select * from Admin where ID=? and Password=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, password);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("SQL execution failure");
        }
        return false;
    }

    public ResultSet allUsers() {
        String sql = "select * from Person";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
