package com.chessgame.utils;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean pawnDash;
    private static final ParserCoo pc = new ParserCoo();

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pawnDash = false;
    }

    public Move(int startX, int startY, int endX, int endY, boolean pawnDash){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pawnDash = pawnDash;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean getPawnDash() { return pawnDash; }



    public void setPawnDash(boolean pawnDash) { this.pawnDash = pawnDash; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startX == move.startX &&
               startY == move.startY &&
               endX == move.endX &&
               endY == move.endY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY);
    }



    @Override
    public String toString(){
        String res = "";
        res += pc.parseColIntToString(startX)+pc.parseRowIntToString(startY)+":"+pc.parseColIntToString(endX)+pc.parseRowIntToString(endY);
        return res;
    }

}

