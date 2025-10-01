package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    final private ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        System.out.println(teamTurn);
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece target_piece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> rejectMoves = new ArrayList<>();



        if(target_piece != null) {
            validMoves = target_piece.pieceMoves(board, startPosition);
//            System.out.println("SET MOVES - " + validMoves);
//            for(ChessMove move : validMoves) {
//                ChessGame false_game = new ChessGame();
//                false_game.setBoard(this.board);
//
//                System.out.println(move.getStartPosition() + " --> " + move.getEndPosition());
//                false_game.getBoard().addPiece(move.getEndPosition(), target_piece);
//                false_game.getBoard().removePiece(move.getStartPosition());
//                if(false_game.isInCheck(target_piece.getTeamColor())) {
//                    rejectMoves.add(move);
//                }
//            }
            return validMoves;
        }
        else {
            return null;
        }
        // Still needs to implement checking if "IN CHECK"
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece target_piece = this.board.getPiece(startPos);

        try {
            Collection<ChessMove> validMoves = validMoves(startPos);
            if(validMoves.contains(move) && target_piece.getTeamColor() == teamTurn) {
                System.out.println("You are a " + target_piece + " moving from " + startPos + " to " + endPos);
            }
            else {
                throw new InvalidMoveException("This is not a valid move!");
            }
        } catch (InvalidMoveException e) {
            System.out.println("Sorry, this isn't a valid move: " + startPos + "," + endPos);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece[][] board = this.board.getBoard();
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition targetKingPos = null;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    // Finding the King
                    if(board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor) {
                        targetKingPos = new ChessPosition(i + 1, j + 1);
                    }
                    // Finding Enemy Moves
                    else if(board[i][j].getTeamColor() != teamColor)
                    {
                        Collection<ChessMove> moves = validMoves(new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            enemyMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(targetKingPos != null) {
            for(ChessMove move : enemyMoves) {
                if((targetKingPos.getColumn() == move.getEndPosition().getColumn()) && (targetKingPos.getRow() == move.getEndPosition().getRow()))  {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPiece[][] board = this.board.getBoard();
        Collection<ChessMove> kingMoves = new ArrayList<>();
        ChessPosition targetKingPos;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    // Finding the King
                    if(board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor) {
                        targetKingPos = new ChessPosition(i + 1, j + 1);
                        Collection<ChessMove> moves = validMoves(targetKingPos);
                        if(moves != null) {
                            kingMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(!isInCheck(teamColor)) {
            return false;
        }

        for(ChessMove move : kingMoves) {
            ChessGame false_game = new ChessGame();
            false_game.setBoard(this.board);

            if(isInCheckmateHelper(teamColor, false_game.getBoard(), move)) {
                return true;
            }

//            false_game.getBoard().addPiece(move.getEndPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
//            false_game.getBoard().removePiece(move.getStartPosition());


//            if(!false_game.isInCheck(teamColor)) {
//                return false;
//            }
        }

        return false;
    }

    private boolean isInCheckmateHelper(TeamColor teamColor, ChessBoard board, ChessMove move) {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());

        ChessPiece[][] board_iter = board.getBoard();
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition targetKingPos = null;


        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board_iter[i][j] != null) {
                    // Finding the King
                    if(board_iter[i][j].getPieceType() == ChessPiece.PieceType.KING && board_iter[i][j].getTeamColor() == teamColor) {
                        targetKingPos = new ChessPosition(i + 1, j + 1);
                    }
                    // Finding Enemy Moves
                    else if(board_iter[i][j].getTeamColor() != teamColor)
                    {
                        Collection<ChessMove> moves = validMoves(new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            enemyMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(targetKingPos != null) {
            for(ChessMove move_iter : enemyMoves) {
                if((targetKingPos.getColumn() == move_iter.getEndPosition().getColumn()) && (targetKingPos.getRow() == move_iter.getEndPosition().getRow()))  {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPiece[][] board = this.board.getBoard();
        Collection<ChessMove> kingMoves = new ArrayList<>();
        ChessPosition targetKingPos = null;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    if(board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor) {
                        targetKingPos = new ChessPosition(i + 1, j + 1);
                        Collection<ChessMove> moves = validMoves(targetKingPos);
                        if(moves != null) {
                            kingMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(isInCheck(teamColor)) {
            return false;
        }

        for(ChessMove move : kingMoves) {
            ChessGame false_game = new ChessGame();
            false_game.setBoard(this.board);

            false_game.getBoard().addPiece(move.getEndPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            false_game.getBoard().removePiece(move.getStartPosition());

            if(!false_game.isInCheck(teamColor)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        ChessPiece[][] current_board = this.board.getBoard();
        ChessPiece[][] new_board = board.getBoard();

        for(int i = 0; i < 8; i++) {
            System.arraycopy(new_board[i], 0, current_board[i], 0, 8);
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
