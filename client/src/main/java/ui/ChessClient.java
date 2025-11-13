package ui;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private State state = State.LOGGED_OUT;
    private String username = null;

    public ChessClient() {}

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
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.LOGGED_IN;
            username = params[0];
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome!"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " You signed in as "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + username;
        }
        throw new Exception("Not enough parameters were provided");
    }

    public String login(String... params) throws Exception {
//      // Check if Registered
        if (params.length >= 1) {
            state = State.LOGGED_IN;
            username = params[0];
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "Welcome!"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + " You logged in as "
                    + EscapeSequences.SET_TEXT_COLOR_YELLOW + username;
        }
        throw new Exception("Not enough parameters were provided");
    }

    public String logout() {
//      // Check if Signed in
        state = State.LOGGED_OUT;
        String holdUser = username;
        username = null;
        return EscapeSequences.SET_TEXT_COLOR_YELLOW + holdUser
                + EscapeSequences.SET_TEXT_COLOR_BLUE + " has left the playing area";
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
