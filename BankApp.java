package GitFiles.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankApp {
    private static final String url = "jdbc:postgresql://localhost:5432/banking_system";
    private static final String username = "postgres";
    private static final String password = "Admin";

    public static void main(String[] args) {
        // Optional in modern JDBC but kept for clarity
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found (continuing if driver auto-loaded): " + e.getMessage());
        }

        // Try-with-resources ensures connection & scanner are closed
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Scanner scanner = new Scanner(System.in)) {

            Users user = new Users(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                int choice1;
                try {
                    choice1 = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice, please enter a number.");
                    continue;
                }

                switch (choice1) {
                    case 1:
                        user.register();
                        break;

                    case 2:
                        String email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");

                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice: ");
                                int openChoice;
                                try {
                                    openChoice = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    openChoice = 2;
                                }

                                if (openChoice == 1) {
                                    long account_number = accounts.openAccount(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            long account_number = accounts.getAccountNumber(email);

                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");

                                try {
                                    choice2 = Integer.parseInt(scanner.nextLine().trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("Please enter a valid number.");
                                    continue;
                                }

                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("Logging out...");
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;

                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
