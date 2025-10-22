package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> bishopMoves = new ArrayList<>();

        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), 1 , 1, bishopMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), -1 , -1, bishopMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), 1 , -1, bishopMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), -1 , 1, bishopMoves);

        return bishopMoves;
    }
}
