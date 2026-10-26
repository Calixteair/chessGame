package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.List;

public class Knight extends Piece {

    public static final int[][] JUMPS = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public void addPseudoLegalMoves(Board board, int x, int y, List<Move> out) {
        step(board, x, y, JUMPS, out);
    }

    @Override
    public TypePiece getType() {
        return TypePiece.KNIGHT;
    }
}
