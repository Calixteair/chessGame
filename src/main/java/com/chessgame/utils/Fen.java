package com.chessgame.utils;

import com.chessgame.model.Board;
import com.chessgame.model.pieces.Piece;

/**
 * Lecture / écriture de la notation FEN (Forsyth-Edwards), format standard d'échange de positions.
 * Exemple (départ) : rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
 */
public final class Fen {

    public static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private Fen() {
    }

    public static String toFen(Board board) {
        return placementAndRights(board) + " " + enPassantField(board, board.hasEnPassantSquare())
                + " " + board.getHalfmoveClock() + " " + board.getFullmoveNumber();
    }

    /**
     * Clé d'identité d'une position pour la règle de triple répétition : placement, trait, droits
     * de roque, et case en passant seulement si une prise en passant est réellement possible.
     */
    public static String positionKey(Board board) {
        return placementAndRights(board) + " " + enPassantField(board, board.hasEnPassantCapture());
    }

    private static String enPassantField(Board board, boolean shown) {
        return shown ? Move.squareName(board.getEnPassantX(), board.getEnPassantY()) : "-";
    }

    private static String placementAndRights(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < Board.SIZE; y++) {
            int empty = 0;
            for (int x = 0; x < Board.SIZE; x++) {
                Piece p = board.getPiece(x, y);
                if (p == null) {
                    empty++;
                    continue;
                }
                if (empty > 0) {
                    sb.append(empty);
                    empty = 0;
                }
                sb.append(p.getFenSymbol());
            }
            if (empty > 0) {
                sb.append(empty);
            }
            if (y < Board.SIZE - 1) {
                sb.append('/');
            }
        }
        sb.append(board.isWhiteToMove() ? " w " : " b ");
        sb.append(castlingField(board));
        return sb.toString();
    }

    private static String castlingField(Board board) {
        String rights = (board.canCastle(true, true) ? "K" : "")
                + (board.canCastle(true, false) ? "Q" : "")
                + (board.canCastle(false, true) ? "k" : "")
                + (board.canCastle(false, false) ? "q" : "");
        return rights.isEmpty() ? "-" : rights;
    }

    /**
     * Construit un plateau depuis une FEN.
     * @throws IllegalArgumentException si la FEN est mal formée.
     */
    public static Board fromFen(String fen) {
        String[] fields = fen.trim().split("\\s+");
        if (fields.length < 4) {
            throw new IllegalArgumentException("FEN incomplète : " + fen);
        }
        Board board = new Board();
        readPlacement(board, fields[0]);
        board.setWhiteToMove("w".equals(fields[1]));
        board.setCastle(true, true, fields[2].contains("K"));
        board.setCastle(true, false, fields[2].contains("Q"));
        board.setCastle(false, true, fields[2].contains("k"));
        board.setCastle(false, false, fields[2].contains("q"));
        if (!"-".equals(fields[3])) {
            int[] ep = Move.parseSquare(fields[3]);
            board.setEnPassant(ep[0], ep[1]);
        }
        board.setHalfmoveClock(fields.length > 4 ? Integer.parseInt(fields[4]) : 0);
        board.setFullmoveNumber(fields.length > 5 ? Integer.parseInt(fields[5]) : 1);
        return board;
    }

    private static void readPlacement(Board board, String placement) {
        String[] rows = placement.split("/");
        if (rows.length != Board.SIZE) {
            throw new IllegalArgumentException("FEN : 8 rangées attendues : " + placement);
        }
        for (int y = 0; y < Board.SIZE; y++) {
            int x = 0;
            for (char c : rows[y].toCharArray()) {
                if (Character.isDigit(c)) {
                    x += c - '0';
                } else {
                    board.setPiece(Piece.create(TypePiece.fromSymbol(c), Character.isUpperCase(c)), x++, y);
                }
            }
            if (x != Board.SIZE) {
                throw new IllegalArgumentException("FEN : rangée invalide : " + rows[y]);
            }
        }
    }
}
