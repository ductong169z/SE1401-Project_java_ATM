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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wibu
 */
// this class is used to store user info (either the admin or the user who logged in)
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

// this class is used to authenticate if user login credentials are valid or not
class Auth {

    public Auth() {
    }

    /**
     * get and store user info in the form of an object
     *
     * @return object user (which includes user information)
     * @throws SQLException
     * @throws NullPointerException
     * @throws ClassNotFoundException
     */
    public static UserInfo loginUser() throws SQLException, NullPointerException, ClassNotFoundException {
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        boolean check = true; // validate input

        int pin = 0; // temporarily store PIN 
        int card_id = 0; // temporarily store card ID
        ResultSet rs; // store the queries' results

        // loop until card ID is correctly inputted and exists in database
        do {
            System.out.println("Please enter CARD ID:");
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                card_id = input.nextInt();
                input.nextLine();

                if (card_id < 10000000 || card_id > 10099999) {
                    check = false; // mark input as invalid
                    System.out.println("Card ID is from 10000000 to 10099999");
                }

            } catch (InputMismatchException ex) {
                check = false; // mark input as invalid
                System.out.println("Please input 8-digit numbers only for card ID! ");
            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }

            // execute query to get all data from the inputted card ID
            rs = stmt.executeQuery("SELECT users.id, card_id, pin, contact_number, gender,"
                    + " address, users.name, role_id FROM users JOIN user_role ON users.id= user_id WHERE card_id =" + card_id);

            // if the card ID has all of the info associated (i.e the account has been created before)
            if (rs.next()) {

                System.out.println("Please enter PIN:");

                // loop until PIN is correctly inputted
                do {
                    try {
                        Scanner input = new Scanner(System.in);
                        check = true; // by default input is valid

                        pin = input.nextInt();
                        input.nextLine();

                        if (pin < 1000 || pin > 9999) {
                            check = false; // mark input as invalid
                            System.out.println("PIN consists of 4 numbers! ");
                        }

                    } catch (InputMismatchException e) {
                        check = false; // mark input as invalid
                        System.out.println("Only numbers here !!");
                    } catch (Exception e) {
                        check = false; // mark input as invalid
                        System.out.println("An error occured! Please try again later! ");
                    }
                } while (!check);

            } else {
                System.out.println("Card ID not found !");
                check = false; // mark input as invalid
            }
        } while (!check);

        // if the card ID and PIN are both correct, return user details
        if (card_id == rs.getInt(2) && pin == rs.getInt(3)) {

            UserInfo user = new UserInfo(rs.getInt(1), rs.getString(7), rs.getInt(8));
            return user;

            // else return nothing (a user with no info at all)
        } else {
            UserInfo user2 = new UserInfo();
            return user2;
        }
    }
}

// this class is used as a base for admin menu and user menu, and also it stores 1 common method from both mentioned menus
class Menu {

    public Menu() {
    }

