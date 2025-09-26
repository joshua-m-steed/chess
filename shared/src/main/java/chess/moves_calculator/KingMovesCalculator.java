package chess.moves_calculator;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> kingMoves = new ArrayList<>();

        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), kingMoves);
        calculateKingMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), kingMoves);

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
