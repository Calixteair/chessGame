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

    public ChessGame chessGame;
    private Board board;
    private Player currentPlayer;
    public List<Move> moveHistory = new ArrayList<>();

    public ChessGameController() {
        this.chessGame = new ChessGame();
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
    }

        public ChessGameController(String pseudo1, String pseudo2) {
        this.chessGame = new ChessGame(pseudo1, pseudo2);
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
    }

    /**
     * Sélectionne une pièce et calcule ses mouvements possibles.
     * @param x Position en X de la pièce.
     * @param y Position en Y de la pièce.
     * @return Liste des positions possibles sous forme de paires [x, y].
     */
    public List<Move> selectPiece(int x, int y) {
        Piece piece = board.getPiece(x, y);


        if (piece != null && piece.isWhite() == currentPlayer.isWhite()) {
            return piece.validMoves;
        }
        return new ArrayList<>();
    }

    public boolean movePiece(Move move) {
        System.out.println("Move piece");
        System.out.println(move.getStartX() + " " + move.getStartY() + " " + move.getEndX() + " " + move.getEndY());
        // Vérifie si la destination est dans la liste des mouvements possibles
        Piece piece = board.getPiece(move.getStartX(), move.getStartY());
        if(currentPlayer.getPieces().contains(piece) && piece.validMoves.contains(move)){
            currentPlayer.movePiece(move);
            moveHistory.add(move);
            checkGameOver();
            switchPlayer();
            return true;

        }
        return false;
    }

    public void switchPlayer() {
        chessGame.switchPlayer();
        currentPlayer = chessGame.getCurrentPlayer();
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
