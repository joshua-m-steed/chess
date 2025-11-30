package websocket.messages;

public class GameMessage extends ServerMessage {

    private final String game;
//    private final String message;

    public GameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
//        this.message = message;
    }

    public String getGame() {
        return game;
    }

//    public String getMessage() {
//        return message;
//    }
}
