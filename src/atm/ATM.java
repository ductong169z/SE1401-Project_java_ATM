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
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Wibu
 */
class UserInfo {

    public int user_id;
    public String user_name;
    public int role_id;

    public UserInfo() {
    }

    public UserInfo(int user_id, String user_name, int role_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getRole_id() {
        return role_id;
    }

}

class Auth {

    public Auth() {
    }

    public static UserInfo loginUser() throws SQLException, NullPointerException, ClassNotFoundException {
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();
        int status = 0;

        ResultSet rs = stmt.executeQuery("SELECT users.id, card_id,pin,contact_number,gender,address,users.name,role_id from users join user_role on users.id= user_id");
        // show data

        rs.next();

        Scanner input = new Scanner(System.in);
        System.out.println("Please enter CARD ID:");
        int card_id = input.nextInt();
        System.out.println("Please enter PIN:");
        int pin = 0;
        boolean c = true;
        do {
            try {
                pin = input.nextInt();
                c = false;
            } catch (InputMismatchException e) {

                System.out.println("Only number here !!");
            }
        } while (c);
        if (card_id == rs.getInt(2) && pin == rs.getInt(3)) {

            UserInfo user = new UserInfo(rs.getInt(1), rs.getString(7), rs.getInt(8));
            return user;

        } else {
            UserInfo user2 = new UserInfo();
            return user2;
        }

    }

}

public class ATM {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, NullPointerException, ClassNotFoundException {
        // TODO code application logic here
        System.out.println("--------WIBU BANK----------");
        UserInfo user = Auth.loginUser();
      
            try {
                switch (user.getRole_id()) {
                    case 1:
                        System.out.println("Admin login successfully!");
                        System.out.println("Hello " + user.getUser_name());
                        break;
                    case 2:
                        System.out.println("User login successfully!!");
                        System.out.println("Hello " + user.getUser_name());
                        break;
                    case 0:
                        System.out.println("Card ID or PIN incorrect !!");
                        System.out.println("");

                        break;
                }
            } catch (NullPointerException ex) {
                System.out.println("Can't connect database.");
            } catch (InputMismatchException e) {

            }
    }

}
