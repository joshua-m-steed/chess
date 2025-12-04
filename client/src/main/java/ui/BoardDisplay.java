package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BoardDisplay {

    private ChessGame game;
    private ChessGame.TeamColor team;
    List<String> letters = List.of("a", "b", "c", "d", "e", "f", "g", "h");


    public BoardDisplay(ChessGame game, ChessGame.TeamColor team) {
        this.game = game;
        this.team = team;
    }

    public void update(ChessGame game) {
        this.game = game;
    }

    public void draw() {
        System.out.println();
        ChessBoard board = game.getBoard();
        ChessPiece[][] display = board.getBoard();
        StringBuilder result = new StringBuilder();

        drawBorder(result, team);

        if (team == ChessGame.TeamColor.WHITE) {
            drawWhitePov(result, display);
        } else {
            drawBlackPov(result, display);
        }

        drawBorder(result, team);

        System.out.print(result);
    }

    public void highlight(String tileID) {
        ChessBoard board = game.getBoard();
        ChessPiece[][] display = board.getBoard();
        StringBuilder result = new StringBuilder();
        ChessPosition targetPos = parseTileID(tileID);

        ChessPiece targetPiece = board.getPiece(targetPos);

        if (targetPiece == null) {
            draw();
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "'" + tileID + "'" + " is unoccupied!" + EscapeSequences.RESET_TEXT_COLOR);
            return;
        }

        Collection<ChessMove> targetMoves = targetPiece.pieceMoves(board, targetPos);
        List<ChessPosition> endPositions = new ArrayList<>();

        for (ChessMove move : targetMoves) {
            endPositions.add(move.getEndPosition());
        }

        drawBorder(result, team);

        if (team == ChessGame.TeamColor.WHITE) {
            drawHighlightWhitePov(result, display, endPositions, targetPos);
        } else {
            drawHighlightBlackPov(result, display, endPositions, targetPos);
        }

        drawBorder(result, team);

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
            for (int j = 0; j < 8; j++)
            {
                drawTile(i, j, result);
                checkPieces(i, j, result, display, Boolean.FALSE, Boolean.FALSE);
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
            for (int j = 7; j >= 0; j--)
            {
                drawTile(i, 8 - j, result);
                checkPieces(i, j, result, display, Boolean.FALSE, Boolean.FALSE);
            }
            //Post
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ")
                    .append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR)
                    .append("\n");
        }
    }

    private void drawHighlightWhitePov(StringBuilder result, ChessPiece[][] display, List<ChessPosition> endPositions, ChessPosition targetPos) {
        for(int i = 7; i >= 0; i--)
        {
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ");
            // Pre
            for (int j = 0; j < 8; j++) {
                ChessPosition scan = new ChessPosition(i + 1, j + 1);
                if (endPositions.contains(scan)) {
                    drawHighlightTile(i, j, result);
                    checkPieces(i, j, result, display, Boolean.TRUE, Boolean.FALSE);
                } else if (scan.equals(targetPos)) {
                    result.append(EscapeSequences.SET_BG_COLOR_MAGENTA);
                    checkPieces(i, j, result, display, Boolean.FALSE, Boolean.TRUE);
                } else {
                    drawTile(i, j, result);
                    checkPieces(i, j, result, display, Boolean.FALSE, Boolean.FALSE);
                }

            }
            //Post
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ")
                    .append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR)
                    .append("\n");
        }
    }

    private void drawHighlightBlackPov(StringBuilder result, ChessPiece[][] display, List<ChessPosition> endPositions, ChessPosition targetPos) {
        for(int i = 0; i < 8; i++)
        {
            result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                    .append(EscapeSequences.SET_TEXT_COLOR_GREEN + " " + (i+1) + " ");
            // Pre
            for (int j = 7; j >= 0; j--)
            {
                ChessPosition scan = new ChessPosition(i + 1, j + 1);
                if (endPositions.contains(scan)) {
                    drawHighlightTile(i, j, result);
                    checkPieces(i, j, result, display, Boolean.TRUE, Boolean.FALSE);
                } else if (scan.equals(targetPos)) {
                    result.append(EscapeSequences.SET_BG_COLOR_MAGENTA);
                    checkPieces(i, j, result, display, Boolean.FALSE, Boolean.TRUE);
                } else {
                    drawTile(i, j, result);
                    checkPieces(i, j, result, display, Boolean.FALSE, Boolean.FALSE);
                }
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
            result.append(EscapeSequences.SET_BG_COLOR_BLACK);
        } else {
            result.append(EscapeSequences.SET_BG_COLOR_WHITE);
        }
    }

    private void drawHighlightTile(int i, int j, StringBuilder result) {
        if((i + j) % 2 == 0) {
            result.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
        } else {
            result.append(EscapeSequences.SET_BG_COLOR_GREEN);
        }
    }

    private void drawPiece(int i, int j, StringBuilder result, ChessPiece[][] display, Boolean highlight, Boolean target) {

        if (highlight) {
            result.append(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        } else if (target) {
            result.append(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        } else if (display[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
            result.append(EscapeSequences.SET_TEXT_COLOR_RED);
        } else {
            result.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
        }

        String piece;
        if (display[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
            piece = switch(display[i][j].getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
            result.append(piece);
            return;
        }
        if (display[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
            piece = switch(display[i][j].getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
            result.append(piece);
        }
    }

    private ChessPosition parseTileID(String tileID) {
        char colChar = tileID.charAt(0);
        char rowChar = tileID.charAt(1);

        int colInt = colChar - 'a' + 1;
        int rowInt = Character.getNumericValue(rowChar);

        return new ChessPosition(rowInt, colInt);
    }

    private void checkPieces(int i, int j, StringBuilder result, ChessPiece[][] display, Boolean highlight, Boolean target) {
        if(display[i][j] != null) {
            drawPiece(i, j, result, display, highlight, target);
        } else {
            result.append("   ");
        }
    }
}
