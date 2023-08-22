package com.secureapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.io.InputStreamReader;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;	


public class Database {
    final int size = 3;
    private String[] conf;
    private PreparedStatement ps;
    private ResultSet rs;
    private static Connection conn;
    
     String dbPassword = "password";
     String dbUserName = "name";
     String dbURL = "dburl";
    Region region = Region.of("us-east-1");

    GetSecretValueResponse getSecretValueResponse;
    SecretsManagerClient client;
     GetSecretValueRequest getSecretValueRequest;

    public Database() {
         client = SecretsManagerClient.builder()
            .region(region)
            .build();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.toString() + "  we   have problem here");

        }
        
          try {
        readFile();
       
    } catch (Exception e) {
        throw e;
    }

  
    }

    private void readFile() {
        conf = new String[size];
             getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbURL)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             String secret = getSecretValueResponse.secretString();
             System.out.println(secret);
           
             String [] parts = secret.split(":");
             String [] values= parts[1].split("\"");
             System.out.println(values[1]);
            conf[0]=values[1];
            
             getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbUserName)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             secret = getSecretValueResponse.secretString();
            
             System.out.println(secret);
           
            parts = secret.split(":");
            values= parts[1].split("\"");
             System.out.println(values[1]);
             conf[1]=values[1];
             
              
            getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbPassword)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             secret = getSecretValueResponse.secretString();
             System.out.println(secret);
             parts = secret.split(":");
            values= parts[1].split("\"");
             System.out.println(values[1]);
             conf[2]=values[1];
    }


    public void initializeConnection() {
        conn = getMySqlConnection();
    }

    public Connection getMySqlConnection() {
        try {
            System.out.println( "jdbc:mysql://" + conf[0] + ":3306" + "/testdb?" + 
                     "autoReconnect=true&useSSL=false" + "," + conf[1] +","+ conf[2]);
            return DriverManager.getConnection(
             "jdbc:mysql://" + conf[0] + ":3306" + "/testdb" , conf[1], conf[2]);

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
