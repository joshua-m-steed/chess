package ui;

import java.util.Scanner;

public class ChessClient {
    private State state = State.LOGGED_OUT;

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
                System.out.print(EscapeSequences.SET_TEXT_ITALIC + EscapeSequences.SET_TEXT_ITALIC + ": " + result + " :");
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
        return input;
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
                watch <ID>                              :♔:  to watch an existing game
                logout                                  :♔:  to logout
                quit                                    :♔:  to leave chess behind
                help                                    :♔:  to llist possible commands
                """;
    }
}
