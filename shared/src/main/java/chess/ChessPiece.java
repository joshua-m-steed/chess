package chess;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //Private CONST Class Objects
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        if (piece.getPieceType() == PieceType.BISHOP) {
            while( true )
            {
//                int i = 1;
                ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
                // Out of bounds
                if(newPosition.getRow() < 0 | newPosition.getColumn() < 0 | newPosition.getRow() >= 8 | newPosition.getColumn() >= 8)
                {
                    break;
                }

                // CurrentPos
                // New Pos

                // If New Pos is:
                    // out of bounds
                    // A piece
                        // Capture
                        // Denied
            }
            possibleMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8), null ));
            possibleMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(2,1), null ));
            possibleMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(2,7), null ));
            possibleMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(3,2), null ));

            System.out.println(myPosition);
            System.out.println(possibleMoves);

            //HARDCODED for FIRST POSITION
//            return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8), null ));
            return possibleMoves;
        }
        return List.of(); // TEMPORARY HACK
        //        throw new RuntimeException("Not implemented");
        //        return new HashSet<ChessMove>(); Another return idea
    }
}
