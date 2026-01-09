package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO{

    /** Check if username already exists in the database
     *@param username the username to check
     *@return true if username exists already
     */

    public boolean usernameExists(String username) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT 1 FROM account WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);

        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    /** Insert a new account into the DB
     * @param account the account to insert
     * @return the account with the generated account_id, null if insertion fails
     * @throws SQLException
     *
     */

    public Account insertAccount(Account account) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();

        if (resultSet.next()){
            int generated_account_id = resultSet.getInt(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }

        return null;

    }

    /**
     * Retrieves an account by username and password
     * @param username the username
     * @param password the password
     * @return the account if the credentials match an account in the DB
     * @throws SQLException
     */

    public Account getAccountByCredentials(String username, String password) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return new Account(resultSet.getInt("account_id"),resultSet.getString("username"), resultSet.getString("password"));
        }
        return null;

    }

    /**
     * Validate that an account exists
     * @param account_id
     * @return true if an account exists, false otherwise
     * @throws SQLException
     */

    public boolean accountExists(int account_id) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, account_id);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }

}