package ui;

public class ChessClient {
    private State state = State.LOGGEDOUT;

    public ChessClient() {}

    public enum State {
        LOGGEDIN,
        LOGGEDOUT
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.print(help());

    }

    private String help() {
        if(state == State.LOGGEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL>  :♔:  to create an account
                    login <USERNAME> <PASSWORD>             :♔:  to login and play
                    quit                                    :♔:  to leave chess behind
                    help                                    :♔:  to list possible commands
                    """;
        }
        return """
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
