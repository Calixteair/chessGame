package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.List;

/**
 * Pion : avance d'une case (deux depuis sa case de départ), prend en diagonale,
 * prend en passant et se promeut en dame, tour, fou ou cavalier sur la dernière rangée.
 */
public class Pawn extends Piece {

    public Pawn(boolean white) {
        super(white);
    }

    /** Sens de marche en y : les blancs montent (y décroît), les noirs descendent. */
    public static int direction(boolean white) {
        return white ? -1 : 1;
    }

    @Override
    public void addPseudoLegalMoves(Board board, int x, int y, List<Move> out) {
        int dir = direction(isWhite());
        int startRow = isWhite() ? 6 : 1;
        int oneStep = y + dir;
        if (!Board.isInside(x, oneStep)) {
            return;
        }
        if (board.getPiece(x, oneStep) == null) {
            addWithPromotions(x, y, x, oneStep, out);
            int twoSteps = y + 2 * dir;
            if (y == startRow && board.getPiece(x, twoSteps) == null) {
                out.add(new Move(x, y, x, twoSteps));
            }
        }
        for (int dx = -1; dx <= 1; dx += 2) {
            int tx = x + dx;
            if (!Board.isInside(tx, oneStep)) {
                continue;
            }
            Piece target = board.getPiece(tx, oneStep);
            boolean capture = target != null && target.isWhite() != isWhite();
            boolean enPassant = board.isEnPassantSquare(tx, oneStep);
            if (capture || enPassant) {
                addWithPromotions(x, y, tx, oneStep, out);
            }
        }
    }

    private void addWithPromotions(int x, int y, int tx, int ty, List<Move> out) {
        if (ty == 0 || ty == 7) {
            for (TypePiece promo : TypePiece.PROMOTIONS) {
                out.add(new Move(x, y, tx, ty, promo));
            }
        } else {
            out.add(new Move(x, y, tx, ty));
        }
    }

    @Override
    public TypePiece getType() {
        return TypePiece.PAWN;
    }
}
