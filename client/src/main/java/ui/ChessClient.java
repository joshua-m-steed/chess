package ui;

public class ChessClient {
    private State state = State.LOGGEDOUT;

    public ChessClient() {}

    public enum State {
        LOGGEDIN,
        LOGGEDOUT
    }

    public void run() {
        System.out.println("Welcome to Chess! Please Sign in");
    }
}
