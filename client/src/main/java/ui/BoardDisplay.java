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

//    public void update(ChessGame game) For future use

    public void draw(ChessGame.TeamColor color) {
        ChessBoard board = game.getBoard();
        ChessPiece[][] display = board.getBoard();
        StringBuilder result = new StringBuilder();

        drawBorder(result, color);

        if (color == ChessGame.TeamColor.WHITE) {
            drawWhitePov(result, display);
        } else {
            drawBlackPov(result, display);
        }

        drawBorder(result, color);

        System.out.print(result);
    }

    private void drawBorder(StringBuilder result, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            // Print row letters
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.SET_TEXT_COLOR_GREEN);
            for(int i = 0; i < 8; i++) {
                result.append(" " + letters.get(i) + " ");
            }
            result.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        } else {
            // Print row letters
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   " + EscapeSequences.SET_TEXT_COLOR_GREEN);
            for(int i = 7; i >= 0; i--) {
                result.append(" " + letters.get(i) + " ");
            }
            result.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        }
    }

    private void drawWhitePov(StringBuilder result, ChessPiece[][] display) {
        for(int i = 7; i >= 0; i--)
        {
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ");
            // Pre
            for (int j = 7; j >= 0; j--)
            {
                drawTile(i, j, result);
                checkPieces(i, j, result, display);
            }
            //Post
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ")
                    .append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR)
                    .append("\n");
        }
    }

    private void drawBlackPov(StringBuilder result, ChessPiece[][] display) {
        for(int i = 0; i < 8; i++)
        {
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ");
            // Pre
            for (int j = 0; j < 8; j++)
            {
                drawTile(i, j, result);
                checkPieces(i, j, result, display);
            }
            //Post
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ")
                    .append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR)
                    .append("\n");
        }
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
        if (display[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
            result.append(EscapeSequences.SET_TEXT_COLOR_RED);

            piece = switch(display[i][j].getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            result.append(EscapeSequences.SET_TEXT_COLOR_BLUE);

            piece = switch(display[i][j].getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }

        result.append(piece);
    }

    private void checkPieces(int i, int j, StringBuilder result, ChessPiece[][] display) {
        if(display[i][j] != null) {
            drawPiece(i, j, result, display);
        } else {
            result.append("   ");
        }
    }
}
