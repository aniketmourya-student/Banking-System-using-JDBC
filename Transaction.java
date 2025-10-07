package com.aniket.advjava.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Transaction {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bank";
        String userName = "postgres";
        String password = "Admin";
        String withdraw = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        String deposit = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try(Connection con = DriverManager.getConnection(url,userName,password)) {
            System.out.println("Connection to the DataBase established Successfully");
            con.setAutoCommit(false);
            PreparedStatement withdrawStatement = con.prepareStatement(withdraw);
            PreparedStatement depositStatement = con.prepareStatement(deposit);
            withdrawStatement.setDouble(1,5000.00);
            withdrawStatement.setString(2,"ACC1");
            depositStatement.setDouble(1,5000.0);
            depositStatement.setString(2,"ACC2");
            int withdrawRowsAffected = withdrawStatement.executeUpdate();
            int depositRowsAffected = depositStatement.executeUpdate();
            if(withdrawRowsAffected > 0 && depositRowsAffected > 0) {
                System.out.println("Transaction Done Successfully!");
                con.commit();
            } else {
                System.out.println("Transaction Failed");
                con.rollback();
            }
            withdrawStatement.close();
            depositStatement.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
