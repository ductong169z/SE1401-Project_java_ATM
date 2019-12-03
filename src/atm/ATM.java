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
import java.util.Random;

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

        ResultSet rs = stmt.executeQuery("SELECT users.id, card_id, pin, contact_number, gender, address, users.name, role_id FROM users JOIN user_role ON users.id= user_id");
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

class Menu {

    public Menu() {
    }

    // random from 10000000 to 10099999
    public int randomCardID() {
        Random rand = new Random(System.currentTimeMillis());
        int randNum = rand.nextInt(99999) + 10000000;
        return randNum;
    }

    // random from 1000 to 9999
    public int randomPIN() {
        Random rand = new Random(System.currentTimeMillis());
        int randNum = rand.nextInt(9000) + 1000;
        return randNum;
    }

    public void createAccount() throws SQLException, ClassNotFoundException {
        // random card ID and PIN, check to make sure card ID is unique
        boolean check = true;

        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();
        do {
            int cardID = randomCardID();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE card_id = " + cardID);
            // if there is already such card ID in database, random again
            if (rs.next()) {
                check = false;
            }
        } while (!check);

        int PIN = randomPIN();

        // filling user account's basic details
        String name = "";
        String contactNumber = "";
        String gender = "";
        String address = "";

    }

    public void changeDepositLimit() {

    }

    public void changeWithdrawalLimit() {

    }

    public void changeTransCount() {

    }

    public void createDepositReport() {

    }

    public void createWithdrawalReport() {

    }

    public void displayAdminMenu() throws SQLException, ClassNotFoundException {
        int choice = 0;
        boolean check = true; // validate if input is valid (by default input is valid)

        do {
            System.out.println("------------ ADMINISTRATION ------------");
            System.out.println("1. Creating new user account");
            System.out.println("2. Change deposit limitation");
            System.out.println("3. Change withdrawal limitation");
            System.out.println("4. Change the number of last transactions on display");
            System.out.println("5. Create deposit report");
            System.out.println("6. Create withdrawal report");
            System.out.println("7. Exit");
            do {
                try {
                    Scanner input = new Scanner(System.in);

                    System.out.print("Enter your choice: ");
                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 7) {
                        check = false;
                        System.out.println("Please enter a number from 1 to 6 ");
                        System.out.println("");
                    }
                } catch (InputMismatchException ex) {
                    check = false;
                    System.out.println("Please enter a number ");
                    System.out.println("");
                } catch (Exception ex) {
                    check = false;
                    System.out.println("An error occured! Please try again later!");
                    System.out.println("");
                }
            } while (!check);

            switch (choice) {
                case 1:
                    createAccount();

                    break;

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:

            }
        } while (choice != 7);
    }
}

public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
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
            System.out.println("Input numbers only!");
        }

    }

}
