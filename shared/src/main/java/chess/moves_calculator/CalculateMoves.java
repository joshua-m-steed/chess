package chess.moves_calculator;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public interface CalculateMoves {
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition);
}
