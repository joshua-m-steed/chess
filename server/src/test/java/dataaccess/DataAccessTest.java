package dataaccess;

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
        da.createUser(user);
        assertEquals(user, da.getUser(user.username()));
        assertEquals(user.username(), da.getUser(user.username()).username());
        assertEquals(user.password(), da.getUser(user.username()).password());
        assertEquals(user.email(), da.getUser(user.username()).email());
    }
}