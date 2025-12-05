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
    private WinCondition winState;
    private PlayerState whiteState;
    private PlayerState blackState;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.winState = WinCondition.IN_PLAY;
        this.whiteState = PlayerState.IN_PLAY;
        this.blackState = PlayerState.IN_PLAY;
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

    public WinCondition getWinCondition() {
        return winState;
    }

    public PlayerState getWhiteState() {
        return whiteState;
    }

    public PlayerState getBlackState() {
        return blackState;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        OBSERVER
    }

    public enum WinCondition {
        IN_PLAY,
        RESIGN,
        CHECKMATE,
        STALEMATE
    }

    public enum PlayerState {
        IN_PLAY,
        IN_CHECK,
        STALEMATE,
        RESIGNED,
        LOST,
        WON
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

        kingPiecePos = scanForEnemyMove(this.board, board, teamColor, enemyMoves);

        if(kingPiecePos != null) {
            return isKingInWay(kingPiecePos, enemyMoves);
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

        scanForTeamMove(board, teamColor, teamMoves);

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

        scanForTeamMove(board, teamColor, teamMoves);

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
        ChessPiece[][] boardCurrent = this.board.getBoard();
        ChessPiece[][] boardNew = board.getBoard();

        for(int i = 0; i < 8; i++) {
            System.arraycopy(boardNew[i], 0, boardCurrent[i], 0, 8);
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

    public void resign(TeamColor team) {
        if (team == TeamColor.WHITE) {
            whiteState = PlayerState.RESIGNED;
            blackState = PlayerState.WON;

            winState = WinCondition.RESIGN;
        } else if (team == TeamColor.BLACK) {
            whiteState = PlayerState.WON;
            blackState = PlayerState.RESIGNED;

            winState = WinCondition.RESIGN;
        }
    }

    public void updateGameState() {
        if (isInCheckmate(TeamColor.WHITE)) {
            whiteState = PlayerState.LOST;
            blackState = PlayerState.WON;

            winState = WinCondition.CHECKMATE;
        } else if (isInCheckmate(TeamColor.BLACK)) {
            whiteState = PlayerState.LOST;
            blackState = PlayerState.WON;

            winState = WinCondition.CHECKMATE;
        }

        if (isInCheck(TeamColor.WHITE)) {
            whiteState = PlayerState.IN_CHECK;
            blackState = PlayerState.IN_PLAY;

            winState = WinCondition.CHECKMATE;
        } else if (isInCheck(TeamColor.BLACK)) {
            whiteState = PlayerState.IN_PLAY;
            blackState = PlayerState.IN_CHECK;

            winState = WinCondition.IN_PLAY;
        }

        if(isInStalemate(TeamColor.WHITE) || isInStalemate(TeamColor.BLACK)) {
            whiteState = PlayerState.STALEMATE;
            blackState = PlayerState.STALEMATE;

            winState = WinCondition.STALEMATE;
        }
    }

    private boolean isInCheckHelper(TeamColor teamColor, ChessBoard board, ChessMove move) {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());

        ChessPiece[][] boardIter = board.getBoard();
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingPiecePos = null;

        kingPiecePos = scanForEnemyMove(board, boardIter, teamColor, enemyMoves);

        if(kingPiecePos != null) {
            return isKingInWay(kingPiecePos, enemyMoves);
        }

        return false;
    }

    private boolean isKingInWay(ChessPosition kingPiecePos, Collection<ChessMove> enemyMoves) {
        for(ChessMove move : enemyMoves) {
            if((kingPiecePos.getColumn() == move.getEndPosition().getColumn()) && (kingPiecePos.getRow() == move.getEndPosition().getRow()))  {
                return true;
            }
        }
        return false;
    }

    private void scanForTeamMove(ChessPiece[][] boardIter, TeamColor teamColor, Collection<ChessMove> foundMoves) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(boardIter[i][j] == null) {
                    continue;
                }
                teamMoveHelper(boardIter, i, j, teamColor, foundMoves);
            }
        }
    }

    private void teamMoveHelper(ChessPiece[][] boardIter, int i, int j, TeamColor teamColor, Collection<ChessMove> foundMoves) {
        if(teamColor == boardIter[i][j].getTeamColor()) {
            Collection<ChessMove> moves = boardIter[i][j].pieceMoves(this.board, new ChessPosition(i + 1, j + 1));
            if(moves != null) {
                foundMoves.addAll(moves);
            }
        }
    }

    private ChessPosition scanForEnemyMove(ChessBoard board, ChessPiece[][] boardIter, TeamColor teamColor, Collection<ChessMove> foundMoves) {
        ChessPosition kingPiecePos = null;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(boardIter[i][j] == null) {
                    continue;
                }
                if(boardIter[i][j].getPieceType() == ChessPiece.PieceType.KING && boardIter[i][j].getTeamColor() == teamColor) {
                    kingPiecePos = new ChessPosition(i + 1, j + 1);
                }
                else if(boardIter[i][j].getTeamColor() != teamColor)
                {
                    Collection<ChessMove> moves = boardIter[i][j].pieceMoves(board, new ChessPosition(i + 1, j + 1));
                    if(moves != null) {
                        foundMoves.addAll(moves);
                    }
                }
            }
        }
        return kingPiecePos;
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
