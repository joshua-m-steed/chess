package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> queenMoves = new ArrayList<>();

        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), 1, 0, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), -1, 0, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() + 1), 0, 1, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() - 1), 0, -1, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), 1 , 1, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), -1 , -1, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), 1 , -1, queenMoves);
        recursiveMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), -1 , 1, queenMoves);

        return queenMoves;
    }
}
