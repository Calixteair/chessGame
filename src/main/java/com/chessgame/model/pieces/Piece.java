package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.io.Serializable;
import java.util.List;

/**
 * Pièce d'échecs immuable : une couleur et un type.
 * La position est portée par le plateau, pas par la pièce.
 *
 * Chaque sous-classe génère ses coups « pseudo-légaux » (sans vérifier si son propre roi
 * reste en échec) ; le filtrage final est fait par {@link Board#legalMoves()}.
 */
public abstract class Piece implements Serializable {

    public static final int[][] ORTHOGONAL = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    public static final int[][] DIAGONAL = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public static final int[][] ALL_DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private final boolean white;

    protected Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public abstract TypePiece getType();

    /** Ajoute à {@code out} les coups pseudo-légaux de cette pièce située en (x, y). */
    public abstract void addPseudoLegalMoves(Board board, int x, int y, List<Move> out);

    /** Lettre FEN : majuscule pour les blancs, minuscule pour les noirs. */
    public char getFenSymbol() {
        char c = getType().getSymbol();
        return white ? Character.toUpperCase(c) : c;
    }

    public static Piece create(TypePiece type, boolean white) {
        switch (type) {
            case PAWN: return new Pawn(white);
            case KNIGHT: return new Knight(white);
            case BISHOP: return new Bishop(white);
            case ROOK: return new Rook(white);
            case QUEEN: return new Queen(white);
            case KING: return new King(white);
            default: throw new IllegalArgumentException("Type inconnu : " + type);
        }
    }

    /** Déplacement glissant (tour, fou, dame) : avance jusqu'au bord, une pièce amie ou une prise. */
    protected void slide(Board board, int x, int y, int[][] directions, List<Move> out) {
        for (int[] d : directions) {
            int nx = x + d[0];
            int ny = y + d[1];
            while (Board.isInside(nx, ny)) {
                Piece target = board.getPiece(nx, ny);
                if (target == null) {
                    out.add(new Move(x, y, nx, ny));
                } else {
                    if (target.isWhite() != white) {
                        out.add(new Move(x, y, nx, ny));
                    }
                    break;
                }
                nx += d[0];
                ny += d[1];
            }
        }
    }

    /** Déplacement d'une case par décalage (cavalier, roi). */
    protected void step(Board board, int x, int y, int[][] offsets, List<Move> out) {
        for (int[] d : offsets) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (Board.isInside(nx, ny)) {
                Piece target = board.getPiece(nx, ny);
                if (target == null || target.isWhite() != white) {
                    out.add(new Move(x, y, nx, ny));
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(getFenSymbol());
    }
}
