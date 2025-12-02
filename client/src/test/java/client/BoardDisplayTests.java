package client;

import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.BoardDisplay;

public class BoardDisplayTests {

    @Test
    public void run() {
        BoardDisplay paint = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.draw();

        BoardDisplay paint2 = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint2.draw();
    }
}
