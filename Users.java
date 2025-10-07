package com.aniket.advjava.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class Users {
    private Connection connection;
    private Scanner scanner;

    public Users(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {
        System.out.println();
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if(user_exist(email)) {
            System.out.println("User already exists for this Email Address!");
            return;
        }
        String query = "INSERT INTO users(full_name, email, password) VALUES (?,?,?);";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("User Registered Successfully");
            } else {
                System.out.println("Registration Failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return email;
            } else {
                return null;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exist(String email) {
        String query = "SELECT * FROM users WHERE email = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
