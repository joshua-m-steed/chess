package dataaccess;

import datamodel.RegistrationResult;
import datamodel.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {
    @Test
    void clear() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        assertNull(da.getUser(user.username()));
        da.createUser(user);
        assertNotNull(da.getUser(user.username()));
        da.clearUsers();
        da.clearGames();
        assertNull(da.getUser(user.username()));
    }

    @Test
    void createUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);
        assertEquals(user.username(), response.username());
        assertEquals(String.class, response.authToken().getClass());
        assertEquals(user.password(), da.getUser(user.username()).password());
        assertEquals(user.email(), da.getUser(user.username()).email());
    }

    @Test
    void getUser() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        da.clearUsers();
        da.clearGames();
        da.createUser(user);

        User foundUser = da.getUser(user.username());
        assertNotNull(foundUser);
        assertEquals(User.class, foundUser.getClass());
        assertEquals(user.username(), foundUser.username());
        assertEquals(user.password(), foundUser.password());
        assertEquals(user.email(), foundUser.email());
    }

    @Test
    void getAuth() {
        var user = new User("Frodo", "theOneRing", "frodo@baggins.com");
        DataAccess da = new MemoryDataAccess();
        da.clearUsers();
        da.clearGames();
        RegistrationResult response = da.createUser(user);
        String authToken = response.authToken();

        User authUser = da.getAuth(authToken);
        assertNotNull(authUser);
        assertEquals(User.class, authUser.getClass());
        assertEquals(user.username(), authUser.username());
        assertEquals(user.password(), authUser.password());
        assertEquals(user.email(), authUser.email());
    }
}