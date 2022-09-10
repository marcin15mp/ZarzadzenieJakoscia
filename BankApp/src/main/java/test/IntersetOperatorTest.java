package test;

import biz.AccountManager;
import biz.BankHistory;
import db.dao.DAO;
import db.dao.impl.SQLiteDB;
import model.Account;
import model.Role;
import model.User;
import model.operations.LogIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;



public class IntersetOperatorTest {


    @Mock
    protected AccountManager accountManager;

  //  protected DAO dao;

    @Test
    void loginSuccess() throws SQLException, ClassNotFoundException {

        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);
        User user = anyUser();

        //then
        Assertions.assertDoesNotThrow(() -> bh.logLoginSuccess(user));
    }

    @Test
    void loginFailure() throws SQLException, ClassNotFoundException {

        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);
        User user = anyUser();

        //then
        Assertions.assertDoesNotThrow(() -> bh.logLoginFailure(user, "Błąd logowania"));
    }

    @Test
    void assertNotImplementedWhenLogPaymentIn() throws SQLException, ClassNotFoundException {
        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);

        //then
        Assertions.assertThrows(NotImplementedException.class, () -> bh.logPaymentIn(null, 0, true) );
    }

    @Test
    void assertNotImplementedWhenLogPaymentOut() throws SQLException, ClassNotFoundException {
        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);

        //then
        Assertions.assertThrows(NotImplementedException.class, () -> bh.logPaymentOut(null, 0, true) );
    }

    @Test
    void logOperation() throws SQLException, ClassNotFoundException {

        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);
        User user = anyUser();

        //then
        Assertions.assertDoesNotThrow(() -> bh.logOperation(new LogIn(user,"Desc"), true));
    }

    @Test
    void loginOut() throws SQLException, ClassNotFoundException {

        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);
        User user = anyUser();

        //then
        Assertions.assertDoesNotThrow(() -> bh.logLogOut(user));
    }

    @Test
    void applyAmount() throws SQLException, ClassNotFoundException {
        //given:
        Account account = new Account();

        //when:
        account.setAmmount(50);

        //then:
        Assertions.assertEquals(account.getAmmount(), 50);
    }

    @Test
    void checkOutcomeWhenAmountIsHigh() throws SQLException, ClassNotFoundException {
        //given:
        Account account = anyAccountWithAmount(50);
        DAO dao = SQLiteDB.createDAO();

        //when:
        boolean outcome = account.outcome(20);
        double amountAfterTransaction = account.getAmmount();

        //then:
        Assertions.assertEquals(outcome, true);
        Assertions.assertEquals(amountAfterTransaction, 30);
    }

    @Test
    void checkOutcomeWhenAmountIsTooLow() throws SQLException, ClassNotFoundException {
        //given:
        Account account = anyAccountWithAmount(20);
        DAO dao = SQLiteDB.createDAO();

        //when:
        boolean outcome = account.outcome(50);
        double amountAfterTransaction = account.getAmmount();

        //then:
        Assertions.assertEquals(outcome, false);
        Assertions.assertEquals(amountAfterTransaction, 20);
    }

    @Test
    void checkOutcomeWhenAmountIsEqual() throws SQLException, ClassNotFoundException {
        //given:
        Account account = anyAccountWithAmount(20);
        DAO dao = SQLiteDB.createDAO();

        //when:
        boolean outcome = account.outcome(20);
        double amountAfterTransaction = account.getAmmount();

        //then:
        Assertions.assertEquals(outcome, true);
        Assertions.assertEquals(amountAfterTransaction, 0);
    }

    private static User anyUser() {
        Role role = new Role();

        User user = new User();
        user.setId(11);
        user.setName("ExampleUser");
        user.setRole(role);

        return user;
    }

    private static Account anyAccountWithAmount(double amount) {
        Account account = new Account();
        account.setAmmount(amount);
        account.setId(20);
        account.setOwner(anyUser());
        return account;
    }

}
