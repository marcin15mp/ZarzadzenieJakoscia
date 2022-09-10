package test;

import gui.Application;
import gui.LoginForm;
import model.Role;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationTest {
    @Test
    void initializeAppWithoutError() {
        Assertions.assertDoesNotThrow( () -> new Application());
    }

    @Test
    void initializeAppMainWithoutError() {
        Assertions.assertDoesNotThrow( () -> Application.main(new String[]{""}));
    }


    @Test
    void initializeLoginFormWithoutError() {
        Application application = new Application();
        Assertions.assertDoesNotThrow( () -> new LoginForm(application));
    }

    @Test
    void userObjectCreatorTest() {
        //given
        Role role = new Role();
        String userName = "Olek";
        int userId = 5;

        //when
        User us = new User();
        us.setName(userName);
        us.setId(userId);
        us.setRole(role);

        //then
        Assertions.assertEquals(us.getName(), userName);
        Assertions.assertEquals(us.getId(), userId);
        Assertions.assertEquals(us.getRole(), role);
    }
}
