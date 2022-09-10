package test;

import biz.AccountManager;
import biz.BankHistory;
import db.dao.DAO;
import db.dao.impl.SQLiteDB;
import model.Account;
import model.Password;
import model.Role;
import model.User;
import model.exceptions.OperationIsNotAllowedException;
import model.exceptions.UserUnnkownOrBadPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

/**
 * Created by Krzysztof Podlaski on 04.03.2018.
 */
public class IntegrationTest {

    @Test
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DAO dao = SQLiteDB.createDAO();
        User user = dao.findUserByName("Adam");
        System.out.println(user.getId()+ " "+user.getName()+ " "+user.getRole().getName());
        Account acc = dao.findAccountById(2);
        user = acc.getOwner();
        System.out.println(user.getId()+ " "+user.getName()+ " "+user.getRole().getName());
        System.out.println(acc.getAmmount());
        acc.setAmmount(11433.85);
        dao.updateAccountState(acc);

        Account accountById = dao.findAccountById(2);
        Assertions.assertEquals(accountById.getAmmount(),11433.85);


        /*String s = AuthenticationManager.hashPassword("Adam");
        System.out.println(s);
        System.out.println(s.equals(AuthenticationManager.hashPassword("Adam")));
        user = dao.findUserByName("Adam");
        dao.setUserPassword(user,s,"Adam");
        user = dao.findUserByName("Ewa");
        s = AuthenticationManager.hashPassword("Ewa");
        dao.setUserPassword(user,s,"Ewa");
        user = dao.findUserByName("Admin");
        s = AuthenticationManager.hashPassword("Admin");
        dao.setUserPassword(user,s,"Admin");
        */
    }

    @Test
    void paymentInTest() throws SQLException, ClassNotFoundException {
        DAO dao = SQLiteDB.createDAO();

        AccountManager accountManager = AccountManager.buildBank();

        Account accountById = dao.findAccountById(1);
        User owner = accountById.getOwner();
        double amountBeforePayment = accountById.getAmmount();
        accountManager.paymentIn(owner, 100, "any title", 1); //brak odwołania do this. w metodzie ksiegujacej
        Account accountById1 = dao.findAccountById(1);
        double ammountAfterPaymentIn = accountById1.getAmmount();

        Assertions.assertEquals(amountBeforePayment + 100, ammountAfterPaymentIn);

    }

    @Test
    void internalPayments() throws SQLException, ClassNotFoundException, OperationIsNotAllowedException {
        DAO dao = SQLiteDB.createDAO();

        AccountManager accountManager = AccountManager.buildBank();

        Account sourceAccount = dao.findAccountById(1);
        Account destinationAccount = dao.findAccountById(2);
        User owner = sourceAccount.getOwner();
        double sourceAccountStateBefore = sourceAccount.getAmmount();
        double destinationAccountStateBefore = destinationAccount.getAmmount();
        accountManager.internalPayment(owner, 100, "any title", 1, 2); //brak odwołania do this. w metodzie ksiegujacej

        Double sourceAccountStateAfter = dao.findAccountById(1).getAmmount();
        Double destinationAccountStateAfter = dao.findAccountById(2).getAmmount();


        Assertions.assertEquals(sourceAccountStateBefore - 100, sourceAccountStateAfter);
        Assertions.assertEquals(destinationAccountStateBefore + 100, destinationAccountStateAfter);

    }

    @Test
    void logInTestWhenBadPassword() {
        AccountManager accountManager = AccountManager.buildBank();

        Assertions.assertThrows(UserUnnkownOrBadPasswordException.class, () -> accountManager.logIn("Admin","badPassword".toCharArray()));
    }

    @Test
    void logOutTestWhenUserNotLoggedAndNotExist() {
        AccountManager accountManager = AccountManager.buildBank();

        //aplikacja potrafi wylogować niezalogowanego użytkownika
        Assertions.assertTrue( () -> {
            try {
                return accountManager.logOut(anyUser());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void getLoggedUserWhenUserNotLogged() {
        AccountManager accountManager = AccountManager.buildBank();

        User loggedUser = accountManager.getLoggedUser();
        Assertions.assertEquals(loggedUser, null);
    }

    @Test
    void paymentOutTest() throws SQLException, ClassNotFoundException, OperationIsNotAllowedException {
        DAO dao = SQLiteDB.createDAO();

        AccountManager accountManager = AccountManager.buildBank();

        Account accountById = dao.findAccountById(1);
        User owner = accountById.getOwner();
        double amountBeforePayment = accountById.getAmmount();
        accountManager.paymentOut(owner, 100, "any title", 1); //brak odwołania do this. w metodzie ksiegujacej
        Account accountById1 = dao.findAccountById(1);
        double ammountAfterPaymentIn = accountById1.getAmmount();

        Assertions.assertEquals(amountBeforePayment - 100, ammountAfterPaymentIn);

    }

    @org.junit.jupiter.api.Test
    void cc() throws SQLException, ClassNotFoundException {

        //given
        DAO dao = SQLiteDB.createDAO();
        BankHistory bh = new BankHistory(dao);
        User user = anyUser();

        //when
        bh.logLoginSuccess(user);

        //then
       Assertions.assertDoesNotThrow(() -> bh.logLoginSuccess(user));
    }

    static User anyUser() {
        DAO dao = mock(DAO.class);
        BankHistory bh = new BankHistory(dao);
        Role role = new Role();

        User user = new User();
        user.setId(11);
        user.setName("ExampleUser");
        user.setRole(role);

        return user;
    }
}
