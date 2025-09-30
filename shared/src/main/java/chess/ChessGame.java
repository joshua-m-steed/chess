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
        if(target_piece != null) {
            return target_piece.pieceMoves(board, startPosition);
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

        System.out.println(startPos + " and " + endPos);
        throw new RuntimeException("Not implemented");
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
                    if(board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor) {
                        targetKingPos = new ChessPosition(i + 1, j + 1);
                    }
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
                    System.out.println("I HIT HIM\n");
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
//            for(int j = 0; j < 8; j++) {
//                System.out.println(current_board[i][j]);
//            }
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
