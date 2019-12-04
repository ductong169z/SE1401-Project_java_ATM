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
        boolean check = true;

        ResultSet rs = stmt.executeQuery("SELECT users.id, card_id, pin, contact_number, gender, address, users.name, role_id FROM users JOIN user_role ON users.id= user_id");
        // show data

        rs.next();

        System.out.println("Please enter CARD ID:");
        int card_id = 0;

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                card_id = input.nextInt();
                input.nextLine();

                if (card_id < 10000000 || card_id > 10099999) {
                    check = false;
                    System.out.println("Card ID is from 10000000 to 10099999");
                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input card ID as a number! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        System.out.println("Please enter PIN:");
        int pin = 0;

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true;

                pin = input.nextInt();
                input.nextLine();

                if (pin < 1000 || pin > 9999) {
                    check = false;
                    System.out.println("PIN consists of 4 numbers! ");
                }

            } catch (InputMismatchException e) {
                check = false;
                System.out.println("Only numbers here !!");

            } catch (Exception e) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

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
        return rand.nextInt(100000) + 10000000;
    }

    // random from 1000 to 9999
    public int randomPIN() {
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt(9000) + 1000;
    }

    public void createAccount() throws SQLException, ClassNotFoundException {
        boolean check = true; // validate input
        int roleID = 2; // store input role ID for user (1 for Admin and 2 for User), by default it's user

        System.out.println("Do you want to create an account for an admin or a user?");
        // input and check role ID

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                System.out.print("Input your choice (1 - Admin or 2 - User): ");
                roleID = input.nextInt();
                input.nextLine();

                if (roleID != 1 && roleID != 2) {
                    check = false;
                    System.out.println("Please input 1 (Admin) or 2 (User) only! ");
                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input an integer! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        int cardID;
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // loop until card ID is unique
        do {
            cardID = randomCardID();
            ResultSet getCardID = stmt.executeQuery("SELECT * FROM users WHERE card_id = " + cardID);
            // if there is already such card ID in database, random again
            if (getCardID.next()) {
                check = false;
            }
        } while (!check);

        int PIN = randomPIN();

        String name = "";
        String contactNumber = "";
        int gender = 1;
        String address = "";

        /**
         * input and fill account basic details
         */
        System.out.println("Filling the new account's basic details: ");

        // input and check account's name
        System.out.print("Input the account's name: ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid
                name = input.nextLine();

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input account's name as a string! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        // input and check account's contactNumber       
        System.out.print("Input the account's contact number: ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                contactNumber = input.nextLine();
                
                // check if there is a character in string not a number
                for (int i = 0; i < contactNumber.length(); i++) {
                    if (contactNumber.charAt(i) < 48 || contactNumber.charAt(i) > 57) {
                        check = false;
                        System.out.println("Please input numbers only! ");
                        break;
                    }
                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input account's contact number as a string! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        // input and check account's gender       
        System.out.print("Input the account's gender (0 - Female or 1 - Male): ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                gender = input.nextInt();
                input.nextLine();

                if (gender != 0 && gender != 1) {
                    check = false;
                    System.out.println("Please input 0 (female) or 1 (male)! ");
                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input an integer! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        // input and check account's address   
        System.out.print("Input the account's address: ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                address = input.nextLine();

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input account's address as a string! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);

        // SQL statements
        stmt.executeUpdate("INSERT INTO users(card_id, pin, contact_number, gender, address, name) VALUES(" + cardID + "," + PIN + ", \"" + contactNumber + "\" ," + gender + ", \"" + address + "\" , \"" + name + "\")");
        ResultSet insertInfo = stmt.executeQuery("SELECT id FROM users WHERE card_id = " + cardID);
        insertInfo.next();
        int userID = insertInfo.getInt(1);
        stmt.executeUpdate("INSERT INTO user_role(user_id, role_id) VALUES(" + userID + "," + roleID + ")");
        stmt.executeUpdate("INSERT INTO user_money(user_id, total_money) VALUES(" + userID + "," + 0 + ")");

    }

    public void changeDepositRelatedLimits() {
        int choice = 0; // store the choice
        boolean check = true; // validate if input is valid (by default input is valid)
        int depositLimit = 25000; // store deposit limit
        int depositNumLimit = 5; // store the limit of number of deposits
        // loop until "exit" is chosen
        do {
            System.out.println("What would you like to do?");
            System.out.println("1. Change deposit limit");
            System.out.println("2. Change the amount of deposits limit");
            System.out.println("3. Revert deposit limit to default value (25000$)");
            System.out.println("4. Revert the amount of deposits limit to default value (5 times)");
            System.out.println("5. Exit");
            System.out.print("Input your choice: ");

            do {

                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 7) {
                        check = false;
                        System.out.println("Please input a number from 1 to 5 ");

                    }
                } catch (InputMismatchException ex) {
                    check = false;
                    System.out.println("Please input a number ");

                } catch (Exception ex) {
                    check = false;
                    System.out.println("An error occured! Please try again later!");

                }
            } while (!check);

            switch (choice) {
                case 1:
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            depositLimit = input.nextInt();
                            input.nextLine();

                            if (depositLimit <= 5) {
                                check = false;
                                System.out.println("Please input a number from 1 to 5 ");
                                System.out.println("If you want to input 5, ");
                            }
                        } catch (InputMismatchException ex) {
                            check = false;
                            System.out.println("Please input a number ");

                        } catch (Exception ex) {
                            check = false;
                            System.out.println("An error occured! Please try again later!");

                        }
                    } while (!check);
            
                    break;

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:

            }
        } while (choice != 5);
    }

    public void changeWithdrawRelatedLimits() {
        int choice = 0;
        boolean check = true; // validate if input is valid (by default input is valid)
        int withdrawLimit = 25000; // store withdraw limit
        int withdrawNumLimit = 5; // store the limit of number of withdrawals

        // loop until "exit" is chosen
        do {
            System.out.println("What would you like to do?");
            System.out.println("1. Change withdraw limit");
            System.out.println("2. Change the amount of withdraws limit");
            System.out.println("3. Revert withdraw limit to default value (25000$)");
            System.out.println("4. Revert the amount of withdraws limit to default value (5 times)");
            System.out.println("5. Exit");
            System.out.print("Input your choice: ");

            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 7) {
                        check = false;
                        System.out.println("Please input a number from 1 to 5! ");

                    }
                } catch (InputMismatchException ex) {
                    check = false;
                    System.out.println("Please input a number! ");

                } catch (Exception ex) {
                    check = false;
                    System.out.println("An error occured! Please try again later!");

                }
            } while (!check);

            switch (choice) {
                case 1:

                    break;

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:

            }
        } while (choice != 5);
    }

    public int setTransCount() {
        int transCount = 5; // store number of transactions from input (default value: 5) 
        boolean check = true; // validate input

        // loop until input is valid
        System.out.print("Input your choice: ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                transCount = input.nextInt();
                input.nextLine();

                if (transCount < 1) {
                    check = false;
                    System.out.println("The number of displayed transactions must be at least 1! ");

                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input a number ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later!");

            }
        } while (!check);

        return transCount;
    }

    public void createDepositReport() {

    }

    public void createWithdrawalReport() {

    }

    public void changeUserInfo() {
        int cardID = 0;
        boolean check = true; // by default input is valid

        System.out.print("Input user's card ID: ");

        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                cardID = input.nextInt();
                input.nextLine();

                if (cardID < 10000000 || cardID > 10099999) {
                    check = false;
                    System.out.println("Card ID is from 10000000 to 10099999");
                }

            } catch (InputMismatchException ex) {
                check = false;
                System.out.println("Please input user card ID as a number! ");

            } catch (Exception ex) {
                check = false;
                System.out.println("An error occured! Please try again later! ");

            }
        } while (!check);
    }

    public void changeAdminInfo() {

    }

    public void changePassword() {

    }

    public void displayAdminMenu() throws SQLException, ClassNotFoundException {
        int choice = 0;
        boolean check = true; // validate if input is valid (by default input is valid)

        do {
            System.out.println("------------ ADMINISTRATION ------------");
            System.out.println("1. Create new user account");
            System.out.println("2. Change deposit-related limitations");
            System.out.println("3. Change withdrawal-related limitations");
            System.out.println("4. Change the number of last transactions to display");
            System.out.println("5. Create deposit report");
            System.out.println("6. Create withdrawal report");
            System.out.println("7. Change user info");
            System.out.println("8. Change admin info");
            System.out.println("9. Change current account's password");
            System.out.println("10. Exit");
            System.out.print("Input your choice: ");

            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 10) {
                        check = false;
                        System.out.println("Please input a number from 1 to 10 ");

                    }
                } catch (InputMismatchException ex) {
                    check = false;
                    System.out.println("Please input a number ");

                } catch (Exception ex) {
                    check = false;
                    System.out.println("An error occured! Please try again later!");

                }
            } while (!check);

            switch (choice) {
                case 1:
                    createAccount();

                    break;

                case 2:
                    changeDepositRelatedLimits();

                    break;

                case 3:
                    changeWithdrawRelatedLimits();

                    break;

                case 4:
                    setTransCount();

                    break;

                case 5:
                    createDepositReport();

                    break;

                case 6:
                    createWithdrawalReport();

                    break;

                case 7:
                    changeUserInfo();

                    break;

                case 8:
                    changeAdminInfo();

                    break;

                case 9:
                    changePassword();

                    break;

                case 10:
            }
        } while (choice != 10);
    }
}

public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("--------WIBU BANK----------");
        
        boolean check = true;
        do {
            try {
                check = true;
                UserInfo user = Auth.loginUser();
                
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
                        check = false;
                        break;
                }
            } catch (NullPointerException ex) {
                System.out.println("Can't connect database.");
            } catch (InputMismatchException e) {
                System.out.println("Input numbers only!");
            }
        } while (!check);
        
        Menu obj = new Menu();

        obj.createAccount();
    }

}
