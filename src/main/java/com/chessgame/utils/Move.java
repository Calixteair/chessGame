package com.chessgame.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un coup : case de départ, case d'arrivée et éventuelle pièce de promotion.
 *
 * Repère : x = colonne (0 = a … 7 = h), y = ligne affichée (0 = rangée 8 en haut, 7 = rangée 1 en bas).
 * Les blancs commencent donc en y = 6 / 7 et avancent vers y = 0.
 */
public final class Move implements Serializable {

    private static final long serialVersionUID = 2L;

    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final TypePiece promotion;

    public Move(int startX, int startY, int endX, int endY) {
        this(startX, startY, endX, endY, null);
    }

    public Move(int startX, int startY, int endX, int endY, TypePiece promotion) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.promotion = promotion;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    /** Pièce de promotion, ou null si le coup n'est pas une promotion. */
    public TypePiece getPromotion() {
        return promotion;
    }

    /** Nom de case algébrique, ex. (4, 6) → "e2". */
    public static String squareName(int x, int y) {
        return "" + (char) ('a' + x) + (8 - y);
    }

    /** Notation UCI : "e2e4", "e7e8q". */
    public String toUci() {
        String uci = squareName(startX, startY) + squareName(endX, endY);
        return promotion == null ? uci : uci + promotion.getSymbol();
    }

    /**
     * Lit un coup en notation UCI.
     * @throws IllegalArgumentException si le texte n'est pas un coup UCI bien formé.
     */
    public static Move fromUci(String uci) {
        if (uci == null || !uci.matches("[a-h][1-8][a-h][1-8][qrbn]?")) {
            throw new IllegalArgumentException("Coup UCI invalide : " + uci);
        }
        TypePiece promo = uci.length() == 5 ? TypePiece.fromSymbol(uci.charAt(4)) : null;
        return new Move(uci.charAt(0) - 'a', '8' - uci.charAt(1),
                uci.charAt(2) - 'a', '8' - uci.charAt(3), promo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return startX == move.startX && startY == move.startY
                && endX == move.endX && endY == move.endY
                && promotion == move.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY, promotion);
    }

    @Override
    public String toString() {
        return toUci();
    }
}
