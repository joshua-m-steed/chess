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
                pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 2, myPos.getColumn()), pawnMoves, null);
            }

            if(myPos.getRow() == 7) {
                for(ChessPiece.PieceType piecetype : ChessPiece.PieceType.values()) {
                    if(piecetype != ChessPiece.PieceType.PAWN && piecetype != ChessPiece.PieceType.KING) {
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), pawnMoves, piecetype);
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), pawnMoves, piecetype);
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), pawnMoves, piecetype);
                    }
                }
                return pawnMoves;
            }

            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn()), pawnMoves, null);
            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() + 1), pawnMoves, null);
            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() + 1, myPos.getColumn() - 1), pawnMoves, null);
        }

        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if(myPos.getRow() == 7) {
                pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 2, myPos.getColumn()), pawnMoves, null);
            }

            if(myPos.getRow() == 2) {
                for(ChessPiece.PieceType piecetype : ChessPiece.PieceType.values()) {
                    if(piecetype != ChessPiece.PieceType.PAWN && piecetype != ChessPiece.PieceType.KING) {
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), pawnMoves, piecetype);
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), pawnMoves, piecetype);
                        pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), pawnMoves, piecetype);
                    }
                }
                return pawnMoves;
            }

            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn()), pawnMoves, null);
            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() + 1), pawnMoves, null);
            pawnMoves(board, myPos, new ChessPosition(myPos.getRow() - 1, myPos.getColumn() - 1), pawnMoves, null);
        }
        return pawnMoves;
    }

    private void pawnMoves(ChessBoard board, ChessPosition myPos, ChessPosition newPos, List<ChessMove> pawnMoves, ChessPiece.PieceType promoType) {
        if(newPos.getRow() < 1 | newPos.getColumn() < 1 | newPos.getRow() >= 9 | newPos.getColumn() >= 9) {
            return;
        }

        ChessPiece nextTile = board.getPiece(newPos);
        ChessPiece myPiece = board.getPiece(myPos);
        int rowDiff = myPos.getRow() - newPos.getRow();
        if(myPos.getColumn() == newPos.getColumn() && (rowDiff < 0 ? -rowDiff : rowDiff) == 1) {
            if(nextTile == null) {
                pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promoType));
            }
            else {
                return;
            }
        }
        else if(myPos.getColumn() == newPos.getColumn() && (rowDiff < 0 ? -rowDiff : rowDiff) == 2) {
            if(myPos.getRow() + 2 == newPos.getRow()) {
                ChessPosition prevPos = new ChessPosition(myPos.getRow() + 1, myPos.getColumn());
                ChessPiece prevTile = board.getPiece(prevPos);

                if(prevTile != null) {
                    return;
                }
                else {
                    if(nextTile == null) {
                        pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promoType));
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
                        pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promoType));
                    }
                    else {
                        return;
                    }
                }
            }
        }

        if(nextTile != null) {
            if(nextTile.getTeamColor() != myPiece.getTeamColor()) {
                pawnMoves.add(new ChessMove(myPos, new ChessPosition(newPos.getRow(), newPos.getColumn()), promoType));
            }
        }
    }
}
