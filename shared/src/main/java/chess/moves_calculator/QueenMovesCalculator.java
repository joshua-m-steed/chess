package chess.moves_calculator;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> queenMoves = new ArrayList<>();

        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), 1, 0, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), -1, 0, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), 0, 1, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), 0, -1, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), 1 , 1, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), -1 , -1, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), 1 , -1, queenMoves);
        calculateQueenMoves(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), -1 , 1, queenMoves);

        return queenMoves;
    }

    private List<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, int dx, int dy,  List<ChessMove> queenMoves) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return queenMoves;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                queenMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null));
            }
            return queenMoves;
        }

        queenMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), null ));

        return calculateQueenMoves(board, myPos, new ChessPosition(newPos.getRow() + dx,newPos.getColumn() + dy), dx, dy, queenMoves);
    }
}
