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
        System.out.println("in find move pawn");
        validMoves.clear();
        int direction = isWhite() ? 1 : -1;
        int newY = getY() + direction;
        System.out.println(newY);
        if (newY >= 0 && newY < 8) {
            System.out.println(board.getPiece(getX(), newY));
            if (board.getPiece(getX(), newY).getType() == TypePiece.EMPTY) {
                System.out.println("allo");
                validMoves.add(new Move(getX(), getY(), getX(), newY));
                System.out.println("already " + alreadyMove);
                if (!alreadyMove) {
                    newY += direction;
                    if (newY >= 0 && newY < 8 && board.getPiece(getX(), newY).getType() == TypePiece.EMPTY) {
                        validMoves.add(new Move(getX(), getY(), getX(), newY));
                    }
                }
            }
            if (getX() > 0 && board.getPiece(getX() - 1, newY - direction).getType() != TypePiece.EMPTY  && board.getPiece(getX() -1,newY - direction).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), getX()-1, newY - direction));
            }
            if (getX() < 7 && board.getPiece(getX() + 1, newY - direction).getType() != TypePiece.EMPTY && board.getPiece(getX()+ 1, newY - direction).isWhite() != isWhite()) {
                validMoves.add(new Move(getX(), getY(), getX()+ 1, newY - direction));
            }
        }
    }



    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
