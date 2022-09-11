package test;

import biz.AuthenticationManager;
import biz.BankHistory;
import db.dao.DAO;
import model.Password;
import model.User;
import model.exceptions.UserUnnkownOrBadPasswordException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

import static biz.AuthenticationManager.hashPassword;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AuthenticationManagerTest {

    @Mock
    protected static BankHistory bankHistory;

    @Mock
    protected static DAO dao;

    protected AuthenticationManager authenticationManager;

    @BeforeAll
    static void prepareMock() {
        dao = Mockito.mock(DAO.class);
        bankHistory = Mockito.mock(BankHistory.class);
    }

    @Test
    void shouldThrownWhenLoginNotExist() throws SQLException, UserUnnkownOrBadPasswordException {
        authenticationManager = new AuthenticationManager(dao, bankHistory);
        Mockito.when(dao.findUserByName(anyString())).thenReturn(null);

        assertThrows(UserUnnkownOrBadPasswordException.class, () -> authenticationManager.logIn("notExistUserName", null));

    }

    @Test
    void shouldThrownWhenUserPasswordIsBad() throws SQLException, UserUnnkownOrBadPasswordException {
        authenticationManager = new AuthenticationManager(dao, bankHistory);
        Password password = new Password();
        password.setUserId(2);
        password.setPasswd(hashPassword("anyPassword".toCharArray()));


        Mockito.when(dao.findUserByName(any())).thenReturn(new User());
        Mockito.when(dao.findPasswordForUser(any())).thenReturn(password);

        assertThrows(UserUnnkownOrBadPasswordException.class, () -> authenticationManager.logIn("anyUserName", "badPassword".toCharArray()));

    }

}
