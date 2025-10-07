package com.aniket.advjava.JDBC;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:postgresql://localhost:5432/hotel_db";
    private static final String userName = "postgres";
    private static final String password = "Admin";

    private static void reserveRoom(Connection connection, Scanner scanner) {
            try {
                System.out.println("Enter guest Name: ");
                String guestName = scanner.next();
                scanner.nextLine();
                System.out.println("Enter room number: ");
                int roomNumber = scanner.nextInt();
                System.out.println("Enter contact number: ");
                String contactNumber = scanner.next();

                String sql = "INSERT INTO reservations (guest_name, room_number, contact_number)" +
                        "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
                try(Statement statement = connection.createStatement()) {
                    int affectedRows = statement.executeUpdate(sql);
                    if(affectedRows > 0) {
                        System.out.println("Reservation Successful");
                    } else {
                        System.out.println("Reservation Failed");
                    }
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
    }

    private static void viewReservations(Connection connection) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations;";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("Current Reservations");
            while(resultSet.next()) {
                int reservation = resultSet.getInt("reservation_id");
                String name = resultSet.getString("guest_name");
                int room_number = resultSet.getInt("room_number");
                String contact = resultSet.getString("contact_number");
                String date = resultSet.getTimestamp("reservation_date").toString();
                System.out.println();
                System.out.println("Guest Name: " + name);
                System.out.println("Reservation Id: " + reservation);
                System.out.println("Room Number: " + room_number);
                System.out.println("Contact: " + contact);
                System.out.println("Date: " + date);
            }
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter your reservation Id: ");
            int resId = scanner.nextInt();
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = "  + resId +
                    " AND guest_name = '" +  guestName + "'";

            try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
                if(resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for reservation id " + resId + " and Guest "
                    + guestName + " is " + roomNumber);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void updateReservation(Connection connection, Scanner scanner) {
            try {
                System.out.println("Enter Reservation Id to Update: ");
                int reservationId = scanner.nextInt();
                scanner.nextLine();
                if(!reservationExists(connection,reservationId)) {
                    System.out.println("Reservation Not found for the given Id");
                    return;
                }
                System.out.print("Enter new Guest Name: ");
                String newGuestName = scanner.nextLine();
                System.out.print("Enter new room number: ");
                int newRoomId = scanner.nextInt();
                System.out.print("Enter new Contact Number: ");
                String newContactNumber = scanner.next();

                String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "',"+
                        "room_number = " + newRoomId + ", " +
                        "contact_number = '" + newContactNumber + "'" +
                        "WHERE reservation_id = " + reservationId;
                try(Statement statement = connection.createStatement()) {
                    int affectedRows = statement.executeUpdate(sql);
                    if(affectedRows > 0) {
                        System.out.println("Reservation Updated Successfully");
                    } else {
                        System.out.println("Reservation Failed");
                    }
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    private static void deleteReservations(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Reservation Id to Delete: ");
            int resId = scanner.nextInt();
            if(!reservationExists(connection,resId)) {
                System.out.println("Reservation not found for the given ID");
                return;
            }
            String sql = "DELETE FROM reservations WHERE reservation_id = " + resId;

            try(Statement statement = connection.createStatement();) {
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows > 0) {
                    System.out.println("Reservation Deleted Successfully");
                } else {
                    System.out.println("Reservation deletion Failed");
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;
            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void exit() throws  InterruptedException {
        System.out.print("Closing connection to the DataBase!");
        int i = 5;
        while(i!=0) {
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System");
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url,userName,password);
            System.out.println("Connection to the DataBase has been Established");
            while(true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get room number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an Option: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservations(connection,scanner);
                        break;
                    case 0:
                        try {
                            exit();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        scanner.close();
                        return;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
