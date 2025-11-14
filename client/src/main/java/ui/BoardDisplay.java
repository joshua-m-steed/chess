package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.util.List;

public class BoardDisplay {

    private ChessGame game;
    List<String> letters = List.of("a", "b", "c", "d", "e", "f", "g", "h");


    public BoardDisplay(ChessGame game) {
        this.game = game;
    }

    public void update(ChessGame game) {
        this.game = game;
    }

    public void draw() {
        ChessBoard board = game.getBoard();
        ChessPiece[][] display = board.getBoard();
        StringBuilder result = new StringBuilder();

        // Print row letters
        result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.SET_TEXT_COLOR_GREEN);
        for(int i = 0; i < 8; i++) {
            result.append(" " + letters.get(i) + " ");
        }
        result.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");

//        result.append(s.repeat(10));

        for(int i = 0; i < 8; i++)
        {
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   ");
            // Pre
            for (int j = 0; j < 8; j++)
            {
                drawTile(i, j, result);
                result.append("   ");
                drawPiece(i, j, result, display);
            }
            //Post
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   ");
            result.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR)
                    .append("\n");
        }

        result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.SET_TEXT_COLOR_GREEN);
        for(int i = 0; i < 8; i++) {
            result.append(" " + letters.get(i) + " ");
        }
        result.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");

        System.out.print(result);
    }

    private void drawTile(int i, int j, StringBuilder result) {
        if((i + j) % 2 == 0) {
            result.append(EscapeSequences.SET_BG_COLOR_WHITE);
        } else {
            result.append(EscapeSequences.SET_BG_COLOR_BLACK);
        }
    }

    private void drawPiece(int i, int j, StringBuilder result, ChessPiece[][] display) {
        String piece;
        if (display[i][j] != null) {
            if (display[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                result.append(EscapeSequences.SET_TEXT_COLOR_RED);
            } else {
                result.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            }

            piece = switch(display[i][j].getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            piece = "   ";
        }

        result.append(piece);
    }
}
