package chess.moves;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

public interface CalculateMoves {
    List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos);

    default void singleMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, List<ChessMove> moves) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                moves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return;
        }

        moves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));
    }

    default List<ChessMove> recursiveMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, int dx, int dy, List<ChessMove> moves) {
        // Out of bounds base case
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return moves;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        // Check for piece color
        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                moves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return moves;
        }

        moves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

        return recursiveMoves(board, myPos, new ChessPosition(newPos.getRow() + dx,newPos.getColumn() + dy), dx, dy, moves);
    }
}
