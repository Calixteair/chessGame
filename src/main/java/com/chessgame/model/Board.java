package com.chessgame.model;

import com.chessgame.model.pieces.Empty;
import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.Move;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    private Piece[][] board;  // Le plateau d'échecs est une matrice 8x8 de pièces

    private static final int BOARD_SIZE = 8;

    public Move lastMove;  // Dernier mouvement effectué

    public List<Move> moveHistory = new ArrayList<>();

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];  // Initialise un tableau 8x8
    }

    // Place une pièce sur le plateau à une position donnée
    public void setPiece(Piece piece, int x, int y) {
        if (isWithinBounds(x, y)) {
            board[x][y] = piece;
        }
    }

    public void clearBoard(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++){
                board[i][j] = new Empty(true,i, j);
            }
        }
    }

    // Récupère la pièce à une position donnée
    public Piece getPiece(int x, int y) {
        if (isWithinBounds(x, y)) {
            return board[x][y];
        }
        return null;  // Si hors des limites, retourne null
    }

    // Déplace une pièce d'une position à une autre (si le mouvement est valide)
    public boolean movePiece(Move move) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        System.out.println("startX : " + startX + " startY : " + startY + " endX : " + endX + " endY : " + endY);
        if (isWithinBounds(startX,startY) && isWithinBounds(endX,endY)) {
            Piece piece = getPiece(startX, startY);
            System.out.println(piece.validMoves.contains(move));
            if (piece != null && piece.validMoves.contains(move)) {
                System.out.println("Piece : " + piece);
                board[endX][endY] = piece;  // Place la pièce à la nouvelle position
                board[startX][startY] = new Empty(true,startX,startY);  // Supprime la pièce de l'ancienne position
                lastMove = move;
                return true;
            }
        }
        return false;  // Si le mouvement est invalide ou hors des limites
    }

    // Vérifie si une position est dans les limites du plateau
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    // Vérifie s'il y a une pièce ennemie à une position donnée
    public boolean isEnemyPiece(int x, int y, boolean isWhite) {
        Piece piece = getPiece(x, y);
        return piece != null && piece.isWhite() != isWhite;
    }

    // Retourne vrai si la position donnée est vide
    public boolean isEmpty(int x, int y) {
        return getPiece(x, y) == null;
    }

    public void undoMove() {
        if (lastMove != null) {
            Piece piece = getPiece(lastMove.getEndX(), lastMove.getEndY());
            board[lastMove.getStartX()][lastMove.getStartY()] = piece;
            board[lastMove.getEndX()][lastMove.getEndY()] = null;
            lastMove = null;
        }
    }

    public List<Move> getMoveHistory(){ return moveHistory; }


    public Piece[][] getBoard() {
        return board;
    }
}
