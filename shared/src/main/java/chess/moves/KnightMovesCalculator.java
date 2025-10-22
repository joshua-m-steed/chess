package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> knightMoves = new ArrayList<>();

        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn() - 1), knightMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn() + 1), knightMoves);

        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn() - 1), knightMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn() + 1), knightMoves);

        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 2), knightMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 2), knightMoves);

        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 2), knightMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 2), knightMoves);

        return knightMoves;
    }
}
