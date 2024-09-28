package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class Rook extends Piece {

    private final TypePiece typePiece = TypePiece.ROOK;
    private Boolean alreadyMove;

    public Rook(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.alreadyMove = false;
    }

    public boolean getAlreadyMove() {
        return this.alreadyMove;
    }

    @Override
    public void findValidMove(Board board){
        //horizontal
        for(int i = 0; i < 8; i++){
            if(this.getX() + i < 8){
                if(board.getPiece(this.getX() + i, this.getY()) == null){
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY()));
                }else if(board.getPiece(this.getX() + i, this.getY()).isWhite() != this.isWhite()){
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY()));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (this.getX() - i >= 0) {
                if (board.getPiece(this.getX() - i, this.getY()) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY()));
                } else if (board.getPiece(this.getX() - i, this.getY()).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY()));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        //vertical
        for (int i = 0; i < 8; i++) {
            if (this.getY() + i < 8) {
                if (board.getPiece(this.getX(), this.getY() + i) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX(), this.getY() + i));
                } else if (board.getPiece(this.getX(), this.getY() + i).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX(), this.getY() + i));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (this.getY() - i >= 0) {
                if (board.getPiece(this.getX(), this.getY() - i) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX(), this.getY() - i));
                } else if (board.getPiece(this.getX(), this.getY() - i).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX(), this.getY() - i));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
