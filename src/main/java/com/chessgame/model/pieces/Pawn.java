package com.chessgame.model.pieces;

import com.chessgame.controller.ChessGameController;
import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class Pawn extends Piece {

    private final TypePiece typePiece = TypePiece.PAWN;


    public Pawn(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
        int direction = isWhite() ? -1 : 1;
        int newY = getY() + direction;
        if (newY >= 0 && newY < 8) {
            if (board.getPiece(getX(), newY).getType() == TypePiece.EMPTY) {
                validMoves.add(new Move(getX(), getY(), getX(), newY));
                if (!this.alreadyMove) {
                    if (newY >= 0 && newY < 8 && board.getPiece(getX(), newY + direction).getType() == TypePiece.EMPTY) {
                        validMoves.add(new Move(getX(), getY(), getX(), newY + direction, true));
                    }
                }
            }
            if (getX() > 0 && board.getPiece(getX() - 1, newY ).getType() != TypePiece.EMPTY  && board.getPiece(getX() -1,newY ).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), getX()-1, newY));
            }
            if (getX() < 7 && board.getPiece(getX() + 1, newY ).getType() != TypePiece.EMPTY && board.getPiece(getX()+ 1, newY ).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), getX()+ 1, newY));
            }
        }
    }



    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
