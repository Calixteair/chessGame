package com.chessgame.utils;

/**
 * Types de pièces, avec leur lettre en notation FEN / algébrique (anglaise).
 */
public enum TypePiece {
    PAWN('p'), KNIGHT('n'), BISHOP('b'), ROOK('r'), QUEEN('q'), KING('k');

    private final char symbol;

    TypePiece(char symbol) {
        this.symbol = symbol;
    }

    /** Lettre minuscule : p, n, b, r, q, k. */
    public char getSymbol() {
        return symbol;
    }

    public static TypePiece fromSymbol(char c) {
        char lower = Character.toLowerCase(c);
        for (TypePiece type : values()) {
            if (type.symbol == lower) {
                return type;
            }
        }
        throw new IllegalArgumentException("Pièce inconnue : " + c);
    }

    /** Pièces autorisées pour une promotion, de la plus forte à la plus faible. */
    public static final TypePiece[] PROMOTIONS = {QUEEN, ROOK, BISHOP, KNIGHT};
}
