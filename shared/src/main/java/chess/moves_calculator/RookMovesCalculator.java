package chess.moves_calculator;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class RookMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> rookMoves = new ArrayList<>();

        calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), 1, 0, rookMoves);
        calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), -1, 0, rookMoves);
        calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), 0, 1, rookMoves);
        calculateRookMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), 0, -1, rookMoves);

        return rookMoves;
    }

    private List<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, int dx, int dy,  List<ChessMove> rookMoves) {
        // Out of bounds base case
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return rookMoves;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        // Check for piece color
        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor())
            {
                rookMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return rookMoves;
        }

        rookMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

        return calculateRookMoves(board, myPos, new ChessPosition(newPos.getRow() + dx,newPos.getColumn() + dy), dx, dy, rookMoves);
    }
}
