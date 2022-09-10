package test;

import biz.AccountManager;
import biz.BankHistory;
import biz.InterestOperator;
import db.dao.DAO;
import model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static test.IntegrationTest.anyUser;

public class IntersetOperatorTes {

    @Mock
    protected static DAO dao;

    @Mock
    protected AccountManager accountManager;

    @Mock
    protected static BankHistory bankHistory;

    @BeforeAll
    static void prepareMock() {
        dao = Mockito.mock(DAO.class);
        bankHistory = Mockito.mock(BankHistory.class);
    }

    @Test
    void interest() throws SQLException {
        accountManager = Mockito.mock(AccountManager.class);
        ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
        InterestOperator interestOperator = new InterestOperator(dao, accountManager, bankHistory);
        Mockito.when(accountManager.paymentIn(any(), amountCaptor.capture(), any(), anyInt()))
                .thenReturn(true);
        Account account = anyAccountWithAmount(100.0);
        interestOperator.countInterestForAccount(account);
        Assertions.assertEquals(20, amountCaptor.getValue());

    }

    private static Account anyAccountWithAmount(double amount) {
        Account account = new Account();
        account.setAmmount(amount);
        account.setId(20);
        account.setOwner(anyUser());
        return account;
    }
}
