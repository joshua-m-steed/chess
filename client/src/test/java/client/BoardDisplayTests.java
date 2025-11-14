package client;

import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.BoardDisplay;

public class BoardDisplayTests {

    @Test
    public void run() {
        BoardDisplay paint = new BoardDisplay(new ChessGame());
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint.draw(ChessGame.TeamColor.BLACK);

        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.draw(ChessGame.TeamColor.WHITE);
    }
}
