package com.chessgame.model.pieces;

import com.chessgame.model.Board;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Serializable {


    private int x;
    private int y;
    protected boolean isWhite;
    public List<Move> validMoves = new ArrayList<>();

    public Piece(boolean isWhite, int x, int y) {
        this.isWhite = isWhite;
        this.x = x;
        this.y = y;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract void findValidMove(Board board);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRow() {
        return x;
    }

    public int getColumn() {
        return y;
    }

    public void move(Move move) {
        this.x = move.getEndX();
        this.y = move.getEndY();
    }

    public List<Move> getValidMoves() {
        return validMoves;
    }

    public abstract TypePiece getType();
}
