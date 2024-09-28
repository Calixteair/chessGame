package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class Knight extends Piece {

    private final TypePiece typePiece = TypePiece.KNIGHT;

    public Knight(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }


    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
        int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};
        for (int i = 0; i < 8; i++) {
            int newX = this.getX() + dx[i];
            int newY = this.getY() + dy[i];
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                if (board.getPiece(newX, newY) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), newX, newY));
                } else if (board.getPiece(newX, newY).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), newX, newY));
                }
            }
        }
    }

    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
