package service;

import dataaccess.DataAccess;
import datamodel.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

import java.util.Objects;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegistrationResult register(User user) throws BadRequestResponse, ForbiddenResponse {
        // Verify that passed in User does not have all needed variables
        if((user.username() == null || user.username().isBlank())
            || (user.password() == null || user.password().isBlank())
                || (user.email() == null || user.email().isBlank())) {
            throw new BadRequestResponse("Error: bad request");
        }

        // Verify that GetUser is either Object or result!
        if(this.dataAccess.getUser(user.username()) != null) {
            throw new ForbiddenResponse("Error: already taken");
        }

        return this.dataAccess.createUser(user);
    }

    public LoginResult login(User user) throws BadRequestResponse, UnauthorizedResponse {
        // Verifies that all needed information is present
        if((user.username() == null || user.username().isBlank() )
                || (user.password() == null || user.password().isBlank())) {
            throw new BadRequestResponse("Error: bad request");
        }

        // Fetches user from dataAccess by username
        User checkAuth = this.dataAccess.getUser(user.username());
        if(checkAuth == null) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }
        else if(!Objects.equals(checkAuth.password(), user.password())) {
            throw new UnauthorizedResponse("Error: unauthorized");
        }

        return this.dataAccess.authUser(user);
    }

    public LogoutResult logout(User user) {
        return new LogoutResult();
    }
}
