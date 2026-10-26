package com.chessgame.model;

import com.chessgame.model.pieces.Knight;
import com.chessgame.model.pieces.Pawn;
import com.chessgame.model.pieces.Piece;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Position complète : placement des pièces, trait, droits de roque, case de prise en passant
 * et compteurs (règle des 50 coups, numéro de coup). Équivalent d'une FEN.
 *
 * Repère : x = colonne (0 = a), y = ligne affichée (0 = rangée 8, 7 = rangée 1).
 */
public class Board implements Serializable {

    private static final long serialVersionUID = 2L;
    public static final int SIZE = 8;

    private static final int WHITE_KINGSIDE = 0;
    private static final int WHITE_QUEENSIDE = 1;
    private static final int BLACK_KINGSIDE = 2;
    private static final int BLACK_QUEENSIDE = 3;
    private static final TypePiece[] BACK_RANK = {
            TypePiece.ROOK, TypePiece.KNIGHT, TypePiece.BISHOP, TypePiece.QUEEN,
            TypePiece.KING, TypePiece.BISHOP, TypePiece.KNIGHT, TypePiece.ROOK};

    private final Piece[][] squares = new Piece[SIZE][SIZE];
    private final boolean[] castling = new boolean[4];
    private boolean whiteToMove = true;
    private int enPassantX = -1;
    private int enPassantY = -1;
    private int halfmoveClock = 0;
    private int fullmoveNumber = 1;

    /** Plateau vide, trait aux blancs, aucun droit de roque. */
    public Board() {
    }

    /** Position de départ standard. */
    public static Board initial() {
        Board board = new Board();
        for (int x = 0; x < SIZE; x++) {
            board.setPiece(Piece.create(BACK_RANK[x], false), x, 0);
            board.setPiece(new Pawn(false), x, 1);
            board.setPiece(new Pawn(true), x, 6);
            board.setPiece(Piece.create(BACK_RANK[x], true), x, 7);
        }
        Arrays.fill(board.castling, true);
        return board;
    }

    public Board copy() {
        Board c = new Board();
        for (int x = 0; x < SIZE; x++) {
            System.arraycopy(squares[x], 0, c.squares[x], 0, SIZE);
        }
        System.arraycopy(castling, 0, c.castling, 0, castling.length);
        c.whiteToMove = whiteToMove;
        c.enPassantX = enPassantX;
        c.enPassantY = enPassantY;
        c.halfmoveClock = halfmoveClock;
        c.fullmoveNumber = fullmoveNumber;
        return c;
    }

    public static boolean isInside(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    /** Pièce en (x, y), ou null si la case est vide ou hors plateau. */
    public Piece getPiece(int x, int y) {
        return isInside(x, y) ? squares[x][y] : null;
    }

    public void setPiece(Piece piece, int x, int y) {
        if (!isInside(x, y)) {
            throw new IllegalArgumentException("Case hors plateau : " + x + "," + y);
        }
        squares[x][y] = piece;
    }

    // --- État de la partie -------------------------------------------------------------

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public void setWhiteToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
    }

    public boolean canCastle(boolean white, boolean kingside) {
        return castling[castlingIndex(white, kingside)];
    }

    public void setCastle(boolean white, boolean kingside, boolean allowed) {
        castling[castlingIndex(white, kingside)] = allowed;
    }

    private static int castlingIndex(boolean white, boolean kingside) {
        if (white) {
            return kingside ? WHITE_KINGSIDE : WHITE_QUEENSIDE;
        }
        return kingside ? BLACK_KINGSIDE : BLACK_QUEENSIDE;
    }

    public boolean isEnPassantSquare(int x, int y) {
        return x == enPassantX && y == enPassantY;
    }

    public boolean hasEnPassantSquare() {
        return enPassantX >= 0;
    }

    public int getEnPassantX() {
        return enPassantX;
    }

    public int getEnPassantY() {
        return enPassantY;
    }

    public void setEnPassant(int x, int y) {
        enPassantX = x;
        enPassantY = y;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }

    // --- Attaques et échec -------------------------------------------------------------

    /** Vrai si la case (x, y) est attaquée par au moins une pièce de la couleur {@code byWhite}. */
    public boolean isAttacked(int x, int y, boolean byWhite) {
        int pawnRow = y - Pawn.direction(byWhite);
        if (isPieceAt(x - 1, pawnRow, TypePiece.PAWN, byWhite) || isPieceAt(x + 1, pawnRow, TypePiece.PAWN, byWhite)) {
            return true;
        }
        for (int[] j : Knight.JUMPS) {
            if (isPieceAt(x + j[0], y + j[1], TypePiece.KNIGHT, byWhite)) {
                return true;
            }
        }
        for (int[] d : Piece.ALL_DIRECTIONS) {
            if (isPieceAt(x + d[0], y + d[1], TypePiece.KING, byWhite)) {
                return true;
            }
        }
        return isAttackedOnRays(x, y, byWhite, Piece.ORTHOGONAL, TypePiece.ROOK)
                || isAttackedOnRays(x, y, byWhite, Piece.DIAGONAL, TypePiece.BISHOP);
    }

    /** Cherche, le long de chaque rayon, la première pièce rencontrée : {@code slider} ou dame ennemie. */
    private boolean isAttackedOnRays(int x, int y, boolean byWhite, int[][] directions, TypePiece slider) {
        for (int[] d : directions) {
            int nx = x + d[0];
            int ny = y + d[1];
            while (isInside(nx, ny)) {
                Piece p = squares[nx][ny];
                if (p != null) {
                    if (p.isWhite() == byWhite && (p.getType() == slider || p.getType() == TypePiece.QUEEN)) {
                        return true;
                    }
                    break;
                }
                nx += d[0];
                ny += d[1];
            }
        }
        return false;
    }

