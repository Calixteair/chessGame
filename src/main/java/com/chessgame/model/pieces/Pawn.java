package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class Pawn extends Piece {

    private final TypePiece typePiece = TypePiece.PAWN;

    private boolean alreadyMove;

    public Pawn(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.alreadyMove = false;
    }

    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
        int direction = isWhite() ? 1 : -1;
        int newX = getX() + direction;
        if (newX >= 0 && newX < 8) {
            if (board.getPiece(newX, getY()) == null) {
                validMoves.add(new Move(getX(), getY(), newX, getY()));
                if (!alreadyMove) {
                    newX += direction;
                    if (newX >= 0 && newX < 8 && board.getPiece(newX, getY()) == null) {
                        validMoves.add(new Move(getX(), getY(), newX, getY()));
                    }
                }
            }
            if (getY() > 0 && board.getPiece(newX, getY() - 1) != null && board.getPiece(newX, getY() - 1).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), newX, getY() - 1));
            }
            if (getY() < 7 && board.getPiece(newX, getY() + 1) != null && board.getPiece(newX, getY() + 1).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), newX, getY() + 1));
            }
        }
    }



    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
