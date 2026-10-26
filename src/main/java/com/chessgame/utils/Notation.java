package com.chessgame.utils;

import com.chessgame.model.Board;
import com.chessgame.model.pieces.Piece;

import java.util.List;

/**
 * Notation algébrique standard (SAN) : e4, Nxf3, exd5, O-O, e8=Q+, Qh4#…
 */
public final class Notation {

    private Notation() {
    }

    /**
     * SAN sans suffixe d'échec (voir {@link #checkSuffix}).
     * @param before position avant le coup
     * @param move   coup légal dans {@code before}
     * @param legal  coups légaux de {@code before} (pour lever les ambiguïtés)
     */
    public static String toSan(Board before, Move move, List<Move> legal) {
        Piece piece = before.getPiece(move.getStartX(), move.getStartY());
        int dx = move.getEndX() - move.getStartX();
        if (piece.getType() == TypePiece.KING && Math.abs(dx) == 2) {
            return dx > 0 ? "O-O" : "O-O-O";
        }
        boolean capture = before.getPiece(move.getEndX(), move.getEndY()) != null;
        String target = Move.squareName(move.getEndX(), move.getEndY());

        if (piece.getType() == TypePiece.PAWN) {
            capture |= dx != 0; // prise en passant : la case d'arrivée est vide
            String san = capture ? (char) ('a' + move.getStartX()) + "x" + target : target;
            return move.getPromotion() == null ? san
                    : san + "=" + Character.toUpperCase(move.getPromotion().getSymbol());
        }
        char letter = Character.toUpperCase(piece.getType().getSymbol());
        return letter + disambiguation(before, move, legal) + (capture ? "x" : "") + target;
    }

    /** "+" si le coup donne échec, "#" s'il mate, "" sinon. */
    public static String checkSuffix(boolean check, boolean mate) {
        return mate ? "#" : check ? "+" : "";
    }

    /** Ajoute la colonne, la rangée, ou les deux si une autre pièce identique peut aller sur la même case. */
    private static String disambiguation(Board before, Move move, List<Move> legal) {
        TypePiece type = before.getPiece(move.getStartX(), move.getStartY()).getType();
        boolean ambiguous = false;
        boolean sameFile = false;
        boolean sameRank = false;
        for (Move other : legal) {
            boolean rival = other.getEndX() == move.getEndX() && other.getEndY() == move.getEndY()
                    && !(other.getStartX() == move.getStartX() && other.getStartY() == move.getStartY())
                    && before.getPiece(other.getStartX(), other.getStartY()).getType() == type;
            if (rival) {
                ambiguous = true;
                sameFile |= other.getStartX() == move.getStartX();
                sameRank |= other.getStartY() == move.getStartY();
            }
        }
        if (!ambiguous) {
            return "";
        }
        String square = Move.squareName(move.getStartX(), move.getStartY());
        if (!sameFile) {
            return square.substring(0, 1);
        }
        return sameRank ? square : square.substring(1);
    }
}