    private boolean isPieceAt(int x, int y, TypePiece type, boolean white) {
        Piece p = getPiece(x, y);
        return p != null && p.getType() == type && p.isWhite() == white;
    }

    /** Position {x, y} du roi de la couleur donnée, ou null s'il n'est pas sur le plateau. */
    public int[] findKing(boolean white) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (isPieceAt(x, y, TypePiece.KING, white)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    public boolean isInCheck(boolean white) {
        int[] king = findKing(white);
        return king != null && isAttacked(king[0], king[1], !white);
    }

    // --- Génération des coups ----------------------------------------------------------

    /** Coups du camp au trait, sans vérifier si le roi reste en échec. */
    public List<Move> pseudoLegalMoves() {
        List<Move> moves = new ArrayList<>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = squares[x][y];
                if (p != null && p.isWhite() == whiteToMove) {
                    p.addPseudoLegalMoves(this, x, y, moves);
                }
            }
        }
        return moves;
    }

    /** Coups légaux du camp au trait : ceux qui ne laissent pas son propre roi en échec. */
    public List<Move> legalMoves() {
        List<Move> legal = new ArrayList<>();
        for (Move move : pseudoLegalMoves()) {
            Board next = copy();
            next.apply(move);
            if (!next.isInCheck(whiteToMove)) {
                legal.add(move);
            }
        }
        return legal;
    }

    /**
     * Joue un coup supposé légal et met à jour tout l'état (prise en passant, roque, promotion,
     * droits de roque, compteurs, trait). Utiliser {@link ChessGame#play(Move)} pour un coup non vérifié.
     */
    public void apply(Move move) {
        int sx = move.getStartX();
        int sy = move.getStartY();
        int ex = move.getEndX();
        int ey = move.getEndY();
        Piece piece = squares[sx][sy];
        if (piece == null) {
            throw new IllegalStateException("Aucune pièce en " + Move.squareName(sx, sy));
        }
        boolean capture = squares[ex][ey] != null;
        boolean isPawn = piece.getType() == TypePiece.PAWN;

        if (isPawn && isEnPassantSquare(ex, ey) && squares[ex][ey] == null) {
            squares[ex][sy] = null;
            capture = true;
        }
        if (piece.getType() == TypePiece.KING && Math.abs(ex - sx) == 2) {
            int rookFrom = ex > sx ? 7 : 0;
            int rookTo = ex > sx ? 5 : 3;
            squares[rookTo][sy] = squares[rookFrom][sy];
            squares[rookFrom][sy] = null;
        }

        squares[sx][sy] = null;
        squares[ex][ey] = move.getPromotion() != null ? Piece.create(move.getPromotion(), piece.isWhite()) : piece;

        updateCastlingRights(piece, sx, sy, ex, ey);
        if (isPawn && Math.abs(ey - sy) == 2) {
            setEnPassant(sx, (sy + ey) / 2);
        } else {
            setEnPassant(-1, -1);
        }
        halfmoveClock = (isPawn || capture) ? 0 : halfmoveClock + 1;
        if (!whiteToMove) {
            fullmoveNumber++;
        }
        whiteToMove = !whiteToMove;
    }

    private void updateCastlingRights(Piece piece, int sx, int sy, int ex, int ey) {
        if (piece.getType() == TypePiece.KING) {
            setCastle(piece.isWhite(), true, false);
            setCastle(piece.isWhite(), false, false);
        }
        // Une tour qui part de son coin, ou qui y est prise, supprime le droit correspondant.
        revokeIfCorner(sx, sy);
        revokeIfCorner(ex, ey);
    }

    private void revokeIfCorner(int x, int y) {
        if (y != 0 && y != 7 || x != 0 && x != 7) {
            return;
        }
        setCastle(y == 7, x == 7, false);
    }

    /** Vrai si le camp au trait peut réellement prendre en passant (utile pour la répétition). */
    public boolean hasEnPassantCapture() {
        if (!hasEnPassantSquare()) {
            return false;
        }
        int pawnRow = enPassantY - Pawn.direction(whiteToMove);
        return isPieceAt(enPassantX - 1, pawnRow, TypePiece.PAWN, whiteToMove)
                || isPieceAt(enPassantX + 1, pawnRow, TypePiece.PAWN, whiteToMove);
    }

    /**
     * Matériel insuffisant pour mater : roi seul, roi + une pièce mineure,
     * ou uniquement des fous tous sur des cases de même couleur.
     */
    public boolean isInsufficientMaterial() {
        int minors = 0;
        int bishopsOnLight = 0;
        int bishopsOnDark = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = squares[x][y];
                if (p == null || p.getType() == TypePiece.KING) {
                    continue;
                }
                switch (p.getType()) {
                    case KNIGHT:
                        minors++;
                        break;
                    case BISHOP:
                        minors++;
                        if ((x + y) % 2 == 0) bishopsOnLight++; else bishopsOnDark++;
                        break;
                    default:
                        return false;
                }
            }
        }
        if (minors <= 1) {
            return true;
        }
        boolean onlyBishops = bishopsOnLight + bishopsOnDark == minors;
        return onlyBishops && (bishopsOnLight == 0 || bishopsOnDark == 0);
    }
}
