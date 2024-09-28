package com.chessgame.model;

import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.TypePiece;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private boolean isWhite; // True si le joueur joue les pièces blanches, false sinon
    private List<Piece> pieces; // Liste des pièces contrôlées par le joueur
    private Board board; // Le plateau sur lequel le joueur joue
    private String name;
    public Boolean isCheck;


    public Player(boolean isWhite, Board board, String name) {
        this.isWhite = isWhite;
        this.board = board;
        this.pieces = new ArrayList<>();
        this.name = name;
        this.isCheck = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Récupère si le joueur est blanc
    public boolean isWhite() {
        return isWhite;
    }

    // Ajoute une pièce à la liste des pièces contrôlées par le joueur
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    // Récupère la liste des pièces du joueur
    public List<Piece> getPieces() {
        return pieces;
    }

    // Déplace une pièce contrôlée par le joueur
    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);

        // Vérifie que la pièce appartient bien au joueur
        if (piece != null && piece.isWhite() == this.isWhite) {
            return board.movePiece(startX, startY, endX, endY);  // Tente de déplacer la pièce sur le plateau
        }
        return false;  // Si le joueur ne contrôle pas la pièce ou le mouvement est invalide
    }

    // Vérifie si le joueur est en échec (à implémenter)
    public boolean isInCheck() {
        // Cette méthode peut vérifier si le roi du joueur est en échec
        return false;  // Logique à implémenter
    }

    public Piece getKing() {
        for (Piece piece : pieces) {
            if (piece.getType().equals(TypePiece.KING)) {
                return piece;
            }
        }
        return null;  // Retourne null si le roi n'est pas trouvé
    }

    public void getValidMoves() {
        for (Piece piece : pieces) {
            piece.findValidMove(board);
        }
    }
}
