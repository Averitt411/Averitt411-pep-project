package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerUser(Account account){
        return accountDAO.insertAccount(account);
    }

    public Account loginUser(Account account){
        return accountDAO.getAccount(account);
    }

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
}
