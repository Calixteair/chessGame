package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.TypePiece;

public class Decoy extends Pawn{


    private final TypePiece typePiece = TypePiece.DECOY;

    private int xPawn;

    private int yPawn;

    private int tour;


    public Decoy(boolean isWhite, int x, int y, int xPawn, int yPawn) {
        super(isWhite, x, y);
        this.xPawn = xPawn;
        this.yPawn = yPawn;
        this.tour = 0;
    }

    @Override
    public void findValidMove(Board board) {
        validMoves.clear();
    }


    public int getTour(){ return tour; }

    public void addTour(){ tour++;}

    public int getxPawn(){ return xPawn;}

    public int getyPawn(){ return yPawn;}


    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
