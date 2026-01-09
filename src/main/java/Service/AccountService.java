package Service;
import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        try {
            if (account.getUsername() == null || account.getUsername().isBlank()) {
                return null;
            }

            if (account.getPassword() == null || account.getPassword().length() < 4) {
                return null;
            }

            if (accountDAO.usernameExists(account.getUsername())) {
                return null;
            }

            return accountDAO.insertAccount(account);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account login(String username, String password) {
        try {
            return accountDAO.getAccountByCredentials(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean accountExists(int account_id) {
        try {
            return accountDAO.accountExists(account_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}