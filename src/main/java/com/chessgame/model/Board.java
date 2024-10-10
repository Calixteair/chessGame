package com.chessgame.model;

import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.Move;

import java.io.Serializable;

public class Board implements Serializable {

    private Piece[][] board;  // Le plateau d'échecs est une matrice 8x8 de pièces

    private static final int BOARD_SIZE = 8;

    public Move lastMove;  // Dernier mouvement effectué

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];  // Initialise un tableau 8x8
    }

    // Place une pièce sur le plateau à une position donnée
    public void setPiece(Piece piece, int x, int y) {
        if (isWithinBounds(x, y)) {
            board[x][y] = piece;
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
    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Move move = new Move(startX, startY, endX, endY);
        if (isWithinBounds(startX, startY) && isWithinBounds(endX, endY)) {
            Piece piece = getPiece(startX, startY);
            if (piece != null && piece.validMoves.contains(move)) {
                board[endX][endY] = piece;  // Place la pièce à la nouvelle position
                board[startX][startY] = null;  // Vide l'ancienne position
                lastMove = new Move(startX, startY, endX, endY);
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


    public Piece[][] getBoard() {
        return board;
    }
}
