package test;

import db.dao.DAO;
import db.dao.impl.SQLiteDB;
import model.Password;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.sqlite.SQLiteException;

import java.sql.SQLException;

public class DaoTest {

    static DAO dao;

    @BeforeAll
    static void initializeDao() throws SQLException, ClassNotFoundException {
        dao = SQLiteDB.createDAO();

    }

    @Test
    void findUserByName() throws SQLException {
        User ewa = dao.findUserByName("Ewa");

        Assertions.assertEquals(ewa.getName(), "Ewa");
    }

    @Test
    void findPasswordForUser() throws SQLException {

        User ewa = dao.findUserByName("Ewa");

        Password passwordForUser = dao.findPasswordForUser(ewa);

        Assertions.assertTrue(StringUtils.isNotBlank(passwordForUser.getPasswd()));
    }

    @Test
    void createDaoWithoutErrors() {
        Assertions.assertDoesNotThrow(() -> SQLiteDB.createDAO());
    }

    @Test
    void initializeDaoWithErrorsWhenDbExist() {
        Assertions.assertThrows(SQLiteException.class,() -> SQLiteDB.initializeDB());
    }

    @Test
    void setPasswordForUser() throws SQLException {

        User ewa = dao.findUserByName("Ewa");
        Password passwordForUser = dao.findPasswordForUser(ewa);

        boolean isSuccess = dao.setUserPassword(ewa, "noweHaslo", passwordForUser.getPasswd());

        Assertions.assertEquals(isSuccess, true);
    }

    @Test
    void setPasswordForUserWhenOldIsBas() throws SQLException {

        User ewa = dao.findUserByName("Ewa");

        boolean isSuccess = dao.setUserPassword(ewa, "noweHaslo", "badOldPassword");

        Assertions.assertEquals(isSuccess, false);
    }
}
