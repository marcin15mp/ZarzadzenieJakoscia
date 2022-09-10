package biz;

import db.dao.DAO;
import model.Account;
import model.Operation;
import model.User;
import model.operations.Interest;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;

/**
 * Created by Krzysztof Podlaski on 07.03.2018.
 */
public class InterestOperator {
    protected DAO dao;
    protected AccountManager accountManager;
    protected BankHistory bankHistory;
    private double interestFactor =.2;

    public InterestOperator (DAO dao, AccountManager am, BankHistory bankHistory){
        this.dao=dao;
        this.accountManager = am; // brak this
        this.bankHistory = bankHistory; // brak historii banku
    }

    public void countInterestForAccount(Account account) throws SQLException {
        double ammount = account.getAmmount();
        double interest = ammount*interestFactor;
        User user = dao.findUserByName("InterestOperator");
        String desc = "Interest ...";
        boolean success = accountManager.paymentIn(user,interest,desc,account.getId());
        Operation operation=new Interest(user,interest,desc,account);
        bankHistory.logOperation(operation,success);
    }


}
