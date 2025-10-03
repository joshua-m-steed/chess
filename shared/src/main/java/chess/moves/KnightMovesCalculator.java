package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> knightMoves = new ArrayList<>();

        knightMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn() - 1), knightMoves);
        knightMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn() + 1), knightMoves);

        knightMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn() - 1), knightMoves);
        knightMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn() + 1), knightMoves);

        knightMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 2), knightMoves);
        knightMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 2), knightMoves);

        knightMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 2), knightMoves);
        knightMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 2), knightMoves);

        return knightMoves;
    }

    private void knightMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, List<ChessMove> knightMoves) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                knightMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return;
        }

        knightMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

    }
}
