/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs_domain.lib.exceptions.InvalidArgumentException;

/**
 *
 * @author tiagogomes
 */
public class Database {
    private static Connection connection;
    
    public static Connection connectDatabase() {
        connection = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:credits.sqlite");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to Database");
        
        return connection;
    }
    
    public static ResultSet select(String table, ArrayList<String> fields, String adicionalArgs) {
        try {
            String sql = "SELECT ";
            if(fields != null) {
                sql += formatFields(fields);
            } else {
                sql += "*";
            }
            sql += " FROM " + table;
            if(adicionalArgs != null) {
                sql += " "+adicionalArgs + ";";
            } else {
                sql += ";";
            }
            Statement statement = connection.createStatement();
            
            ResultSet items = statement.executeQuery(sql);
            
            // statement.close();
            
            return items;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public static void insert(String table, ArrayList<String> fields, ArrayList<String> values) throws InvalidArgumentException  {
        
        try {
            if(fields.size() != values.size()) {
                throw new InvalidArgumentException("Fields and Values are not the same size");
            }
            
            String sql = "INSERT INTO "+ table + " (" + formatFields(fields) + ") VALUES ("+ formatFields(values) +");";
            
            // connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            
            // statement.close();
            // connection.commit();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void delete(String table, String id) {
        String sql = "DELETE from "+ table;
        if(id != null) {
            sql += "WHERE ID="+id;
        }
        
        try {
            connection.setAutoCommit(false);
            
            Statement statement = connection.createStatement();
        
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void closeConnection() {
        
    }
    
    public static Connection getConnection() {
        return connection;
    }
    
    private static String formatFields(ArrayList<String> fields) {
        String result = "";
        for(int i = 0 ; i < fields.size() ; i++) {
           String field = fields.get(i);
           result += field;
           if(i != fields.size() - 1) {
               result +=  ',';
           }
        }
        
        return result;
    }
}

