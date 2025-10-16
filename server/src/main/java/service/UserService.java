package service;

import dataaccess.DataAccess;
import datamodel.LoginResult;
import datamodel.LogoutResult;
import datamodel.RegistrationResult;
import datamodel.User;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegistrationResult register(User user) {
        return new RegistrationResult(user.username(), "theOneRing");
    }

    public LoginResult login(User user) {
        return new LoginResult(user.username(), "melonTheElvishWordForFriend");
    }

    public LogoutResult logout(User user) {
        return new LogoutResult();
    }
}
