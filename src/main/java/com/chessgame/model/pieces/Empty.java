package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.TypePiece;

public class Empty extends Piece{

    private final TypePiece typePiece = TypePiece.EMPTY;

    public Empty(boolean isWhite, int x, int y) {
        super(isWhite,x,y);
    }

    public void findValidMove(Board board){
        return;
    }

    public TypePiece getType(){
        return typePiece;
    }
}
