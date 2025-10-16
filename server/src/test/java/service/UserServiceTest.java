package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.RegistrationResult;
import datamodel.User;
import org.junit.jupiter.api.Test;

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
}