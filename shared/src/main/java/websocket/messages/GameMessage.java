package websocket.messages;

public class GameMessage extends ServerMessage {

    private final String game;

    public GameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getGame() {
        return game;
    }
}
