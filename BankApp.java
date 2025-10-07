package com.aniket.advjava.JDBC;
import java.sql.*;
import java.util.Scanner;


public class BankApp {
    private static final String url = "jdbc:postgresql://localhost:5432/banking_system";
    private static final String userName = "postgres";
    private static final String password = "Admin";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println("Could Not load Drivers because " + e.getMessage());
        }

        try(Connection con = DriverManager.getConnection(url,userName,password)) {
            Scanner scanner = new Scanner(System.in);
            Users user = new Users(con,scanner);
            Accounts accounts = new Accounts(con,scanner);
            AccountManager accountManager = new AccountManager(con,scanner);

            String email;
            String account_number;

            while(true) {
                System.out.println("Welcome to the Banking System");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
