package service;

import dataaccess.DataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

import java.util.ArrayList;
import java.util.Objects;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegistrationResult register(User user) throws BadRequestResponse, ForbiddenResponse {
        if((user.username() == null || user.username().isBlank())
            || (user.password() == null || user.password().isBlank())
                || (user.email() == null || user.email().isBlank())) {
            throw new BadRequestResponse("Error: bad request");
        }

        this.dataAccess.createUser(user);
        // Verify that GetUser is either Object or result!
//        if(this.dataAccess.getUser(user.username()) != null) {
//            throw new ForbiddenResponse("Error: already taken");
//        }

        return new RegistrationResult(user.username(), "theOneRing");
    }

    public LoginResult login(User user) throws BadRequestResponse, UnauthorizedResponse {
        if((user.username() == null || user.username().isBlank() )
                || (user.password() == null || user.password().isBlank())) {
            throw new BadRequestResponse("Error: bad request");
        }

        User checkAuth = this.dataAccess.getUser(user.username());
        if(checkAuth == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        else if(!Objects.equals(checkAuth.password(), user.password())) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }

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
