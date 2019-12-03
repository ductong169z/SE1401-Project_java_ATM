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

    public static int loginUser() throws SQLException,NullPointerException,ClassNotFoundException {
        Connection conn = DataConnection.getConnection(DB_URL, USER_NAME, PASSWORD);
        Statement stmt = conn.createStatement();
        int status = 0;
        ResultSet rs = stmt.executeQuery("SELECT users.id, card_id,pin,contact_number,gender,address,users.name,role_id from users join user_role on users.id= user_id");
        // show data
        rs.next();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter CARD ID:");
        int card_id = input.nextInt();
        System.out.println("Enter PIN:");
        int pin = input.nextInt();
        if (card_id == rs.getInt(2) && pin == rs.getInt(3)) {
            status = rs.getInt(8);

        }
        return status;

    }

}

public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Auth auth = new Auth();
        boolean check=false;
        do{
        try {
                System.out.println("Card ID or Pin Incorrect !!");
            switch (auth.loginUser()) {
                case 1:
                    System.out.println("Admin login successfully!");
                    break;
                case 2:
                    System.out.println("User login !!");
                    break;
                case 3:
                    System.out.println("Exit.");
                    break;
                default:
                    System.out.println("Card ID or PIN incorrect !!");
                    System.out.println("");
                    check=true;
                    break;
            }
        } catch (SQLException | NullPointerException | ClassNotFoundException ex) {
            System.out.println("Can't connect database.");
        }
        }while(check);
       
    }
    public void Usermenu(){
        System.out.println(" ♥(。O ω O。)WELCOME TO WIBU ATM(。O ω O。)♥");
        System.out.println("            -----USERS MENU-----");
        System.out.print("1.Deposit                ");
        System.out.println("2.Withdrawal");
        System.out.print("3.Balance Enquiry        ");
        System.out.println("4.Change Password.");
        System.out.println("5.Exit");
        System.out.println(" Wibu Choice(1-5): ");
    }
}
