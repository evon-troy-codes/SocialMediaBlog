package Controller;

import java.sql.SQLException;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getSingleMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByIdHandler);

        return app;
    }

    private void registerHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        AccountService accountService = new AccountService();

        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            context.json(registeredAccount);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context){
        Account account = context.bodyAsClass(Account.class);
        AccountService accountService = new AccountService();

        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());

        if (loggedInAccount != null){
            context.json(loggedInAccount);
            context.status(200);
        } else {
            context.status(401);
        }
    }


    private void createMessageHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);
        MessageService messageService = new MessageService();

        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            context.json(createdMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context){
        MessageService messageService = new MessageService();

        List<Message> messages = messageService.getAllMessages();

        if (messages != null) {
            context.json(messages);
        }
        context.status(200);
    }

    private void getSingleMessageHandler(Context context){
        MessageService messageService = new MessageService();
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.getMessageById(message_id);

        if (message != null){
            context.json(message);
        }
        context.status(200);
    }

    public void deleteMessageByIdHandler(Context context){
        MessageService messageService = new MessageService();
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.getMessageById(message_id);

        if (message != null) {
            messageService.deleteMessage(message_id);
            context.json(message);
        }

        context.status(200);
    }

    public void updateMessageHandler(Context context){
        MessageService messageService = new MessageService();
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = context.bodyAsClass(Message.class);
        String updatedMessageText = message.getMessage_text();

        int rowsUpdated = messageService.updateMessage(message_id, updatedMessageText);

        if (rowsUpdated == 1){
            // Return the updated message
            Message updatedMessage = messageService.getMessageById(message_id);
            context.json(updatedMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    public void getAllMessagesByIdHandler(Context context){
        MessageService messageService = new MessageService();
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messages = messageService.getMessagesByAccountId(account_id);

        if (messages != null) {
            context.json(messages);
        }
        context.status(200);
    }
}