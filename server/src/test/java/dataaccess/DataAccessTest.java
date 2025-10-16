package dataaccess;

import datamodel.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {
    @Test
    void clear() {
        var user = new User("James", "haha", "@me.bro");
        DataAccess da = new MemoryDataAccess();
        assertNull(da.getUser(user.username()));
        da.saveUser(user);
        assertNotNull(da.getUser(user.username()));
        da.clear();
        assertNull(da.getUser(user.username()));
    }
}