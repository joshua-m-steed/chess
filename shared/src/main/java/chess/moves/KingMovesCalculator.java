package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> kingMoves = new ArrayList<>();

        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() + 1), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), kingMoves);

        return kingMoves;
    }

    private void calculateKingMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, List<ChessMove> kingMoves) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                kingMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return;
        }

        kingMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

    }
}
