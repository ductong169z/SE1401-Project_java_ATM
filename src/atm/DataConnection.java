/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Wibu
 */
public class DataConnection {

    public static Connection getConnection(String dbURL, String userName, 
            String password) throws ClassNotFoundException, SQLException {
        Connection conn = null;
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, userName, password);
//            System.out.println("connect successfully!");
     
        return conn;
    }
}
