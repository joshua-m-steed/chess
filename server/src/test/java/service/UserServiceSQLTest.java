package service;

import dataaccess.MemoryDataAccess;
import datamodel.LoginResult;
import datamodel.LogoutResult;
import datamodel.RegistrationResult;
import datamodel.User;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceSQLTest {
    @Test
    void register() {
        var userSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);
        RegistrationResult res = service.register(userSQL);
        assertNotNull(res);
        assertEquals(res.username(), userSQL.username());
        assertNotNull(res.authToken());
        assertEquals(String.class, res.authToken().getClass());
    }

    @Test
    void registerNameAlreadyExists() {
        var existingUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult resOne = service.register(existingUserSQL);
        assertNotNull(resOne);
        assertEquals(resOne.username(), existingUserSQL.username());
        assertNotNull(resOne.authToken());

        assertThrows(ForbiddenResponse.class, () -> {
            service.register(existingUserSQL);
        });
    }

    @Test
    void registerMissingUserInfo() {
        var missingUserSQL = new User("", "AlphabetSoup", "My@email.com");
        var missingPassSQL = new User("I have a name", "", "My@email.com");
        var missingMailSQL = new User("I have a name", "AlphabetSoup", "");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingUserSQL);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingPassSQL);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.register(missingMailSQL);
        });
    }

    @Test
    void login() {
        var regUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUserSQL);
        LoginResult res = service.login(regUserSQL);

        assertNotNull(res);
        assertEquals(res.username(), regUserSQL.username());
        assertNotNull(res.authToken());
        assertEquals(String.class, res.authToken().getClass());
    }

    @Test
    void loginMissingUserInfo() {
        var missingUserSQL = new User("", "IS", "");
        var missingPassSQL = new User("MyName", "", null);


        var regUserSQL = new User("MyName", "IS", "@me.bro");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUserSQL);

        assertThrows(BadRequestResponse.class, () -> {
            service.login(missingUserSQL);
        });

        assertThrows(BadRequestResponse.class, () -> {
            service.login(missingPassSQL);
        });
    }

    @Test
    void loginUserNotFound() {
        var regUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        service.register(regUserSQL);

        assertThrows(UnauthorizedResponse.class, () -> {
            service.login(new User("Samwise", "ImGoingWithYou", "allforfrodo@baggins.com"));
        });
    }

    @Test
    void logout() {
        var regUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult regResponse = service.register(regUserSQL);

        LogoutResult outResponse = service.logout(regResponse.authToken());

        assertNotNull(outResponse);
        assertEquals(new LogoutResult(), outResponse);
    }

    @Test
    void logoutMissionAuthInfo() {
        var regUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult auth = service.register(regUserSQL);
        String emptyAuth = null;

        assertEquals(String.class, auth.authToken().getClass());
        assertNotEquals(emptyAuth, auth.authToken());
        assertThrows(UnauthorizedResponse.class, () -> {
            service.logout(emptyAuth);
        });
    }

    @Test
    void logoutInvalidAuthToken() {
        var regUserSQL = new User("Frodo", "theOneRing", "frodo@baggins.com");

        var da = new MemoryDataAccess();
        var service = new UserService(da);

        RegistrationResult auth = service.register(regUserSQL);
        String invalidAuth = "WhereDidFrodoGo";

        assertEquals(String.class, auth.authToken().getClass());
        assertNotEquals(invalidAuth, auth.authToken());
        assertThrows(UnauthorizedResponse.class, () -> {
            service.logout(invalidAuth);
        });
    }
}