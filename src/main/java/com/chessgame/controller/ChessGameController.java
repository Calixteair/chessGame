package com.chessgame.controller;

import com.chessgame.model.Board;
import com.chessgame.model.ChessGame;
import com.chessgame.model.Player;
import com.chessgame.model.pieces.King;
import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.Move;

import java.util.ArrayList;
import java.util.List;

public class ChessGameController {

    private ChessGame chessGame;
    private Board board;
    private Player currentPlayer;
    private List<int[]> possibleMoves; // Liste des mouvements possibles pour une pièce sélectionnée

    public ChessGameController() {
        this.chessGame = new ChessGame();
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
        this.possibleMoves = new ArrayList<>();
    }

    /**
     * Sélectionne une pièce et calcule ses mouvements possibles.
     * @param x Position en X de la pièce.
     * @param y Position en Y de la pièce.
     * @return Liste des positions possibles sous forme de paires [x, y].
     */
    public List<int[]> selectPiece(int x, int y) {
        Piece piece = board.getPiece(x, y);
        possibleMoves.clear();

        if (piece != null && piece.isWhite() == currentPlayer.isWhite()) {
            // Parcourir tout le plateau pour trouver les mouvements valides

        }
        return new ArrayList<>(possibleMoves); // Retourne une copie de la liste
    }

    /**
     * Tente de déplacer une pièce.
     * @param startX Position de départ en X.
     * @param startY Position de départ en Y.
     * @param endX Position d'arrivée en X.
     * @param endY Position d'arrivée en Y.
     * @return Message de résultat du mouvement.
     */
    public boolean movePiece(int startX, int startY, int endX, int endY) {
        // Vérifie si la destination est dans la liste des mouvements possibles
        boolean isValidDestination = false;
        for (int[] move : possibleMoves) {
            if (move[0] == endX && move[1] == endY) {
                isValidDestination = true;
                break;
            }
        }

        if (!isValidDestination) {
            return false;
        }

        // Tente de déplacer la pièce
        boolean moveSuccess = board.movePiece(startX, startY, endX, endY);

        if (moveSuccess) {
            // Vérifie si le roi est en échec après le mouvement
            if (chessGame.isKingInCheck(currentPlayer.isWhite())) {
                // Annule le mouvement si le roi est en échec
                board.undoMove(); // Assure-toi que la méthode undoMove est implémentée
                return false;
            } else {
                // Change de joueur si le mouvement est valide
                Piece piece = board.getPiece(endX, endY);
                piece.findValidMove(board);
                chessGame.switchPlayer();
                currentPlayer = chessGame.getCurrentPlayer();
                return true;
            }
        } else {
            return false;
        }
    }



    public boolean isGameOver() {
        return chessGame.isGameOver();
    }


    // Vérifie si le jeu est terminé
    private void checkGameOver() {
        if (chessGame.isCheckMate()) {
            chessGame.setGameOver(true);
        } else if (chessGame.isStaleMate()) {
            chessGame.setGameOver(true);
        }

    }


    /**
     * Retourne le joueur actuel.
     * @return Le joueur qui doit jouer.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retourne le plateau de jeu.
     * @return Le plateau de jeu.
     */
    public Board getBoard() {
        return board;
    }


    //TODO: roque et promotion de pion
}
