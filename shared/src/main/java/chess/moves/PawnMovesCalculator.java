package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMovesCalculator implements CalculateMoves {
    @Override
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPos) {
        List<ChessMove> pawnMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPos);

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(myPos.getRow() == 2) {
                calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn()), pawnMoves, null);
            }

            if(myPos.getRow() == 7) {
                for(ChessPiece.PieceType piecetype : ChessPiece.PieceType.values()) {
                    if(piecetype != ChessPiece.PieceType.PAWN && piecetype != ChessPiece.PieceType.KING) {
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), pawnMoves, piecetype);
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), pawnMoves, piecetype);
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), pawnMoves, piecetype);
                    }
                }
                return pawnMoves;
            }

            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), pawnMoves, null);
            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), pawnMoves, null);
            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), pawnMoves, null);
        }

        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if(myPos.getRow() == 7) {
                calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn()), pawnMoves, null);
            }

            if(myPos.getRow() == 2) {
                for(ChessPiece.PieceType piecetype : ChessPiece.PieceType.values()) {
                    if(piecetype != ChessPiece.PieceType.PAWN && piecetype != ChessPiece.PieceType.KING) {
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), pawnMoves, piecetype);
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), pawnMoves, piecetype);
                        calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), pawnMoves, piecetype);
                    }
                }
                return pawnMoves;
            }

            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), pawnMoves, null);
            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), pawnMoves, null);
            calculatePawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), pawnMoves, null);
        }
        return pawnMoves;
    }

    private void calculatePawnMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, List<ChessMove> pawnMoves, ChessPiece.PieceType promotionType) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if((myPos.getRow() + 1 == newPos.getRow() && myPos.getColumn() == newPos.getColumn()) | (myPos.getRow() - 1 == newPos.getRow() && myPos.getColumn() == newPos.getColumn())) {
            if(nextTile == null) {
                pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promotionType));
            }
            else {
                return;
            }
        }
        else if((myPos.getRow() + 2 == newPos.getRow() && myPos.getColumn() == newPos.getColumn()) | (myPos.getRow() - 2 == newPos.getRow() && myPos.getColumn() == newPos.getColumn())) {
            if(myPos.getRow() + 2 == newPos.getRow()) {
                ChessPosition prevPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn());
                ChessPiece prevTile = board.getPiece(prevPos);

                if(prevTile != null) {
                    return;
                }
                else {
                    if(nextTile == null) {
                        pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promotionType));
                    }
                    else {
                        return;
                    }
                }
            }
            else if (myPos.getRow() - 2 == newPos.getRow()) {
                ChessPosition prevPos = new ChessPosition(myPos.getRow() - 1, myPos.getColumn());
                ChessPiece prevTile = board.getPiece(prevPos);

                if(prevTile != null) {
                    return;
                }
                else {
                    if(nextTile == null) {
                        pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promotionType));
                    }
                    else {
                        return;
                    }
                }
            }
        }

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promotionType));
            }
        }
    }
}
