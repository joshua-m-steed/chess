package dataaccess;

import chess.ChessGame;
import datamodel.*;

import java.util.ArrayList;

public interface DataAccess {
        void clearUsers();
        void clearGames();
        RegistrationResult createUser(User user);
        User getUser(String username);
        User getAuth(String authToken);
        LoginResult authUser(User user);
        boolean deleteUser(String authToken);
        ArrayList<Game> listGame(String authToken);
        Game createGame(String gameName);
        void joinGame(User authUser, Game targetGame, String s);
        void updateGame(Integer gameID, ChessGame chessGame);
}
