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
                    register - to create an account
                    login - to login and play
                    quit - to leave chess behind
                    help - to list possible commands
                    """;
        }
        return """
                he
                """;
    }
}
