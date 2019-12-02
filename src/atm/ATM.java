/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Wibu
 */
class Auth {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    public Auth() {
    }

    public boolean loginUser() throws SQLException, NullPointerException, ClassNotFoundException {
        Connection conn = DataConnection.getConnection(DB_URL, USER_NAME, PASSWORD);
        Statement stmt = conn.createStatement();
        boolean status = false;
        ResultSet rs = stmt.executeQuery("select * from users");

        // show data
        rs.next();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter CARD ID:");
        int card_id = input.nextInt();
        System.out.println("Enter PIN:");
        int pin = input.nextInt();
        if (card_id == rs.getInt(2) && pin == rs.getInt(3)) {
            status = true;

        }
        return status;

    }

}

public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // new object
        Auth auth = new Auth();
        try {
            if (auth.loginUser()) {
                System.out.println("Login successfully!");
            } else {

                System.out.println("Card ID or Pin Incorrect !!");
            }
        } catch (SQLException | NullPointerException | ClassNotFoundException ex) {
            System.out.println("Can't connect database.");
        }
    }

}
