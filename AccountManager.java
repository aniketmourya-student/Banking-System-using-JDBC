package GitFiles.JDBC;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debit_money(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
                statement.setLong(1,account_number);
                statement.setString(2,security_pin);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance) {
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
                        PreparedStatement preparedStatement = connection.prepareStatement(debit_query);
                        preparedStatement.setDouble(1,amount);
                        preparedStatement.setString(2,security_pin);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if(rowsAffected > 0) {
                            System.out.println("Rs " + amount + " debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void credit_money(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
                statement.setLong(1,account_number);
                statement.setString(2,security_pin);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()) {
                       if(amount!=0) {
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
                        PreparedStatement preparedStatement = connection.prepareStatement(credit_query);
                        preparedStatement.setDouble(1,amount);
                        preparedStatement.setLong(2,account_number);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if(rowsAffected > 0) {
                            System.out.println("Rs " + amount + " credited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Amount Should Be Greater than 0!");
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Your Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?;");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance is: " + balance);
            } else {
                System.out.println("Invalid Pin");
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferMoney(long sender_acc_number) {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long reciever_acc_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1,sender_acc_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(sender_acc_number!=0 && reciever_acc_number!=0) {
                double current_balance = resultSet.getDouble("balance");
                if(amount <= current_balance) {
                    String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
                    String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
                    PreparedStatement debitStatement = connection.prepareStatement(debitQuery);
                    PreparedStatement creditStatement = connection.prepareStatement(creditQuery);
                    debitStatement.setDouble(1,amount);
                    debitStatement.setLong(2,sender_acc_number);
                    creditStatement.setDouble(1,amount);
                    creditStatement.setLong(2,reciever_acc_number);
                    int debitRowsAffected = debitStatement.executeUpdate();
                    int creditRowsAffected = creditStatement.executeUpdate();
                    if(debitRowsAffected > 0 && creditRowsAffected > 0) {
                        System.out.println("Transaction Successfully!");
                        System.out.println("Rs " + " Transferred Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