    /**
     * change password for an user (either an administrator or normal user)
     *
     * @param user (user information from class UserInfo)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void changePassword(UserInfo user) throws SQLException, ClassNotFoundException {
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // store the queries' results
        ResultSet rS = stmt.executeQuery("Select * FROM users WHERE id=" + user.user_id);
        rS.next();

        // temporary variables to store new PIN, new PIN confirmation from input and PIN from database (of user in the param)
        int PIN = rS.getInt(3);
        int newPIN1 = 0;
        int newPIN2 = 0;
        boolean check = true; // validate input

        System.out.print("Please input current PIN: ");

        // loop until PIN is a valid number from 1000 to 9999
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                PIN = input.nextInt();
                input.nextLine();

                if (PIN < 1000 || PIN > 9999) {
                    check = false; // mark input as invalid
                    System.out.println("PIN is from 1000 to 9999! ");
                }

            } catch (InputMismatchException ex) {
                check = false; // mark input as invalid
                System.out.println("Please input PIN as a number of 4 digits! ");
            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        // store the queries' results
        ResultSet rS1 = stmt.executeQuery("SELECT pin FROM users WHERE id =" + user.user_id);
        rS1.next();

        // if the inputted PIN is the same as the PIN stored in database
        if (PIN == rS1.getInt(1)) {
            System.out.print("Please enter new PIN: ");
            // loop until new PIN is a valid number from 1000 to 9999
            do {
                try {
                    Scanner input1 = new Scanner(System.in);
                    check = true; // by default input is valid
                    newPIN1 = input1.nextInt();

                    if (newPIN1 < 1000 || newPIN1 > 9999) {
                        check = false; // mark input as invalid
                        System.out.println("PIN is from 1000 to 9999! ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input PIN as a number of 4 digits! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later! ");
                }
            } while (!check);

            // loop until new PIN confirmation is a valid number from 1000 to 9999
            do {
                System.out.print("Please confirm new PIN : ");
                // loop until PIN is a valid number from 1000 to 9999
                do {
                    try {
                        Scanner input2 = new Scanner(System.in);
                        check = true; // by default input is valid

                        newPIN2 = input2.nextInt();

                        if (newPIN2 < 1000 || newPIN2 > 9999) {
                            check = false; // mark input as invalid
                            System.out.println("PIN is from 1000 to 9999! ");
                        }

                    } catch (InputMismatchException ex) {
                        check = false; // mark input as invalid
                        System.out.println("Please input PIN as a number of 4 digits! ");
                    } catch (Exception ex) {
                        check = false; // mark input as invalid
                        System.out.println("An error occured! Please try again later! ");
                    }
                } while (!check);

                // if the inputted new PIN and new PIN confirmation are the same
                if (newPIN1 == newPIN2) {
                    stmt.executeUpdate("UPDATE users SET pin= " + newPIN1 + " WHERE id= " + user.user_id);
                    check = true; // mark input as valid
                    System.out.println("PIN change successfully");
                } else {
                    System.out.println("New PIN confirmation doesn't match!");
                    check = false; // mark input as invalid
                }
            } while (!check);
            // if the inputted PIN is not the same as the PIN stored in the database
        } else {
            System.out.println("Wrong PIN!!");
        }
    }

}

// this class is used to display the user menu and includes user menu related methods
class userMenu extends Menu {

    /**
     * display the user menu
     *
     * @param user (user information from class UserInfo)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void displayUserMenu(UserInfo user) throws SQLException, ClassNotFoundException {
        int choice = 0; // store the input choice
        boolean check = true; // validate input

        // loop until user chooses to exit
        do {
            System.out.println("------------ WELCOME TO WIBU ATM ------------");
            System.out.print("1. Deposit                ");
            System.out.println("2. Withdrawal");
            System.out.print("3. Balance Enquiry        ");
            System.out.println("4. Change Password.");
            System.out.println("5. Exit");
            System.out.print("Please input your choice: ");

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 5) {
                        check = false; // mark input as invalid
                        System.out.println("Please input a number from 1 to 5! ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            switch (choice) {
                case 1:
                    deposit(user);

                    break;

                case 2:
                    withdraw(user);

                    break;

                case 3:
                    performBalanceEnquiry(user);

                    break;

                case 4:
                    changePassword(user);

                    break;

                case 5:
                    System.out.println("Exiting... Thank you for using our service!! ");

            }
        } while (choice != 5);
    }

    /**
     * perform deposits for user
     *
     * @param user (user information from class UserInfo)
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void deposit(UserInfo user) throws ClassNotFoundException, SQLException {
        boolean check = true; // validate input
        int money = 0; // store input money

        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // store the queries' result
        ResultSet rS = stmt.executeQuery("Select * FROM setting WHERE id=1");
        rS.next();

        // temporarily store the max money allowed to deposit and the max number of deposit allowed to make
        int maxDepositMoney = rS.getInt(1);
        int maxDepositLimit = rS.getInt(2);

        // store the queries' result
        ResultSet rS1 = stmt.executeQuery("Select count(*) FROM user_deposit WHERE user_id=" + user.user_id + " AND created_at LIKE '" + java.time.LocalDate.now() + "%'");
        rS1.next();

        // temporarily store the number of deposits has been made in current day
        int count = rS1.getInt(1);

        // store the queries' result
        ResultSet rS2 = stmt.executeQuery("SELECT total_money FROM user_money WHERE user_id =" + user.user_id);
        rS2.next();

        // temporarily store the current money of user (available in balance)
        long currentMoney = rS2.getInt(1);

        System.out.println("Your balance: " + currentMoney);

        // if the number of deposits already at limit
        if (count == maxDepositLimit) {
            System.out.println("Sorry, you have reach the limit of deposit times today.");
        } else {
            System.out.println("Please enter the amount of money you want to deposit: ");

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    money = input.nextInt();
                    input.nextLine();

                    if (money < 0 || money > maxDepositMoney) {
                        check = false; // mark input as invalid
                        System.out.println("Error!! Input number must be in range from 0 to " + maxDepositMoney);
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            // execute SQL statements
            stmt.executeUpdate("INSERT INTO user_deposit (deposit_money,user_id,created_at,type) VALUES (" + money + "," + user.user_id + ",'" + java.time.LocalDate.now() + "',0)");
            stmt.executeUpdate("UPDATE user_money SET total_money = " + (currentMoney + money) + " WHERE user_id = " + user.user_id);

            System.out.println("Deposit successfully!!!");
            System.out.println("Your current balance: " + (currentMoney + money));
        }
    }

    /**
     * perform withdrawals for user
     *
     * @param user (user information from class UserInfo)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void withdraw(UserInfo user) throws SQLException, ClassNotFoundException {
        boolean check = true; // validate input
        int money = 0; // store input money

        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // store the queries' result
        ResultSet rS = stmt.executeQuery("Select * FROM setting WHERE id=1");
        rS.next();

        // temporarily store the max money allowed to withdraw and the max number of withdrawal allowed to make
        int maxWithdrawMoney = rS.getInt(1);
        int maxWithdrawLimit = rS.getInt(2);

        // store the queries' result
        ResultSet rS1 = stmt.executeQuery("Select count(*) FROM user_withdraw WHERE user_id=" + user.user_id + " AND created_at LIKE '" + java.time.LocalDate.now() + "%'");
        rS1.next();

        // temporarily store the number of deposits has been made in current day
        int count = rS1.getInt(1);

        // store the queries' result
        ResultSet rS2 = stmt.executeQuery("SELECT total_money FROM user_money WHERE user_id =" + user.user_id);
        rS2.next();

        // temporarily store the current money of user (available in balance)
        long currentMoney = rS2.getInt(1);
        System.out.println("Your balance: " + currentMoney);

        // if the number of withdrawals already at limit
        if (count == maxWithdrawLimit) {
            System.out.println("Sorry, you have reach the limit of withdrawal times today.");
        } else {
            System.out.println("Please enter the amount of money you want to withdraw: ");

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    money = input.nextInt();
                    input.nextLine();

                    if (money < 0 || money > maxWithdrawMoney) {
                        check = false; // mark input as invalid
                        System.out.println("Error!! Input number must be in range from 0 to " + maxWithdrawMoney);
                    }
                    if (money > currentMoney) {
                        check = false; // mark input as invalid
                        System.out.println("You don't have enough money in current balance to withdraw!");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            // execute SQL statements
            stmt.executeUpdate("INSERT INTO user_withdraw (withdraw_money,user_id,created_at,type) VALUES (" + money + "," + user.user_id + ",'" + java.time.LocalDate.now() + "',1)");
            stmt.executeUpdate("UPDATE user_money SET total_money = " + (currentMoney - money) + " WHERE user_id = " + user.user_id);

            System.out.println("Withdrawal successfully!!!");
            System.out.println("Your current balance: " + (currentMoney - money));

        }
    }

    /**
     * perform balance enquiry for user
     *
     * @param user (user information from class UserInfo)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void performBalanceEnquiry(UserInfo user) throws SQLException, ClassNotFoundException {
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // store the queries' result
        ResultSet rS = stmt.executeQuery("SELECT name, gender, card_id, contact_number, address, total_money FROM users JOIN user_money on users.id=user_id WHERE users.id=" + user.user_id);
        rS.next();

        // print out the name of current user
        System.out.println("Name: " + rS.getString(1));

        // print out whether if gender is male or female
        if (rS.getInt(2) == 1) {
            System.out.println("Gender: Male");
        } else {
            System.out.println("Gender: Female");
        }

        // print out the card ID, contact number, address and balance enquiry
        System.out.println("Card ID: " + rS.getInt(3));
        System.out.println("Contact number: " + rS.getString(4));
        System.out.println("Address: " + rS.getString(5));
        System.out.println("Balance Enquiry: " + rS.getString(6));

        // display transaction history
        // store the queries' result
        ResultSet setting = stmt.executeQuery("Select * FROM setting WHERE id=1");
        setting.next();

        // store the num of transactions to display from database
        int numTransDisplay = setting.getInt(5);

        // count the number of tra
        int countTrans = 0;

        // store the queries' result
        ResultSet historyTrans = stmt.executeQuery("SELECT * FROM user_withdraw WHERE user_id=" + user.user_id + " \n"
                + "UNION ALL\n"
                + "SELECT * FROM user_deposit   \n"
                + "WHERE  user_id=" + user.user_id + " \n"
                + "ORDER BY created_at DESC");
        System.out.println("");
        if (historyTrans.first()) {
            System.out.println("Top " + numTransDisplay + " latest transactions!");

            while (historyTrans.next()) {
                if (historyTrans.getInt(5) == 0) {
                    System.out.println("Deposit  | Amount : " + historyTrans.getInt(3) + " | Date: " + historyTrans.getDate(4));

                } else {
                    System.out.println("Withdraw | Amount : " + historyTrans.getInt(3) + " | Date: " + historyTrans.getDate(4));
                }

                countTrans++;

                if (countTrans == numTransDisplay) {
                    break;
                }
            }
        } else {
            System.out.println("There are no transactions in the input date to display!");
        }
    }
}

// this class is used to display menu for the admins and includes admin menu related methods
class adminMenu extends Menu {

    // random from 10000000 to 10099999
    public int randomCardID() {
        // set the seed as the current system time in millis
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt(100000) + 10000000;
    }

    // random from 1000 to 9999
    public int randomPIN() {
        // set the seed as the current system time in millis
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt(9000) + 1000;
    }

    /**
     * create account for an user (either an administrator or normal user)
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void createAccount() throws SQLException, ClassNotFoundException {
        boolean check = true; // validate input
        int roleID = 2; // store input role ID for user (1 for Admin and 2 for User), by default it's user

        System.out.println("Do you want to create an account for an admin or a user?");

        // loop until role ID is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                System.out.print("Input your choice (1 - Admin or 2 - User): ");

                roleID = input.nextInt();
                input.nextLine();

                if (roleID != 1 && roleID != 2) {
                    check = false; // mark input as invalid
                    System.out.println("Please input 1 (Admin) or 2 (User) only! ");
                }

            } catch (InputMismatchException ex) {
                check = false; // mark input as invalid
                System.out.println("Please input an integer! ");
            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        int cardID;  // store the randomized card ID
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // loop until the randomized card ID is unique
        do {
            cardID = randomCardID(); // make sure the card ID 
            ResultSet getCardID = stmt.executeQuery("SELECT * FROM users WHERE card_id = " + cardID);
            // if there is already such card ID in database, random again
            if (getCardID.next()) {
                check = false;
            }

        } while (!check);

        // store the randomized PIN
        int PIN = randomPIN();

        // temporarily store the inputted name, contact number, gender and address (user info)
        String name = "";
        String contactNumber = "";
        int gender = 1;
        String address = "";

        System.out.println("Filling the new account's basic details: ");

        // input and check account's name
        System.out.print("Input the account's name: ");

        // loop until account's name is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                name = input.nextLine();

                // if inputted name is empty
                if (name.isEmpty()) {
                    check = false; // mark input as invalid
                    System.out.println("Name must not be empty! ");
                }

            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        // input and check account's contactNumber
        System.out.print("Input the account's contact number: ");

        // loop until account's contact number is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                contactNumber = input.nextLine();

                // if inputted contact number is empty
                if (contactNumber.isEmpty()) {
                    check = false; // mark input as invalid
                    System.out.println("Contact number must not be empty! ");
                }
                if (contactNumber.length() != 10) {
                    check = false; // mark input as invalid
                    System.out.println("Contact number must have 10 digits! ");
                }

                // check if there is a character in string not a number
                for (int i = 0; i < contactNumber.length(); i++) {
                    if (contactNumber.charAt(i) < 48 || contactNumber.charAt(i) > 57) {
                        check = false; // mark input as invalid
                        System.out.println("Please input numbers only! ");
                        break;
                    }
                }

            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        // input and check account's gender
        System.out.print("Input the account's gender (0 - Female or 1 - Male): ");

        // loop until account's gender is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                gender = input.nextInt();
                input.nextLine();

                if (gender != 0 && gender != 1) {
                    check = false; // mark input as invalid
                    System.out.println("Please input 0 (female) or 1 (male)! ");
                }

            } catch (InputMismatchException ex) {
                check = false; // mark input as invalid
                System.out.println("Please input an integer! ");
            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        // input and check account's address
        System.out.print("Input the account's address: ");

        // loop until account's address is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                address = input.nextLine();

                // if inputted address is empty
                if (address.isEmpty()) {
                    check = false; // mark input as invalid
                    System.out.println("Address must not be empty! ");
                }

            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later! ");
            }
        } while (!check);

        // execute SQL statements
        stmt.executeUpdate("INSERT INTO users(card_id, pin, contact_number, gender, address, name) VALUES(" + cardID + "," + PIN + ", \"" + contactNumber + "\" ," + gender + ", \"" + address + "\" , \"" + name + "\")");

        // store the SQL statements' results
        ResultSet insertInfo = stmt.executeQuery("SELECT id FROM users WHERE card_id = " + cardID);
        insertInfo.next();

        int userID = insertInfo.getInt(1); // temporarily store the user ID which was just generated

        // execute SQL statements
        stmt.executeUpdate("INSERT INTO user_role(user_id, role_id) VALUES(" + userID + "," + roleID + ")");
        stmt.executeUpdate("INSERT INTO user_money(user_id, total_money) VALUES(" + userID + "," + 0 + ")");

    }

    /**
     * sub-method to change deposit related limits
     *
     * @param mode as operation mode (1 for changing deposit value and 2 for
     * changing number of deposits)
     * @param depositLimit value of deposits to change
     * @param depositNumLimit value of number of deposits to change
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void changeDepositLimit(int mode, int depositLimit, int depositNumLimit) throws SQLException, ClassNotFoundException {
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // if it's in the mode for changing deposit limit
        if (mode == 1) {
            // set depositLimit in database to inputted deposit limit
            stmt.executeUpdate("UPDATE setting SET deposit_lim=" + depositLimit + " WHERE ID=1");
        } else {
            // set depositNumLimit in database to inputted number of deposits limit
            stmt.executeUpdate("UPDATE setting SET num_deposit_lim=" + depositNumLimit + " WHERE ID=1");
        }
    }

    /**
     * change deposit related limits (deposit amount limit and the number of
     * deposits limit)
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void changeDepositRelatedLimits() throws SQLException, ClassNotFoundException {
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

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 5) {
                        check = false; // mark input as invalid
                        System.out.println("Please input a number from 1 to 5 ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            switch (choice) {
                case 1:
                    System.out.println("Please input deposit amount limit: ");

                    // loop until depositLimit is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid
                            depositLimit = input.nextInt();
                            input.nextLine();

                            if (depositLimit < 25000) {
                                check = false; // mark input as invalid
                                System.out.println("Please input a number equal to or larger than 25000! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please input a number ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later!");
                        }
                    } while (!check);

                    changeDepositLimit(1, depositLimit, 0);

                    System.out.println("Change the deposit limit successfully! ");

                    break;

                case 2:
                    System.out.println("Please input number of deposits limit: ");

                    // loop until depositNumLimit is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid
                            depositNumLimit = input.nextInt();
                            input.nextLine();

                            if (depositNumLimit < 5) {
                                check = false; // mark input as invalid
                                System.out.println("Please input a number equal to or larger than 5! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please input a number ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later!");
                        }
                    } while (!check);

                    changeDepositLimit(2, 0, depositNumLimit);

                    System.out.println("Change the number of deposits limit successfully! ");

                    break;

                case 3:
                    changeDepositLimit(1, 25000, 0);

                    System.out.println("Revert deposit limit to default value successfully! ");

                    break;

                case 4:
                    changeDepositLimit(2, 0, 5);

                    System.out.println("Revert the number of deposits limit to default value successfully! ");

                    break;

                case 5:
                    System.out.println("Exit changing deposit-related limits...");

            }
        } while (choice != 5);
    }

    /**
     * sub-method to change withdrawal related limits
     *
     * @param mode as operation mode (1 for changing deposit value and 2 for
     * changing number of withdrawals)
     * @param withdrawLimit value of withdrawals to change
     * @param withdrawNumLimit value of number of withdrawals to change
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void changeWithdrawLimit(int mode, int withdrawLimit, int withdrawNumLimit) throws SQLException, ClassNotFoundException {
        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // if it's in the mode for changing withdrawal limit
        if (mode == 1) {
            // set withdrawLimit in database to inputted withdrawal limit
            stmt.executeUpdate("UPDATE setting SET withdraw_lim=" + withdrawLimit + " WHERE ID=1");
        } else {
            // set withdrawNumLimit in database to inputted number of withdrawals limit
            stmt.executeUpdate("UPDATE setting SET num_withdraw_lim=" + withdrawNumLimit + " WHERE ID=1");
        }
    }

    /**
     * change withdrawal related limits (withdrawal amount limit and the number
     * of withdrawals limit)
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void changeWithdrawRelatedLimits() throws SQLException, ClassNotFoundException {
        int choice = 0; // store user choice
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

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 5) {
                        check = false; // mark input as invalid
                        System.out.println("Please input a number from 1 to 5! ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            switch (choice) {
                case 1:
                    System.out.println("Please input withdrawal amount limit: ");

                    // loop until withdrawLimit is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            withdrawLimit = input.nextInt();
                            input.nextLine();

                            if (withdrawLimit < 25000) {
                                check = false; // mark input as invalid
                                System.out.println("Please input a number equal to or larger than 25000! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please input a number ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later!");
                        }
                    } while (!check);

                    changeWithdrawLimit(1, withdrawLimit, 0);

                    System.out.println("Change the withdrawal limit successfully! ");

                    break;

                case 2:
                    System.out.println("Please input number of deposits limit: ");

                    // loop until withdrawNumLimit is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            withdrawNumLimit = input.nextInt();
                            input.nextLine();

                            if (withdrawNumLimit < 5) {
                                check = false; // mark input as invalid
                                System.out.println("Please input a number equal to or larger than 5! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please input a number ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later!");
                        }
                    } while (!check);

                    changeWithdrawLimit(2, 0, withdrawNumLimit);

                    System.out.println("Change the number of withdrawals limit successfully! ");

                    break;

                case 3:
                    changeWithdrawLimit(1, 25000, 0);

                    System.out.println("Revert withdraw limit to default value successfully! ");

                    break;

                case 4:
                    changeWithdrawLimit(2, 0, 5);

                    System.out.println("Revert the number of withdrawals limit to default value successfully! ");

                    break;

                case 5:
                    System.out.println("Exit changing withdrawal-related limits...");

            }
        } while (choice != 5);
    }

    /**
     * set the number of transactions to display for user balance enquiries
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void setTransCount() throws SQLException, ClassNotFoundException {
        int transCount = 5; // store number of transactions from input (default value: 5)
        boolean check = true; // validate input

        // loop until input is valid
        System.out.print("Input the number of transactions to change: ");

        // loop until transCount is correctly inputted
        do {
            try {
                Scanner input = new Scanner(System.in);
                check = true; // by default input is valid

                transCount = input.nextInt();
                input.nextLine();

                if (transCount < 1) {
                    check = false; // mark input as invalid
                    System.out.println("The number of displayed transactions must be at least 1! ");
                }

            } catch (InputMismatchException ex) {
                check = false; // mark input as invalid
                System.out.println("Please input a number ");
            } catch (Exception ex) {
                check = false; // mark input as invalid
                System.out.println("An error occured! Please try again later!");
            }
        } while (!check);

        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // code to set number of transactions to display (for balance enquiry method)
        stmt.executeUpdate("UPDATE setting SET num_trans_display=" + transCount + " WHERE ID=1");

        System.out.println("Change the number of transactions to display successfully! ");

    }

    /**
     * create deposit report
     */
    public void createDepositReport() {
        boolean check = false; // validate input

        System.out.println("Deposit Report: ");

        Scanner input = new Scanner(System.in);

        // loop until input is valid
        do {
            try {
                System.out.print("Enter Date(yyyy-mm-dd):");

                // request to input date
                String date = input.nextLine();

                // set a date format for the input
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                Date inputDate = dateFormat.parse(date);

                check = true; // by default input is valid

                Connection conn;
                try {
                    // connect database
                    conn = DataConnection.getConnection();
                    Statement stmt = conn.createStatement();

                    // store the queries' result
                    ResultSet rS = stmt.executeQuery("SELECT  card_id, name, deposit_money, total_money  \n"
                            + "From user_deposit w \n"
                            + "	join users u \n"
                            + "		on w.user_id=u.id \n"
                            + "	JOIN user_money m \n"
                            + "		on w.user_id = m.user_id\n"
                            + " where created_at like '" + dateFormat.format(inputDate) + "%'");

                    int i; // variable to mark the fields to print

                    // print in the format stated
                    System.out.format("|%8s | %32s | %15s | %13s|\n", "Card_id", " Name", " Deposit Amount", "Balance");

                    // while the next line in the result set is not empty
                    while (rS.next()) {
                        i = 1;

                        // print in the format stated
                        System.out.format("|%8s | %32s | %15s | %13s|\n", rS.getString(i++), rS.getString(i++), rS.getString(i++), rS.getString(i++));
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Cannot connect to database. ");
                }

            } catch (ParseException ex) {
                System.out.println("Please enter in the right format!!!");
                check = false; // mark input as invalid
            }
        } while (!check);
    }

    /**
     * create withdrawal report
     */
    public void createWithdrawReport() {
        boolean check = false; // validate input

        System.out.println("Withdrawal Report: ");

        Scanner input = new Scanner(System.in);

        // loop until input is valid
        do {
            try {
                System.out.print("Enter Date(yyyy-mm-dd):");

                // request to input date
                String date = input.nextLine();

                // set a date format for the input
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                Date inputDate = dateFormat.parse(date);

                check = true; // by default input is valid

                Connection conn;
                try {
                    // connect to database
                    conn = DataConnection.getConnection();
                    Statement stmt = conn.createStatement();

                    // store the queries' result
                    ResultSet rS = stmt.executeQuery("SELECT  card_id, name, withdraw_money, total_money  \n"
                            + "From user_withdraw w \n"
                            + "	join users u \n"
                            + "		on w.user_id=u.id \n"
                            + "	JOIN user_money m \n"
                            + "		on w.user_id = m.user_id\n"
                            + " where created_at like '" + dateFormat.format(inputDate) + "%'");

                    int i; // variable to mark the fields to print

                    // print in the format stated
                    System.out.format("|%8s | %32s | %15s | %13s|\n", "Card_id", " Name", " Withdraw Amount", "Balance");

                    // while the next line in the result set is not empty
                    while (rS.next()) {
                        i = 1;

                        // print in the format stated
                        System.out.format("|%8s | %32s | %15s | %13s|\n", rS.getString(i++), rS.getString(i++), rS.getString(i++), rS.getString(i++));
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Cannot connect to database. ");
                }

            } catch (ParseException ex) {
                System.out.println("Please enter in the right format!!!");
                check = false; // mark input as invalid
            }
        } while (!check);
    }

    /**
     * create account report
     */
    public void createAccountReport() {
        Connection conn;

        try {
            // connect to database
            conn = DataConnection.getConnection();
            Statement stmt = conn.createStatement();

            // store the queries' result
            ResultSet rS = stmt.executeQuery("select *\n"
                    + "from users u \n"
                    + "	join user_role r on u.id = r.user_id\n");

            int i; // variable to mark the fields to print

            // mark input as invalid
            System.out.format("|%5s | %8s | %4s | %14s | %6s | %32s | %32s| %12s|\n", "Id", "Card_id", "Pin", "Contact Number", "Gender", "Address", "Name", "Account Type");
            // while the next line in the result set is not empty
            while (rS.next()) {
                i = 1;
                System.out.format("|%5s | %8s | %4s | %14s | %6s | %32s | %32s| %12s|\n", rS.getString(i++), rS.getString(i++), rS.getString(i++), rS.getString(i++), (rS.getInt(i++) == 1) ? "Male" : "Female", rS.getString(i++), rS.getString(i++), (rS.getInt(9) == 2) ? "User" : "Admin");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Cannot connect to database. ");
        }
    }

    /**
     * change user info (for either an administrator or an user)
     *
     * @params user (user information from class UserInfo)
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void changeUserInfo(UserInfo user) throws ClassNotFoundException, SQLException {
        boolean check = true; // by default input is valid
        int choice = 0; // store input choice

        // connect to database
        Connection conn = DataConnection.getConnection();
        Statement stmt = conn.createStatement();

        // temporarily store inputted values
        int cardID = 0;
        int pin = 0;
        String name = "";
        String contactNumber = "";
        int gender = 1;
        String address = "";
        char confirm = 'Y'; // store the inputted confirmation (Y or N)
        String masterPasword = ""; // store the inputted master password (in case an admin wants to change other admin info)

        // loop until card ID is valid and exists in database
        do {
            System.out.print("Please input user's card ID: ");

            // loop until card ID is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    cardID = input.nextInt();
                    input.nextLine();

                    if (cardID < 10000000 || cardID > 10099999) {
                        check = false; // mark input as invalid
                        System.out.println("Card ID is from 10000000 to 10099999");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input user card ID as a number of 8 digits! ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later! ");
                }
            } while (!check);

            // store the queries' result 
            ResultSet rS = stmt.executeQuery("SELECT card_id,pin,name,contact_number,gender,address,role_id"
                    + " FROM users JOIN user_role on users.id=user_id where card_id = " + cardID);

            // if the card ID exists in the database
            if (rS.next()) {
                // if the card ID is of an admin, request an input for master password
                if (rS.getInt(7) == 1) {
                    // store the queries' result
                    ResultSet rSmpass = stmt.executeQuery("SELECT master_password FROM setting WHERE id=1");
                    rSmpass.next();

                    // confirm if admin knows the master password
                    System.out.println("Do you really remember the master password?");
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            confirm = input.next().charAt(0);
                            input.nextLine();

                            if (confirm != 'Y' && confirm != 'N') {
                                check = false; // mark input as invalid
                                System.out.println("Enter Y or N only! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please enter 1 character only ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // run this only if user chooses 'Y'
                    if (confirm == 'Y') {
                        int failedCount = 0; // initialize a variable to store the number of failed inputs

                        System.out.print("Please input master password: ");
                        // loop until master password is inputted correctly and matches the one in database
                        do {
                            try {
                                Scanner input = new Scanner(System.in);
                                check = true; // by default input is valid

                                masterPasword = input.nextLine();
                                input.nextLine();

                                // if master password is not correct, request input again
                                if (masterPasword.compareTo(rSmpass.getString(1)) != 0) {
                                    check = false; // mark input as invalid
                                    failedCount++; // increase the number of failed inputs
                                    System.out.println("Master Password is incorrect, please try again! ");
                                }

                                // if an admin input the password wrong 3 times
                                if (failedCount == 3) {
                                    System.out.println("You have exceeded the number of failed attempts");
                                    System.out.println("Exitting to admin menu...");
                                    displayAdminMenu(user);
                                }

                            } catch (InputMismatchException ex) {
                                check = false; // mark input as invalid
                                System.out.println("Please input master password as a string! ");
                            } catch (Exception ex) {
                                check = false; // mark input as invalid
                                System.out.println("An error occured! Please try again later! ");
                            }
                        } while (!check);
                    }
                }

                // assign the current info to temp variables
                name = rS.getString(3);
                contactNumber = rS.getString(4);
                gender = rS.getInt(5);
                address = rS.getString(6);
                pin = rS.getInt(2);
                check = true; // mark input as valid

                // if the card ID does not exist in the database
            } else {
                System.out.println("Card ID not found!!");
                check = false; // mark input as invalid
            }
        } while (!check);

        // loop until exit is chosen
        do {
            System.out.println("Select one of following options to change for user: ");
            System.out.println("1. Change PIN");
            System.out.println("2. Change name ");
            System.out.println("3. Change contact number");
            System.out.println("4. Change gender");
            System.out.println("5. Change address");
            System.out.println("6. Exit");
            System.out.println("Please input your choice: ");

            // loop until the inputted choice is valid
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 6) {
                        check = false; // mark input as invalid
                        System.out.println("Please input a number from 1 to 6 ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
                    System.out.println("An error occured! Please try again later!");
                }
            } while (!check);

            switch (choice) {
                case 1:
                    System.out.println("Input new PIN: ");

                    // loop until PIN is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            pin = input.nextInt();
                            input.nextLine();

                            if (pin < 1000 || pin > 9999) {
                                check = false; // mark input as invalid
                                System.out.println("PIN consists of 4 numbers! ");
                            }

                        } catch (InputMismatchException e) {
                            check = false; // mark input as invalid
                            System.out.println("Only numbers here !!");
                        } catch (Exception e) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // SQL statements to update in database
                    stmt.executeUpdate("UPDATE users SET pin= " + pin + " WHERE card_id= " + cardID);

                    System.out.println("Change user's pin successfully!!");

                    break;

                case 2:
                    // input and check account's name
                    System.out.print("Input new name: ");

                    // loop until account's name is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            name = input.nextLine();

                            // if inputted name is empty
                            if (name.isEmpty()) {
                                check = false; // mark input as invalid
                                System.out.println("Name must not be empty!");
                            }

                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // SQL statements to update in database
                    stmt.executeUpdate("UPDATE users SET name = '" + name + "' WHERE card_id= " + cardID);

                    System.out.println("Change user's name successfully!!");

                    break;

                case 3:
                    // input and check account's contactNumber
                    System.out.print("Input new contact number: ");

                    // loop until account's contact number is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            contactNumber = input.nextLine();

                            // check if inputted contact number is empty
                            if (contactNumber.isEmpty()) {
                                check = false; // mark input as invalid
                                System.out.println("Contact number must not be empty!");
                            }
                            // check if there is a character in string not a number
                            for (int i = 0; i < contactNumber.length(); i++) {
                                if (contactNumber.charAt(i) < 48 || contactNumber.charAt(i) > 57) {
                                    check = false; // mark input as invalid
                                    System.out.println("Please input numbers only! ");
                                    break;
                                }
                            }

                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // SQL statements to update in database
                    stmt.executeUpdate("UPDATE users SET contact_number= '" + contactNumber + "' WHERE card_id= " + cardID);

                    System.out.println("Change user's contact number successfully!!");

                    break;

                case 4:
                    // input and check account's gender
                    System.out.print("Input new gender (0 - Female or 1 - Male): ");

                    // loop until account's gender is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            gender = input.nextInt();
                            input.nextLine();

                            if (gender != 0 && gender != 1) {
                                check = false; // mark input as invalid
                                System.out.println("Please input 0 (female) or 1 (male)! ");
                            }

                        } catch (InputMismatchException ex) {
                            check = false; // mark input as invalid
                            System.out.println("Please input an integer! ");
                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // SQL statements to update in database
                    stmt.executeUpdate("UPDATE users SET gender= " + gender + " WHERE card_id= " + cardID);

                    System.out.println("Change user's gender successfully!!");

                    break;

                case 5:
                    // input and check account's address
                    System.out.print("Input new address: ");

                    // loop until account's address is correctly inputted
                    do {
                        try {
                            Scanner input = new Scanner(System.in);
                            check = true; // by default input is valid

                            address = input.nextLine();

                            // if inputted address is empty
                            if (address.isEmpty()) {
                                check = false; // mark input as invalid
                                System.out.println("Address must not be empty");
                            }

                        } catch (Exception ex) {
                            check = false; // mark input as invalid
                            System.out.println("An error occured! Please try again later! ");
                        }
                    } while (!check);

                    // SQL statements to update in database
                    stmt.executeUpdate("UPDATE users SET address= '" + address + "' WHERE card_id= " + cardID);

                    System.out.println("Change user's address successfully!!");

                    break;

                case 6:
                    System.out.println("Exiting changing user info ...");
            }
        } while (choice != 6);

    }

    /**
     * display the menu for the administrators
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void displayAdminMenu(UserInfo user) throws SQLException, ClassNotFoundException {
        int choice = 0; // store user input choice
        boolean check = true; // validate if input is valid (by default input is valid)

        // loop until exit is chosen
        do {
            System.out.println("------------ ADMINISTRATION ------------");
            System.out.println("1. Create new user account");
            System.out.println("2. Change deposit-related limitations");
            System.out.println("3. Change withdrawal-related limitations");
            System.out.println("4. Change the number of last transactions to display");
            System.out.println("5. Create deposit report");
            System.out.println("6. Create withdrawal report");
            System.out.println("7. Create account report");
            System.out.println("8. Change user info");
            System.out.println("9. Change current account's password");
            System.out.println("10. Exit");
            System.out.print("Input your choice: ");

            // loop until choice is correctly inputted
            do {
                try {
                    Scanner input = new Scanner(System.in);
                    check = true; // by default input is valid

                    choice = input.nextInt();
                    input.nextLine();

                    if (choice < 1 || choice > 10) {
                        check = false; // mark input as invalid
                        System.out.println("Please input a number from 1 to 10 ");
                    }

                } catch (InputMismatchException ex) {
                    check = false; // mark input as invalid
                    System.out.println("Please input a number ");
                } catch (Exception ex) {
                    check = false; // mark input as invalid
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
                    createWithdrawReport();

                    break;
                case 7:
                    createAccountReport();

                    break;

                case 8:
                    changeUserInfo(user);

                    break;

                case 9:
                    changePassword(user);

                    break;

                case 10:
                    System.out.println("Exiting the administration menu...");

            }
        } while (choice != 10);
    }

}

// this class includes the main method
public class ATM {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("-------- WIBU BANK ----------");

        boolean check = true; // validate input

        // loop until login credentials are correct
        do {
            try {
                check = true; // by default the login credentials are valid

                UserInfo user = Auth.loginUser();

                // create the objects for admin and user menus
                adminMenu admin_menu = new adminMenu();
                userMenu users_menu = new userMenu();

                switch (user.getRole_id()) {
                    case 1:
                        System.out.println("Admin login successfully!");
                        System.out.println("Hello " + user.getUser_name());
                        admin_menu.displayAdminMenu(user);
                        break;
                    case 2:
                        System.out.println("User login successfully!!");
                        System.out.println("Hello " + user.getUser_name());
                        users_menu.displayUserMenu(user);
                        break;
                    case 0:
                        System.out.println("Card ID or PIN incorrect !!");
                        check = false; // mark the login session as invalid

                        break;
                }

            } catch (NullPointerException | SQLException ex) {
                System.out.println("Cannot connect to database.");
            } catch (InputMismatchException e) {
                System.out.println("Input numbers only!");
            }
        } while (!check);

    }

}
