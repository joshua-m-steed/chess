package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> bishopMoves = new ArrayList<>();

        calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), 1 , 1, bishopMoves);
        calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), -1 , -1, bishopMoves);
        calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), 1 , -1, bishopMoves);
        calculateBishopMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), -1 , 1, bishopMoves);

        return bishopMoves;
    }

    private List<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, int dx, int dy,  List<ChessMove> bishopMoves) {
        // Out of bounds base case
        if (newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return bishopMoves;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        // Check for piece color
        if (nextTile != null) {
            if (nextTile.getTeamColor() != myPiece.getTeamColor()) {
                bishopMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return bishopMoves;
        }

        bishopMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

        return calculateBishopMoves(board, myPos, new ChessPosition(newPos.getRow() + dx,newPos.getColumn() + dy), dx, dy, bishopMoves);
    }
}
