package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.List;

/**
 * Roi : une case dans toutes les directions, plus le roque.
 *
 * Roque autorisé si : le droit de roque existe encore (ni le roi ni la tour concernée n'ont bougé),
 * les cases entre roi et tour sont vides, et le roi n'est pas en échec ni ne traverse / n'arrive
 * sur une case attaquée. Le roi va en g (petit roque) ou en c (grand roque).
 */
public class King extends Piece {

    public King(boolean white) {
        super(white);
    }

    @Override
    public void addPseudoLegalMoves(Board board, int x, int y, List<Move> out) {
        step(board, x, y, ALL_DIRECTIONS, out);
        addCastling(board, x, y, out);
    }

    private void addCastling(Board board, int x, int y, List<Move> out) {
        int homeRow = isWhite() ? 7 : 0;
        if (x != 4 || y != homeRow) {
            return;
        }
        boolean enemy = !isWhite();
        if (board.isAttacked(4, homeRow, enemy)) {
            return;
        }
        if (board.canCastle(isWhite(), true)
                && areEmpty(board, homeRow, 5, 6)
                && !board.isAttacked(5, homeRow, enemy)
                && !board.isAttacked(6, homeRow, enemy)) {
            out.add(new Move(4, homeRow, 6, homeRow));
        }
        if (board.canCastle(isWhite(), false)
                && areEmpty(board, homeRow, 1, 2, 3)
                && !board.isAttacked(3, homeRow, enemy)
                && !board.isAttacked(2, homeRow, enemy)) {
            out.add(new Move(4, homeRow, 2, homeRow));
        }
    }

    private static boolean areEmpty(Board board, int row, int... columns) {
        for (int col : columns) {
            if (board.getPiece(col, row) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TypePiece getType() {
        return TypePiece.KING;
    }
}
