package chess;
import java.util.List;

public interface CalculateMoves {
    public List<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition);
}
