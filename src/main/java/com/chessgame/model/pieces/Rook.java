package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.List;

public class Rook extends Piece {

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public void addPseudoLegalMoves(Board board, int x, int y, List<Move> out) {
        slide(board, x, y, ORTHOGONAL, out);
    }

    @Override
    public TypePiece getType() {
        return TypePiece.ROOK;
    }
}
