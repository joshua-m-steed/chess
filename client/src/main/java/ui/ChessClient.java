package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ChessClient {
    private State state = State.LOGGED_OUT;
    private ServerFacade server;
    private String username = null;
    private String authToken = null;
    private GameList recentList = null;
    private BoardDisplay display = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public enum State {
        LOGGED_IN,
        IN_GAME,
        LOGGED_OUT
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome to Chess!");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = evaluate(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();

    }

    private void printPrompt() {
        System.out.print("\n"+ EscapeSequences.SET_TEXT_COLOR_BLUE + "[" + this.state + "] >>> ");
    }

    private String evaluate(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            cmd = cmd.toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "list" -> listGames();
                case "create" -> create(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> quit();
                case "move" -> move(params);
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                case "leave" -> leave();
                case "resign" -> resign();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String quit() throws Exception {
        if (state == State.IN_GAME) {
            resign();
        }

        if (state == State.LOGGED_IN) {
            logout();
        }

        return "quit";
    }

    private String register(String... params) throws Exception {
        if (state == State.LOGGED_IN) {
            throw new Exception("Logout before you register a new account!");
        }
        if (params.length >= 3) {
            username = params[0];
            String password = params[1];
            String email = params[2];

            User user = new User(username, password, email);
            Auth authUser = server.register(user);
            if (authUser != null) {
                state = State.LOGGED_IN;
                authToken = authUser.authToken();
            } else {
                throw new Exception("Username already taken");
            }
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome!"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " You signed in as "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + username;
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String login(String... params) throws Exception {
        if (params.length >= 2) {
            username = params[0];
            String password = params[1];

            User user = new User(username, password, null);
            Auth authUser = server.login(user);
            if (authUser != null) {
                state = State.LOGGED_IN;
                authToken = authUser.authToken();
            } else {
                throw new Exception("User not found");
            }
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome!"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " You logged in as "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + username;
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String logout() throws Exception {
        assertAuthorized();
        server.logout(authToken);

        state = State.LOGGED_OUT;
        String holdUser = username;
        username = null;
        authToken = null;
        recentList = null;
        return EscapeSequences.SET_TEXT_COLOR_YELLOW + holdUser
                + EscapeSequences.SET_TEXT_COLOR_BLUE + " has left the playing area";
    }

    private String listGames() throws Exception {
        assertAuthorized();
        GameList list = server.list(authToken);
        recentList = list;
        StringBuilder result = new StringBuilder();
        var gson = new Gson();
        if (list.games().isEmpty()) {
            return "There are no games at the moment!";
        } else {
            int listIter = 0;
            for (Game game : list.games()) {
                result.append(EscapeSequences.SET_TEXT_COLOR_YELLOW)
                        .append(" | " + ++listIter + " | ")
                        .append(EscapeSequences.SET_TEXT_COLOR_BLUE)
                        .append(" Name: ")
                        .append(gson.toJson(game.gameName()))
                        .append(" White: ")
                        .append(gson.toJson(game.whiteUsername()))
                        .append(" Black: ")
                        .append(gson.toJson(game.blackUsername()))
                        .append('\n');
            }
        }
        return result.toString();
    }

    private String create(String... params) throws Exception {
        assertAuthorized();
        if (params.length >= 1) {
            String gameName = params[0];

            Game game = new Game(null, null, null, gameName, new ChessGame());
            game = server.create(game, authToken);
            GameList list = server.list(authToken);
            recentList = list;
            return "The game " + EscapeSequences.SET_TEXT_COLOR_GREEN + gameName
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " has been created";
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String join(String... params) throws Exception {
        assertAuthorized();
        if (params.length >= 2) {
            if( recentList == null) {
                throw new Exception("Please refer to 'list' before joining a game");
            }
            Game foundGame = null;
            int listId = Integer.parseInt(params[0]);
            String color = params[1].toLowerCase();

            if (recentList.games() == null) {
                throw new Exception("Unable to find any games. Go make one!");

            } else if (recentList.games().size() < (listId - 1)) {
                throw new Exception("Invalid game choice. Please refer to 'list'");
            } else {
                foundGame = recentList.games().get(listId - 1);
            }

            if (!color.equals("white") && !color.equals("black")) {
                throw new Exception("Chose a valid team color");
            }


            int gameID = foundGame.gameID();
            ChessGame.TeamColor teamColor = Objects.equals(color, "white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            GameJoin gameRequest = new GameJoin(teamColor, gameID);
            server.join(gameRequest, authToken);
            state = State.IN_GAME;

            display = new BoardDisplay(foundGame.game(), gameRequest.playerColor());
            display.draw();
            // Potentially verify their input with the list?
//            server.list(authToken);
//            System.out.println("");
            return EscapeSequences.SET_TEXT_COLOR_GREEN + username +
                    EscapeSequences.SET_TEXT_COLOR_BLUE + " has joined the game, " +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW + foundGame.gameName() +
                    EscapeSequences.SET_TEXT_COLOR_BLUE + ", as " + teamColor;
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String observe(String... params) throws Exception {
        assertAuthorized();
        if (params.length >= 1) {
            if( recentList == null) {
                throw new Exception("Please refer to 'list' before joining a game");
            }
            Game foundGame = null;
            int listId = Integer.parseInt(params[0]);
            if (recentList.games() == null) {
                throw new Exception("Unable to find any games. Go make one!");
            } else {
                foundGame = recentList.games().get(listId - 1);
            }

            int gameID = foundGame.gameID();

            GameJoin gameRequest = new GameJoin(null, gameID);
//            server.observe(gameRequest, authToken);
            state = State.IN_GAME;

            display = new BoardDisplay(foundGame.game(), ChessGame.TeamColor.WHITE);
            display.draw();

            return EscapeSequences.SET_TEXT_COLOR_GREEN + username +
                    EscapeSequences.SET_TEXT_COLOR_BLUE + " is watching the game, " +
                    EscapeSequences.SET_TEXT_COLOR_YELLOW + foundGame.gameName();
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String move(String... params) throws Exception {
        assertInGame();

        return EscapeSequences.SET_TEXT_COLOR_RED + "I am a placeholder";
    }

    private String redraw() throws Exception {
        assertInGame();
        display.draw();

        return "Board redrawn!";
    }

    private String highlight(String... params) throws Exception {
        assertInGame();
        if (params.length >= 1) {
            String tileID = params[0];
            display.highlight(tileID);

            return EscapeSequences.SET_TEXT_COLOR_RED + "I am a placeholder";
        }
        throw new Exception("Not enough parameters were given.");
    }

    private String leave() throws Exception {
        assertInGame();

        state = State.LOGGED_IN;
        return "Leaving the game";
    }

    private String resign(String... params) throws Exception {
        assertInGame();

        return EscapeSequences.SET_TEXT_COLOR_RED + "I am a placeholder";
    }

    private void assertAuthorized() throws Exception {
        if (state == State.LOGGED_OUT) {
            throw new Exception("Please log in first!");
        }
    }

    private void assertInGame() throws Exception {
        if (state != State.IN_GAME) {
            throw new Exception("Please join a game!");
        }
    }

    private String help() {
        if(state == State.LOGGED_OUT) {
            return EscapeSequences.SET_TEXT_COLOR_MAGENTA + """
                    register <USERNAME> <PASSWORD> <EMAIL>  :♔:  to create an account
                    login <USERNAME> <PASSWORD>             :♔:  to login and play
                    quit                                    :♔:  to leave chess behind
                    help                                    :♔:  to list possible commands
                    """;
        }
        else if(state == State.LOGGED_IN) {
            return EscapeSequences.SET_TEXT_COLOR_MAGENTA + """
                create <NAME>                           :♔:  to create a new game
                list                                    :♔:  to present all existing games
                join <ID> [ WHITE | BLACK ]             :♔:  to join an existing game
                observe <ID>                            :♔:  to observe an existing game
                logout                                  :♔:  to logout
                quit                                    :♔:  to leave chess behind
                help                                    :♔:  to list possible commands
                """;
        }
        else if(state == State.IN_GAME) {
            return EscapeSequences.SET_TEXT_COLOR_MAGENTA + """
                move <TILE>                             :♔:  to move your chosen piece
                redraw                                  :♔:  to redraw the board
                highlight <TILE>                        :♔:  to list possible moves of a piece
                leave                                   :♔:  to leave your game
                resign                                  :♔:  to forfeit and set away from the game
                help                                    :♔:  to list possible commands
                """;
        }

        return EscapeSequences.SET_TEXT_COLOR_RED + "Could not find proper game state. Please reload!";
    }
}
