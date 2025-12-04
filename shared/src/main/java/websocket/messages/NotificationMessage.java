package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public enum NotificationType {
        JOIN,
        MOVE,
        LEAVE,
        RESIGN,
        CHECK,
        CHECKMATE,
        DISCONNECT
    }

    private final NotificationType type;
    private final String message;

    public NotificationMessage(NotificationType type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
