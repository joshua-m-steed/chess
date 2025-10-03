package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> queenMoves = new ArrayList<>();

        queenMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), 1, 0, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), -1, 0, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() + 1), 0, 1, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow(), myPos.getColumn() - 1), 0, -1, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), 1 , 1, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), -1 , -1, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), 1 , -1, queenMoves);
        queenMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), -1 , 1, queenMoves);

        return queenMoves;
    }

    private List<ChessMove> queenMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, int dx, int dy,  List<ChessMove> queenMoves) {
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

        return queenMoves(board, myPos, new ChessPosition(newPos.getRow() + dx,newPos.getColumn() + dy), dx, dy, queenMoves);
    }
}
