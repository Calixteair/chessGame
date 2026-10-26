package com.chessgame.controller;

import com.chessgame.model.Board;
import com.chessgame.model.ChessGame;
import com.chessgame.model.Player;
import com.chessgame.model.pieces.Decoy;
import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChessGameController {

    public ChessGame chessGame;
    private Board board;
    private Player currentPlayer;

    public ChessGameController() {
        this.chessGame = new ChessGame();
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
    }

    public ChessGameController(String pseudo1){
        this.chessGame = new ChessGame(pseudo1);
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
    }

        public ChessGameController(String pseudo1, String pseudo2) {
        this.chessGame = new ChessGame(pseudo1, pseudo2);
        this.board = chessGame.getBoard();
        this.currentPlayer = chessGame.getCurrentPlayer();
    }

    //468861530

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
        Piece piece = board.getPiece(move.getStartX(), move.getStartY());
        if(currentPlayer.getPieces().contains(piece) && piece.validMoves.contains(move)){
            if(board.getPiece(move.getEndX(), move.getEndY()).getType() != TypePiece.EMPTY){
                Player opponent = chessGame.getOpponent();
                opponent.getPieces().remove(board.getPiece(move.getEndX(), move.getEndY()));
                if (board.getPiece(move.getEndX(), move.getEndY()).getType() == TypePiece.DECOY){
                    Decoy decoy = (Decoy) board.getPiece(move.getEndX(), move.getEndY());
                    opponent.getPieces().remove(board.getPiece(decoy.getxPawn(),decoy.getyPawn()));
                }
            }
            if(piece.getType() == TypePiece.KING && Math.abs(move.getStartX() - move.getEndX()) > 1){
                if (move.getEndX() == 1){
                    Move moveRook = new Move(0, move.getStartY(), 2, move.getStartY(),false);
                    fakeMoove(moveRook);
                }
                if (move.getEndX() == 6){
                    Move moveRook = new Move(7,move.getStartY(), 5, move.getStartY() , false);
                    fakeMoove(moveRook);
                }
            }
            if(move.getPawnDash()){
                System.out.println(move);
                int xDecoy = move.getEndX();
                int yDecoy = move.getEndY() > move.getStartY() ?  move.getEndY() -1 : move.getEndY() + 1;
                Decoy decoy = new Decoy(currentPlayer.isWhite(), xDecoy, yDecoy, move.getEndX(), move.getEndY());
                currentPlayer.addPiece(decoy);
                board.setPiece(decoy, xDecoy, yDecoy);
            }
            if (!piece.getAlreadyMove()){
                piece.setAlreadyMove(true);
            }
            currentPlayer.movePiece(move);
            board.getMoveHistory().add(move);
            checkGameOver();
            switchPlayer();
            return true;

        }
        return false;
    }

    public void fakeMoove(Move move){
        currentPlayer.movePiece(move);
    }

    public void switchPlayer() {
        chessGame.switchPlayer();
        currentPlayer = chessGame.getCurrentPlayer();
        System.out.println("SWITCH PLAYER kjfldsqjfkjfkljdsqkflkdsjfksqjfkqjsdkfjqksdfjqslkfqs");
        currentPlayer.decoyCheck();
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
