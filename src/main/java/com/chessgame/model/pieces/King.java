package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class King extends Piece {

    private Boolean alreadyMove;

    private final TypePiece typePiece = TypePiece.KING;

    public King(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.alreadyMove = false;
    }

    public boolean getAlreadyMove() {
        return this.alreadyMove;
    }

    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
        int[] directions = {1, 0, -1};
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int newX = getX() + dx;
                int newY = getY() + dy;
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                    if (board.getPiece(newX, newY) == null || board.getPiece(newX, newY).isWhite() != isWhite) {
                        validMoves.add(new Move(getX(), getY(), newX, newY));
                    }
                }
            }
        }
    }

    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
