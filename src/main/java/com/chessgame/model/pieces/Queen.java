package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

public class Queen extends Piece {

    private final TypePiece typePiece = TypePiece.QUEEN;

    public Queen(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
        //diagonale 1
        for(int i = 0; i < 8; i++){
            if(this.getX() + i < 8 && this.getY() + i < 8){
                if(board.getPiece(this.getX() + i, this.getY() + i) == null){
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY() + i));
                }else if(board.getPiece(this.getX() + i, this.getY() + i).isWhite() != this.isWhite()){
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY() + i));
                    break;
                }else{
                    break;
                }
            }else{
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (this.getX() - i >= 0 && this.getY() - i >= 0) {
                if (board.getPiece(this.getX() - i, this.getY() - i) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY() - i));
                } else if (board.getPiece(this.getX() - i, this.getY() - i).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY() - i));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        //diagonale 2
        for (int i = 0; i < 8; i++) {
            if (this.getX() + i < 8 && this.getY() - i >= 0) {
                if (board.getPiece(this.getX() + i, this.getY() - i) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY() - i));
                } else if (board.getPiece(this.getX() + i, this.getY() - i).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY() - i));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (this.getX() - i >= 0 && this.getY() + i < 8) {
                if (board.getPiece(this.getX() - i, this.getY() + i) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY() + i));
                } else if (board.getPiece(this.getX() - i, this.getY() + i).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() - i, this.getY() + i));
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        //horizontal
        for (int i = 0; i < 8; i++) {
            if (this.getX() + i < 8) {
                if (board.getPiece(this.getX() + i, this.getY()) == null) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY()));
                } else if (board.getPiece(this.getX() + i, this.getY()).isWhite() != this.isWhite()) {
                    validMoves.add(new Move(this.getX(), this.getY(), this.getX() + i, this.getY()));
                    break;
                } else {
                    break;
                }
            } else {
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
