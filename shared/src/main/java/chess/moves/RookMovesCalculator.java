package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class RookMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> rookMoves = new ArrayList<>();

        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), 1, 0, rookMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), -1, 0, rookMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() + 1), 0, 1, rookMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() - 1), 0, -1, rookMoves);

        return rookMoves;
    }
}
