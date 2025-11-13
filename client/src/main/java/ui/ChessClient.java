package ui;

import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private State state = State.LOGGED_OUT;
    private ServerFacade server;
    private String username = null;
    private String authToken = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public enum State {
        LOGGED_IN,
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
                case "create" -> create(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(String... params) throws Exception {
        if (state == State.LOGGED_IN) {
            throw new Exception("Logout before you register a new account!");
        }
        if (params.length >= 1) {
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
        throw new Exception("Not enough parameters were provided");
    }

    private String login(String... params) throws Exception {
        if (params.length >= 1) {
            username = params[0];
            String password = params[1];

            User user = new User(username, password, null);
            Auth authUser = server.login(user);
            if (authUser != null) {
                state = State.LOGGED_IN;
            } else {
                throw new Exception("User not found");
            }
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome!"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " You logged in as "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + username;
        }
        throw new Exception("Not enough parameters were provided");
    }

    private String logout() throws Exception {
        assertAuthorized();
        server.logout(authToken);

        state = State.LOGGED_OUT;
        String holdUser = username;
        username = null;
        return EscapeSequences.SET_TEXT_COLOR_YELLOW + holdUser
                + EscapeSequences.SET_TEXT_COLOR_BLUE + " has left the playing area";
    }

    private String create(String... params) throws Exception {
        // Check if AUTH
        if (params.length >= 1) {
            String gameName = params[0];
            String gameID = "0000"; // PLACEHOLDER
            return "The game " + EscapeSequences.SET_TEXT_COLOR_GREEN + gameName
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " has been created at ID "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + gameID;
        }
        throw new Exception("Not enough parameters were provided");
    }

    // Implement LIST Game AFTER Server HTTP endpoint is joined

    private void assertAuthorized() throws Exception {
        if (state == State.LOGGED_OUT) {
            throw new Exception("Please log in!");
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
        return EscapeSequences.SET_TEXT_COLOR_MAGENTA + """
                create <NAME>                           :♔:  to create a new game
                list                                    :♔:  to present all existing games
                join <ID> [ WHITE | BLACK ]             :♔:  to join an existing game
                observe <ID>                            :♔:  to observe an existing game
                logout                                  :♔:  to logout
                quit                                    :♔:  to leave chess behind
                help                                    :♔:  to llist possible commands
                """;
    }
}
