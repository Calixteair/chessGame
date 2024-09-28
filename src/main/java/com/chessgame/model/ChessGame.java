package com.chessgame.model;

import com.chessgame.model.pieces.*;
import com.chessgame.utils.Move;

import java.util.List;

public class ChessGame {

    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private boolean gameOver;

    public ChessGame() {
        initializeGame();
    }

    // Initialise le plateau et les pièces
    private void initializeGame() {
        board = new Board();
        whitePlayer = new Player(true, board, "White");
        blackPlayer = new Player(false, board, "Black");
        currentPlayer = whitePlayer;
        gameOver = false;

        putPieceOnBoard();
        givePieceToPlayers();
    }

    public void putPieceOnBoard(){
        for (int i = 0; i < 8; i++) {
            board.setPiece(new Pawn(true,i,1),i, 1 );
            board.setPiece( new Pawn(false,i, 6),i, 6);
        }
        board.setPiece(new Queen(true, 3,0), 3, 0);
        board.setPiece(new King(true,4,0), 4, 0);
        board.setPiece(new Rook(true,0,0), 0, 0);
        board.setPiece(new Rook(true,7,0), 7, 0);
        board.setPiece(new Knight(true,1,0), 1, 0);
        board.setPiece(new Knight(true,6,0), 6, 0);
        board.setPiece(new Bishop(true,2,0), 2, 0);
        board.setPiece(new Bishop(true,5,0), 5, 0);

        board.setPiece(new Queen(false,3,7), 3, 7);
        board.setPiece(new King(false,4,7), 4, 7);
        board.setPiece(new Rook(false,0,7), 0, 7);
        board.setPiece(new Rook(false,7,7), 7, 7);
        board.setPiece(new Knight(false,1,7), 1, 7);
        board.setPiece(new Knight(false,6,7), 6, 7);
        board.setPiece(new Bishop(false,2,7), 2, 7);
        board.setPiece(new Bishop(false,5,7), 5, 7);
    }

    public void givePieceToPlayers(){
        for(int i = 0; i < 8; i++){
            int t = 7;
            for(int j = 0; j < 2; j++){
                whitePlayer.addPiece(board.getPiece(i, j));
                blackPlayer.addPiece(board.getPiece(7-i, t-j));
            }
        }
    }

    // Retourne le joueur actuel (blanc ou noir)
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Alterner les tours
    public void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public boolean isKingInCheck(boolean isWhite) {
        Player opponent = isWhite ? getBlackPlayer() : getWhitePlayer();

        List<Piece> pieces = opponent.getPieces();

        for(Piece piece : pieces){
            // Vérifie si le roi est sur une destionation possible qui est dans validMoves de la piece
            for (Move move : piece.getValidMoves()) {
                if(move.getEndX() == currentPlayer.getKing().getX() && move.getEndY() == currentPlayer.getKing().getY()){
                    return true;
                }
            }
        }
        return false;
    }




    // Détermine si le joueur actuel est en échec et mat
    public boolean isCheckMate() {
        if(currentPlayer.isInCheck()){
            for (Piece piece : currentPlayer.getPieces()) {
                if (!piece.getValidMoves().isEmpty()) {
                    return false;
                }
            }
        }
        return false;
    }

    // Détermine si la partie est nulle (stalemate)
    public boolean isStaleMate() {
        for (Piece piece : currentPlayer.getPieces()) {
            if (!piece.getValidMoves().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Retourne l'état du jeu (fini ou non)
    public boolean isGameOver() {
        return gameOver;
    }

    public Board getBoard() {
        return board;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public void setGameOver(boolean b) {
        gameOver = b;
    }


}
