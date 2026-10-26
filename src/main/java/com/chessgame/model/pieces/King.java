package com.chessgame.model.pieces;

import com.chessgame.controller.ChessGameController;
import com.chessgame.model.Board;
import com.chessgame.model.Player;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {


    private final TypePiece typePiece = TypePiece.KING;

    public King(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        this.alreadyMove = false;
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
                    if (board.getPiece(newX, newY).getType() == TypePiece.EMPTY || board.getPiece(newX, newY).isWhite() != isWhite) {
                        validMoves.add(new Move(getX(), getY(), newX, newY));
                    }
                }
            }
        }
        if(!alreadyMove){
            List<Rook> rooks = getRooks(board, isWhite);
            for(Rook rook : rooks){
                if(!rook.getAlreadyMove()){
                    boolean canCastle = true;
                    int nbCase = Math.abs(rook.getX() - getX());
                    if(rook.getX() > getX()){
                        for(int i = getX() + 1; i < rook.getX(); i++){
                            if(board.getPiece(i, getY()).getType() != TypePiece.EMPTY){
                                canCastle = false;
                                break;
                            }
                        }
                        for (int t = getX(); t < getX() + nbCase; t++){
                            if(isCheck(board, t, getY(), isWhite)){
                                canCastle = false;
                                break;
                            }
                        }
                        if(canCastle){
                            validMoves.add(new Move(getX(), getY(), getX() + nbCase -1 , getY(), false));
                        }
                    }else{
                        for(int i = rook.getX() + 1; i < getX(); i++){
                            if(board.getPiece(i, getY()).getType() != TypePiece.EMPTY){
                                canCastle = false;
                                break;
                            }
                        }
                        for (int t = getX(); t > getX() - nbCase; t--){
                            if(isCheck(board, t, getY(), isWhite)){
                                canCastle = false;
                                break;
                            }
                        }
                        if(canCastle){
                            validMoves.add(new Move(getX(), getY(), getX() - nbCase +1 , getY(), false));
                        }
                    }
                }
            }

        }
    }

    public boolean isCheck(Board board, int t, int y, boolean kingIsWhite) {
        List<Piece> opponentPieces = getOpponentPieces(board, kingIsWhite);
        for (Piece piece : opponentPieces) {
            for (Move move : piece.getValidMoves()) {
                if (move.getEndX() == t && move.getEndY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Piece> getOpponentPieces(Board board, boolean kingIsWhite){
        List<Piece> piecesOpponent = new ArrayList<Piece>();
        Piece[][] boardTab = board.getBoard();
        for (int col = 0; col < boardTab.length; col++){
            for(int row = 0; row < boardTab[col].length; row++){
                if(board.getPiece(col,row).getType() != TypePiece.EMPTY && board.getPiece(col,row).isWhite() != kingIsWhite ){
                    piecesOpponent.add(board.getPiece(col,row));
                }
            }
        }
        return piecesOpponent;
    }

    public List<Rook> getRooks(Board board, boolean kingIsWhite){
        Piece[][] boardTab = board.getBoard();
        List<Rook> rooks = new ArrayList<Rook>();
        for (int col = 0; col < boardTab.length; col++){
            for(int row = 0; row < boardTab[col].length; row++){
                if(board.getPiece(col,row).getType() == TypePiece.ROOK && board.getPiece(col,row).isWhite() == kingIsWhite ){
                    rooks.add( (Rook) board.getPiece(col,row));
                }
            }
        }
        return rooks;
    }

    @Override
    public TypePiece getType() {
        return typePiece;
    }
}
