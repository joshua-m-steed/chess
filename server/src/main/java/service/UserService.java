package service;

import dataaccess.DataAccess;
import datamodel.*;

import java.util.ArrayList;

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

    public GameListResult gameList() {
        ArrayList<Game> games = new ArrayList<>();
//        games.add(new Game(5555, "MiniJosh", "Frodo", "Uhhh", null));
//        games.add(new Game(2, "Me", "Myself", "AndI", null));
        return new GameListResult(games);
    }
}
