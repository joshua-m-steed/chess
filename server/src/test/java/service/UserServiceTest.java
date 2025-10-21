package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.LoginResult;
import datamodel.RegistrationResult;
import datamodel.User;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Test
    void register() {
        var user = new User("James", "haha", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);
        RegistrationResult res = service.register(user);
        assertNotNull(res);
        assertEquals(res.username(), user.username());
        assertNotNull(res.authToken());
        assertEquals(String.class, res.authToken().getClass());
    }

    @Test
    void registerNameAlreadyExists() {
        var existingUser = new User("James", "haha", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult resOne = service.register(existingUser);
        assertNotNull(resOne);
        assertEquals(resOne.username(), existingUser.username());
        assertNotNull(resOne.authToken());

        assertThrows(ForbiddenResponse.class, () -> {
            service.register(existingUser);
        });
    }

    @Test
    void registerMissingUserInfo() {
        var missingUser = new User("", "AlphabetSoup", "My@email.com");
        var missingPass = new User("I have a name", "", "My@email.com");
        var missingMail = new User("I have a name", "AlphabetSoup", "");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingUser);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingPass);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingMail);
        });
    }

    @Test
    void login() {
        var regUser = new User("James", "haha", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUser);
        LoginResult res = service.login(regUser);

        assertNotNull(res);
        assertEquals(res.username(), regUser.username());
        assertNotNull(res.authToken());
        assertEquals(String.class, res.authToken().getClass());
    }

    @Test
    void loginMissingUserInfo() {
        var missingUser = new User("", "IS", "");
        var missingPass = new User("MyName", "", null);


        var regUser = new User("MyName", "IS", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUser);

        assertThrows(BadRequestResponse.class, () -> {
            service.login(missingUser);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.login(missingPass);
        });
    }

    @Test
    void loginUserNotFound() {
        var regUser = new User("James", "haha", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUser);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.login(new User("Michael", "uhoh", "Why@you"));
        });
    }
}