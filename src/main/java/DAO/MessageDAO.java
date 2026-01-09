package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    /**
     * Insert a message into the DB
     * @param message the message to insert
     * @return the message with the generated messageID, null if insertion fails
     * @throws SQLException
     */

    public Message insertMessage(Message message) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();

        if (resultSet.next()){
            int generated_message_id = (int) resultSet.getLong(1);

            return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
        return null;
    }

    /**
     * Retrieve a list of all messages found in the DB
     * @return a list of all messages retrieved from the DB - empty if no messages found
     * @throws SQLException
     */

    public List<Message> getAllMessages() throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));
            messages.add(message);

        }

        return messages;

    }

    /**
     * Get a single message by message_id
     * @param message_id the message_id to retrieve
     * @return the message, null if ID not found
     * @throws SQLException
     */

    public Message getSingleMessage(int message_id) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Message singleMessage = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));
            return singleMessage;
        }

        return null;

    }

    /**
     * Remove an existing message from the DB
     * @param message_id the message_id to be deleted
     * @return the number of rows updated
     * @throws SQLException
     */

    public int deleteMessageByID(int message_id) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);

        int rowsDeleted = preparedStatement.executeUpdate();

        return rowsDeleted;

    }

    /**
     * Update the message_text of a message
     * @param message_id the id of the message to update
     * @param message_text the message text
     * @return the number of rows updated
     * @throws SQLException
     */

    public int updateMessage(int message_id, String message_text) throws SQLException{

        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message_text);
        preparedStatement.setInt(2, message_id);

        int rowsUpdated = preparedStatement.executeUpdate();

        return rowsUpdated;

    }


    /**
     * Get all messages from user given account_id
     * @param account_id the account_id to retrieve the messages from
     * @return the messages
     * @throws SQLException
     */

    public List<Message> getMessageByAccountId (int account_id) throws SQLException {

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));
            messages.add(message);
        }

        return messages;

    }
}

