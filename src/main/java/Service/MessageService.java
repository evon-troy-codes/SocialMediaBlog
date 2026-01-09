package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage(Message message) {
        try {
            if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
                return null;
            }

            if (message.getMessage_text().length() >= 255) {
                return null;
            }

            if (!accountDAO.accountExists(message.getPosted_by())) {
                return null;
            }

            return messageDAO.insertMessage(message);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getAllMessages() {
        try {
            return messageDAO.getAllMessages();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message getMessageById(int message_id) {
        try {
            return messageDAO.getSingleMessage(message_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteMessage(int message_id) {
        try {
            return messageDAO.deleteMessageByID(message_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateMessage(int message_id, String message_text) {
        try {
            if (message_text == null || message_text.isBlank()) {
                return 0;
            }

            if (message_text.length() >= 255) {
                return 0;
            }

            return messageDAO.updateMessage(message_id, message_text);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        try {
            return messageDAO.getMessageByAccountId(account_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}