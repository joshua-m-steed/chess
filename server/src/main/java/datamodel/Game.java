package datamodel;

import chess.ChessGame;

public record Game(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
