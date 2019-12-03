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
class Auth {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    public Auth() {
    }

    public static int loginUser() throws SQLException, NullPointerException, ClassNotFoundException {
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

class Menu {

    public Menu() {
    }

    public int random() {
        Random rand = new Random(System.currentTimeMillis());
        int randNum = rand.nextInt(9000) + 1000;
        return randNum;
    }

    public void displayAdminMenu() {
        int choice;
        boolean check; // validate if input is valid

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
                // by default input for "choice" is valid
                check = true;

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
            }
        } while (!check);
    }
}

class Menu {

    public Menu() {
    }

    public int random() {
        Random rand = new Random(System.currentTimeMillis());
        int randNum = rand.nextInt(9000) + 1000;
        return randNum;
    }

    public void displayAdminMenu() {
        int choice, check;

        System.out.println("------------ ADMINISTRATION ------------");
        System.out.println("1. Creating new user account");
        System.out.println("2. Change deposit limitation");
        System.out.println("3. Change withdrawal limitation");
        System.out.println("4. Change the number of last transactions on display");
        System.out.println("5. Create deposit report");
        System.out.println("6. Create withdrawal report");
        do {
            try {
                Scanner input = new Scanner(System.in);
                // by default input for "choice" is valid
                check = 1;

                System.out.print("Enter your choice: ");
                choice = input.nextInt();
                input.nextLine();

                if (choice < 1 || choice > 6) {
                    check = 0;
                    System.out.println("Please enter a number from 1 to 6 ");
                    System.out.println("");
                }
            } catch (InputMismatchException ex) {
                check = 0;
                System.out.println("Please enter a number ");
                System.out.println("");
            }
        } while (check == 0);
    }
}

public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Auth auth = new Auth();
        boolean check = true;
        do {
            try {
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
                        System.out.println("Card ID or PIN is incorrect !!");
                        System.out.println("");
                        check = false;
                        break;
                }
            } catch (SQLException | NullPointerException | ClassNotFoundException ex) {
                System.out.println("Cannot connect to the database!");
            }
        } while (!check);
    }

}
