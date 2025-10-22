package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> kingMoves = new ArrayList<>();

        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() + 1), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() - 1), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), kingMoves);
        singleMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), kingMoves);

        return kingMoves;
    }
}
