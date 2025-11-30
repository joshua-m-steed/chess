package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public enum Type {
        JOIN,
        MOVE,
        LEAVE,
        RESIGN,
        CHECK,
        CHECKMATE,
        CONNECT,
        DISCONNECT
    }

    private final Type type;
    private final String message;

    public NotificationMessage(Type type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
