package client;

import chess.ChessGame;
import org.junit.jupiter.api.Test;
//import passoff.chess.TestUtilities;
import ui.BoardDisplay;

public class BoardDisplayTests {

    @Test
    public void run() {
        BoardDisplay paint1 = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint1.draw();

        BoardDisplay paint2 = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint2.draw();
    }

    @Test
    public void testWhite() {
        BoardDisplay paint = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.draw();
    }

    @Test
    public void testBlack() {
        BoardDisplay paint = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint.draw();
    }

    @Test
    public void testWhiteMovedPieces() {
        var game = new ChessGame();
//        game.setBoard(TestUtilities.loadBoard("""
//                | | | | | | | | |
//                | | | | | | | | |
//                | |B| | | | | | |
//                | | | | | |K| | |
//                | | |n| | | | | |
//                | | | | | | | | |
//                | | | |q| |k| | |
//                | | | | | | | | |
//                """));

        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.draw();
    }

    @Test
    public void testBlackMovedPieces() {
        var game = new ChessGame();
//        game.setBoard(TestUtilities.loadBoard("""
//                | | | | | | | | |
//                | | | | | | | | |
//                | |B| | | | | | |
//                | | | | | |K| | |
//                | | |n| | | | | |
//                | | | | | | | | |
//                | | | |q| |k| | |
//                | | | | | | | | |
//                """));

        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint.draw();
    }

    @Test
    public void testWhiteOpeningMoves() {
        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
//        game.setBoard(TestUtilities.loadBoard("""
//                    |r|n|b|q|k|b| |r|
//                    |p|p|p|p|p|p|p|p|
//                    | | | | | |n| | |
//                    | | | | | | | | |
//                    | | | | |P| | | |
//                    | | | | | | | | |
//                    |P|P|P|P| |P|P|P|
//                    |R|N|B|Q|K|B|N|R|
//                    """));


        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.draw();
    }

    @Test
    public void testBlackOpeningMoves() {
        var game = new ChessGame();
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
//        game.setBoard(TestUtilities.loadBoard("""
//                    |r|n|b|q|k|b| |r|
//                    |p|p|p|p|p|p|p|p|
//                    | | | | | |n| | |
//                    | | | | | | | | |
//                    | | | | |P| | | |
//                    | | | | | | | | |
//                    |P|P|P|P| |P|P|P|
//                    |R|N|B|Q|K|B|N|R|
//                    """));


        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint.draw();
    }

    @Test
    public void testHighlight() {
        BoardDisplay paint = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.WHITE);
        paint.highlight("d4");
    }

    @Test
    public void highlightKnight() {
        BoardDisplay paint = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.WHITE);
        System.out.print("THIS IS SUPPOSED TO BE FROM WHITE VIEW\n");
        paint.highlight("b1");

        BoardDisplay paint2 = new BoardDisplay(new ChessGame(), ChessGame.TeamColor.BLACK);
        System.out.print("THIS IS SUPPOSED TO BE FROM BLACK VIEW\n");
        paint2.highlight("b1");
    }

    @Test
    public void testMovedPiecesHighlightQueenWhite() {
        var game = new ChessGame();
//        game.setBoard(TestUtilities.loadBoard("""
//                | | | | | | | | |
//                | | | | | | | | |
//                | |B|B| | | | | |
//                | | | | | |K| | |
//                | |n|n| | | | | |
//                | | | | | | | | |
//                | | | |q| |k| | |
//                | | | | | | | | |
//                """));

        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.WHITE);
        paint.highlight("d2");
    }

    @Test
    public void testMovedPiecesHighlightQueenBlack() {
        var game = new ChessGame();
//        game.setBoard(TestUtilities.loadBoard("""
//                | | | | | | | | |
//                | | | | | | | | |
//                | |B|B| | | | | |
//                | | | | | |K| | |
//                | |n|n| | | | | |
//                | | | | | | | | |
//                | | | |q| |k| | |
//                | | | | | | | | |
//                """));

        BoardDisplay paint = new BoardDisplay(game, ChessGame.TeamColor.BLACK);
        paint.highlight("d2");
    }
}
