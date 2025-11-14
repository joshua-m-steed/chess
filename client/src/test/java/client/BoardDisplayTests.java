package client;

import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.BoardDisplay;

public class BoardDisplayTests {

    @Test
    public void run() {
        BoardDisplay paint = new BoardDisplay(new ChessGame());
        paint.draw();
    }

}
