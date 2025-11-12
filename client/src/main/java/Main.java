import chess.*;
import ui.ChessClient;

public class Main {
    public static void main(String[] args) {
        try {
            new ChessClient().run();
//            var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//            System.out.println("♕ 240 Chess Client: " + piece);
        } catch (Throwable ex) {
            System.out.print("Unable to start server");
        }

    }
}