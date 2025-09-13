package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        if(piece.getPieceType() == PieceType.BISHOP) {
            calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), 1 , 1, possibleMoves);
            calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), -1 , -1, possibleMoves);
            calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), 1 , -1, possibleMoves);
            calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), -1 , 1, possibleMoves);
//            System.out.println(possibleMoves); // DEBUG PRINT FOR FULL MOVE COLLECTION
        }

        if(piece.getPieceType() == PieceType.ROOK) {
            calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), 1, 0, possibleMoves);
            calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), -1, 0, possibleMoves);
            calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), 0, 1, possibleMoves);
            calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), 0, -1, possibleMoves);
//            System.out.println(possibleMoves); // DEBUG PRINT FOR FULL MOVE COLLECTION
        }

        if(piece.getPieceType() == PieceType.QUEEN) {
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), 1, 0, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), -1, 0, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), 0, 1, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), 0, -1, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), 1 , 1, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), -1 , -1, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), 1 , -1, possibleMoves);
            calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), -1 , 1, possibleMoves);
        }

        if(piece.getPieceType() == PieceType.KING) {
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), 1, 0, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), -1, 0, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), 0, 1, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), 0, -1, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), 1 , 1, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), -1 , -1, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), 1 , -1, possibleMoves);
            calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), -1 , 1, possibleMoves);
        }

        return possibleMoves;
    }

    private List<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition myPos, ChessPosition pos, int dx, int dy,  List<ChessMove> possibleMoves)
    {
        if(pos.getRow() < 1 | pos.getColumn() < 1 | pos.getRow() >= 9 | pos.getColumn() >= 9)
        {
//            System.out.println("Sorry, out of bounds");
            return possibleMoves;
        }

        ChessPiece tile = board.getPiece(pos);
        ChessPiece myPiece = board.getPiece(myPos);
        if(tile != null)
        {
            if(tile.getTeamColor() != myPiece.getTeamColor())
            {
                // SPEAK when you are a piece! ANNOUNCE YOURSELF!
//                System.out.println("YES, HELLO, I AM NOT NULL");
                possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null));
            }
            return possibleMoves;
        }

        // Readability for my pleasure
//        System.out.println("RECURSIVE");
//        System.out.println(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        return calculateBishopMoves(board, myPos, new ChessPosition(pos.getRow() + dx,pos.getColumn() + dy), dx, dy, possibleMoves);
    }

    private List<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition myPos, ChessPosition pos, int dx, int dy,  List<ChessMove> possibleMoves)
    {
        if(pos.getRow() < 1 | pos.getColumn() < 1 | pos.getRow() >= 9 | pos.getColumn() >= 9)
        {
//            System.out.println("Sorry, out of bounds");
            return possibleMoves;
        }

        ChessPiece tile = board.getPiece(pos);
        ChessPiece myPiece = board.getPiece(myPos);
        if(tile != null)
        {
            if(tile.getTeamColor() != myPiece.getTeamColor())
            {
                // SPEAK when you are a piece! ANNOUNCE YOURSELF!
//                System.out.println("YES, HELLO, I AM NOT NULL");
                possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null));
            }
            return possibleMoves;
        }

        // Readability for my pleasure
//        System.out.println("RECURSIVE");
//        System.out.println(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        return calculateRookMoves(board, myPos, new ChessPosition(pos.getRow() + dx,pos.getColumn() + dy), dx, dy, possibleMoves);
    }

    private List<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition myPos, ChessPosition pos, int dx, int dy,  List<ChessMove> possibleMoves)
    {
        if(pos.getRow() < 1 | pos.getColumn() < 1 | pos.getRow() >= 9 | pos.getColumn() >= 9)
        {
//            System.out.println("Sorry, out of bounds");
            return possibleMoves;
        }

        ChessPiece tile = board.getPiece(pos);
        ChessPiece myPiece = board.getPiece(myPos);
        if(tile != null)
        {
            if(tile.getTeamColor() != myPiece.getTeamColor())
            {
                // SPEAK when you are a piece! ANNOUNCE YOURSELF!
//                System.out.println("YES, HELLO, I AM NOT NULL");
                possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null));
            }
            return possibleMoves;
        }

        // Readability for my pleasure
//        System.out.println("RECURSIVE");
//        System.out.println(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        return calculateQueenMoves(board, myPos, new ChessPosition(pos.getRow() + dx,pos.getColumn() + dy), dx, dy, possibleMoves);
    }

    private List<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition myPos, ChessPosition pos, int dx, int dy,  List<ChessMove> possibleMoves)
    {
        if(pos.getRow() < 1 | pos.getColumn() < 1 | pos.getRow() >= 9 | pos.getColumn() >= 9)
        {
//            System.out.println("Sorry, out of bounds");
            return possibleMoves;
        }

        ChessPiece tile = board.getPiece(pos);
        ChessPiece myPiece = board.getPiece(myPos);
        if(tile != null)
        {
            if(tile.getTeamColor() != myPiece.getTeamColor())
            {
                // SPEAK when you are a piece! ANNOUNCE YOURSELF!
//                System.out.println("YES, HELLO, I AM NOT NULL");
                possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null));
            }
            return possibleMoves;
        }

        // Readability for my pleasure
//        System.out.println("RECURSIVE");
//        System.out.println(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        possibleMoves.add(new ChessMove(myPos, new ChessPosition(pos.getRow(), pos.getColumn()), null ));

        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "Piece: " +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
