package com.chessgame.model;

import com.chessgame.utils.Fen;
import com.chessgame.utils.Move;
import com.chessgame.utils.Notation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Une partie d'échecs : position courante, historique, et arbitrage des règles
 * (coups légaux, échec et mat, pat, nulles par 50 coups / triple répétition / matériel insuffisant,
 * abandon).
 *
 * Point d'entrée unique pour jouer : {@link #play(Move)} refuse tout coup illégal.
 */
public class ChessGame implements Serializable {

    private static final long serialVersionUID = 3L;
    private static final int FIFTY_MOVE_HALFMOVES = 100;

    private final Board board;
    private final List<String> sanHistory = new ArrayList<>();
    private final Map<String, Integer> repetitions = new HashMap<>();
    private List<Move> legalMoves;
    private boolean check;
    private Move lastMove;
    private GameStatus status = GameStatus.ONGOING;
    private Boolean winnerIsWhite;

    public ChessGame() {
        this(Board.initial());
    }

    /** Partie démarrant d'une position arbitraire (tests, problèmes). */
    public ChessGame(Board start) {
        this.board = start;
        refreshStatus(recordPosition());
    }

    /**
     * Joue un coup s'il est légal et que la partie n'est pas terminée.
     * @return true si le coup a été joué, false s'il est refusé (illégal, partie finie…).
     */
    public synchronized boolean play(Move move) {
        if (status.isOver() || move == null || !legalMoves.contains(move)) {
            return false;
        }
        String san = Notation.toSan(board, move, legalMoves);
        board.apply(move);
        lastMove = move;
        refreshStatus(recordPosition());
        sanHistory.add(san + Notation.checkSuffix(check, status == GameStatus.CHECKMATE));
        return true;
    }

    /** Abandon du camp indiqué : l'adversaire gagne. */
    public synchronized void resign(boolean white) {
        endBy(GameStatus.RESIGNATION, !white);
    }

    /** Un joueur a quitté la partie : l'autre gagne. */
    public synchronized void abandon(boolean white) {
        endBy(GameStatus.ABANDONED, !white);
    }

    private void endBy(GameStatus reason, boolean winnerWhite) {
        if (!status.isOver()) {
            status = reason;
            winnerIsWhite = winnerWhite;
            legalMoves = Collections.emptyList();
        }
    }

    /** Compte la position courante et renvoie son nombre d'occurrences. */
    private int recordPosition() {
        return repetitions.merge(Fen.positionKey(board), 1, Integer::sum);
    }

    private void refreshStatus(int occurrences) {
        legalMoves = board.legalMoves();
        boolean whiteToMove = board.isWhiteToMove();
        check = board.isInCheck(whiteToMove);
        if (legalMoves.isEmpty()) {
            status = check ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
            winnerIsWhite = check ? !whiteToMove : null;
        } else if (board.isInsufficientMaterial()) {
            status = GameStatus.DRAW_INSUFFICIENT_MATERIAL;
        } else if (board.getHalfmoveClock() >= FIFTY_MOVE_HALFMOVES) {
            status = GameStatus.DRAW_FIFTY_MOVES;
        } else if (occurrences >= 3) {
            status = GameStatus.DRAW_REPETITION;
        }
        if (status.isOver()) {
            legalMoves = Collections.emptyList();
        }
    }

    // --- Lecture de l'état ------------------------------------------------------------

    public synchronized List<Move> getLegalMoves() {
        return Collections.unmodifiableList(legalMoves);
    }

    public synchronized List<Move> getLegalMovesFrom(int x, int y) {
        List<Move> from = new ArrayList<>();
        for (Move m : legalMoves) {
            if (m.getStartX() == x && m.getStartY() == y) {
                from.add(m);
            }
        }
        return from;
    }

    public synchronized boolean isWhiteToMove() {
        return board.isWhiteToMove();
    }

    /** Vrai si le camp au trait est en échec (y compris en cas de mat). */
    public synchronized boolean isCheck() {
        return check;
    }

    public synchronized GameStatus getStatus() {
        return status;
    }

    public synchronized boolean isGameOver() {
        return status.isOver();
    }

    /** true = blancs, false = noirs, null = pas de vainqueur (partie en cours ou nulle). */
    public synchronized Boolean getWinnerIsWhite() {
        return winnerIsWhite;
    }

    public synchronized String getFen() {
        return Fen.toFen(board);
    }

    public synchronized List<String> getSanHistory() {
        return Collections.unmodifiableList(new ArrayList<>(sanHistory));
    }

    public synchronized Move getLastMove() {
        return lastMove;
    }

    /** Copie de la position courante (le plateau interne n'est jamais exposé). */
    public synchronized Board getBoard() {
        return board.copy();
    }
}
