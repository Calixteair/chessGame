package com.chessgame.model;

/**
 * État d'une partie. Tout état autre que {@link #ONGOING} est une fin de partie.
 */
public enum GameStatus {
    ONGOING,
    CHECKMATE,
    STALEMATE,
    DRAW_FIFTY_MOVES,
    DRAW_REPETITION,
    DRAW_INSUFFICIENT_MATERIAL,
    RESIGNATION,
    ABANDONED;

    public boolean isOver() {
        return this != ONGOING;
    }

    public boolean isDraw() {
        return this == STALEMATE || this == DRAW_FIFTY_MOVES
                || this == DRAW_REPETITION || this == DRAW_INSUFFICIENT_MATERIAL;
    }
}
