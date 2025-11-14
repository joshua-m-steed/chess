package model;

import chess.ChessGame;

public record GameJoin(ChessGame.TeamColor playerColor, Integer gameID) {
}