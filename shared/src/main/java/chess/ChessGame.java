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
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves;
        Collection<ChessMove> rejectMoves = new ArrayList<>();

        if(piece != null) {
            validMoves = piece.pieceMoves(board, startPosition);
            for(ChessMove move : validMoves) {
                ChessGame mockGame = new ChessGame();
                mockGame.setBoard(this.board);

                if(isInCheckHelper(piece.getTeamColor(), mockGame.getBoard(), move)) {
                    rejectMoves.add(move);
                }
            }
        } else {
            return null;
        }

        for(ChessMove move : rejectMoves) {
            validMoves.remove(move);
            if(validMoves.contains(move)) {
                System.out.println("I am a liar, I hid the move!");
            }
        }

        return validMoves;
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
        ChessPiece piece = this.board.getPiece(startPos);

        if(piece == null) {
            throw new InvalidMoveException("There is no piece there!");
        }

        Collection<ChessMove> validMoves = validMoves(startPos);

        if(!validMoves.contains(move) || piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("This is not a valid move!");
        }

        this.board.removePiece(startPos);
        ChessPiece pieceNew = piece;
        if(move.getPromotionPiece() != null) {
            pieceNew = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        this.board.addPiece(endPos, pieceNew);

        if(teamTurn == TeamColor.WHITE) {
            this.setTeamTurn(TeamColor.BLACK);
        }
        else {
            this.setTeamTurn(TeamColor.WHITE);
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
        ChessPosition kingPiecePos = null;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    if(board[i][j].getPieceType() == ChessPiece.PieceType.KING && board[i][j].getTeamColor() == teamColor) {
                        kingPiecePos = new ChessPosition(i + 1, j + 1);
                    }
                    else if(board[i][j].getTeamColor() != teamColor)
                    {
                        Collection<ChessMove> moves = board[i][j].pieceMoves(this.board, new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            enemyMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(kingPiecePos != null) {
            for(ChessMove move : enemyMoves) {
                if((kingPiecePos.getColumn() == move.getEndPosition().getColumn()) && (kingPiecePos.getRow() == move.getEndPosition().getRow()))  {
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
        if(!isInCheck(teamColor)) {
            return false;
        }

        ChessPiece[][] board = this.board.getBoard();
        Collection<ChessMove> teamMoves = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    if(teamColor == board[i][j].getTeamColor()) {
                        Collection<ChessMove> moves = board[i][j].pieceMoves(this.board, new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            teamMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        for(ChessMove move : teamMoves) {
            ChessGame mockGame = new ChessGame();
            mockGame.setBoard(this.board);

            if(!isInCheckHelper(teamColor, mockGame.getBoard(), move)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }

        ChessPiece[][] board = this.board.getBoard();
        Collection<ChessMove> teamMoves = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    if(teamColor == board[i][j].getTeamColor()) {
                        Collection<ChessMove> moves = board[i][j].pieceMoves(this.board, new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            teamMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        for(ChessMove move : teamMoves) {
            ChessGame mockGame = new ChessGame();
            mockGame.setBoard(this.board);

            if(!isInCheckHelper(teamColor, mockGame.getBoard(), move)) {
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

    private boolean isInCheckHelper(TeamColor teamColor, ChessBoard board, ChessMove move) {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());

        ChessPiece[][] boardIter = board.getBoard();
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingPiecePos = null;


        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(boardIter[i][j] != null) {
                    if(boardIter[i][j].getPieceType() == ChessPiece.PieceType.KING && boardIter[i][j].getTeamColor() == teamColor) {
                        kingPiecePos = new ChessPosition(i + 1, j + 1);
                    }
                    else if(boardIter[i][j].getTeamColor() != teamColor)
                    {
                        Collection<ChessMove> moves = boardIter[i][j].pieceMoves(board, new ChessPosition(i + 1, j + 1));
                        if(moves != null) {
                            enemyMoves.addAll(moves);
                        }
                    }
                }
            }
        }

        if(kingPiecePos != null) {
            for(ChessMove moves : enemyMoves) {
                if((kingPiecePos.getColumn() == moves.getEndPosition().getColumn()) && (kingPiecePos.getRow() == moves.getEndPosition().getRow())) {
                    return true;
                }
            }
        }

        return false;
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
