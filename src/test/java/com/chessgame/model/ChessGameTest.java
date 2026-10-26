package com.chessgame.model;

import com.chessgame.utils.Fen;
import com.chessgame.utils.Move;
import com.chessgame.utils.TypePiece;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest {

    /** Joue une suite de coups UCI et échoue si l'un d'eux est refusé. */
    private static ChessGame playAll(ChessGame game, String... uciMoves) {
        for (String uci : uciMoves) {
            assertTrue(game.play(Move.fromUci(uci)), "coup refusé : " + uci);
        }
        return game;
    }

    private static ChessGame fromFen(String fen) {
        return new ChessGame(Fen.fromFen(fen));
    }

    @Test
    void startPositionHasTwentyMovesAndWhiteToMove() {
        ChessGame game = new ChessGame();
        assertEquals(Fen.START_POSITION, game.getFen());
        assertEquals(20, game.getLegalMoves().size());
        assertTrue(game.isWhiteToMove());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    void foolsMateIsCheckmateWonByBlack() {
        ChessGame game = playAll(new ChessGame(), "f2f3", "e7e5", "g2g4", "d8h4");
        assertEquals(GameStatus.CHECKMATE, game.getStatus());
        assertEquals(Boolean.FALSE, game.getWinnerIsWhite());
        assertEquals(List.of("f3", "e5", "g4", "Qh4#"), game.getSanHistory());
        assertFalse(game.play(Move.fromUci("a2a3")), "aucun coup après le mat");
    }

    @Test
    void scholarsMateIsCheckmateWonByWhite() {
        ChessGame game = playAll(new ChessGame(), "e2e4", "e7e5", "f1c4", "b8c6", "d1h5", "g8f6", "h5f7");
        assertEquals(GameStatus.CHECKMATE, game.getStatus());
        assertEquals(Boolean.TRUE, game.getWinnerIsWhite());
        assertEquals("Qxf7#", game.getSanHistory().get(6));
    }

    @Test
    void moveLeavingOwnKingInCheckIsRejected() {
        // Le cavalier e2 est cloué par la tour e8 sur le roi e1.
        ChessGame game = fromFen("4r2k/8/8/8/8/8/4N3/4K3 w - - 0 1");
        assertFalse(game.play(Move.fromUci("e2c3")));
        assertTrue(game.getLegalMovesFrom(4, 6).isEmpty());
    }

    @Test
    void kingMustEscapeCheck() {
        ChessGame game = fromFen("4k3/8/8/8/8/8/3q4/4K3 w - - 0 1");
        assertTrue(game.isCheck());
        // Fuites : prendre la dame en d2 ou aller en f1 (d1, e2, f2 sont contrôlées).
        assertEquals(Set.of(Move.fromUci("e1d2"), Move.fromUci("e1f1")), Set.copyOf(game.getLegalMoves()));
    }

    @Test
    void stalemateIsDraw() {
        ChessGame game = fromFen("7k/5Q2/6K1/8/8/8/8/8 b - - 0 1");
        assertEquals(GameStatus.STALEMATE, game.getStatus());
        assertTrue(game.getStatus().isDraw());
        assertNull(game.getWinnerIsWhite());
    }

    @Test
    void kingsideAndQueensideCastlingMoveTheRook() {
        ChessGame game = fromFen("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        playAll(game, "e1g1", "e8c8");
        assertEquals("2kr3r/8/8/8/8/8/8/R4RK1 w - - 2 2", game.getFen());
        assertEquals(List.of("O-O", "O-O-O"), game.getSanHistory());
    }

    @Test
    void cannotCastleThroughAttackedSquareOrOutOfCheck() {
        // La tour f8 noire contrôle f1 : petit roque interdit, grand roque permis.
        ChessGame game = fromFen("5r1k/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        assertFalse(game.getLegalMoves().contains(Move.fromUci("e1g1")));
        assertTrue(game.getLegalMoves().contains(Move.fromUci("e1c1")));

        ChessGame inCheck = fromFen("4r2k/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        assertFalse(inCheck.getLegalMoves().contains(Move.fromUci("e1g1")));
        assertFalse(inCheck.getLegalMoves().contains(Move.fromUci("e1c1")));
    }

    @Test
    void queensideCastlingAllowedWhenOnlyB1IsAttacked() {
        ChessGame game = fromFen("1r5k/8/8/8/8/8/8/R3K3 w Q - 0 1");
        assertTrue(game.play(Move.fromUci("e1c1")));
    }

    @Test
    void movingRookLosesCastlingRightOnThatSide() {
        ChessGame game = playAll(fromFen("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1"), "h1h2", "a8a7", "h2h1", "a7a8");
        assertFalse(game.getLegalMoves().contains(Move.fromUci("e1g1")));
        assertTrue(game.getLegalMoves().contains(Move.fromUci("e1c1")));
        assertTrue(game.getFen().contains(" Qk "));
    }

    @Test
    void enPassantCapturesThePawnBehind() {
        ChessGame game = playAll(new ChessGame(), "e2e4", "a7a6", "e4e5", "d7d5");
        assertTrue(game.getFen().contains(" d6 "));
        assertTrue(game.play(Move.fromUci("e5d6")));
        assertNull(game.getBoard().getPiece(3, 3), "le pion d5 a été pris");
        assertEquals("exd6", game.getSanHistory().get(4));
    }

    @Test
    void enPassantOnlyImmediatelyAfterDoubleStep() {
        ChessGame game = playAll(new ChessGame(), "e2e4", "a7a6", "e4e5", "d7d5", "h2h3", "h7h6");
        assertFalse(game.play(Move.fromUci("e5d6")));
    }

    @Test
    void promotionRequiresAPieceChoiceAndReplacesThePawn() {
        ChessGame game = fromFen("8/P6k/8/8/8/8/8/K7 w - - 0 1");
        assertFalse(game.play(Move.fromUci("a7a8")), "promotion sans pièce choisie");
        assertTrue(game.play(Move.fromUci("a7a8n")));
        assertEquals(TypePiece.KNIGHT, game.getBoard().getPiece(0, 0).getType());
        assertEquals("a8=N", game.getSanHistory().get(0));
    }

    @Test
    void insufficientMaterialIsDraw() {
        assertEquals(GameStatus.DRAW_INSUFFICIENT_MATERIAL, fromFen("8/8/4k3/8/8/3NK3/8/8 w - - 0 1").getStatus());
        assertEquals(GameStatus.DRAW_INSUFFICIENT_MATERIAL, fromFen("8/8/4k3/5b2/8/3BK3/8/8 w - - 0 1").getStatus(), "fous de même couleur");
        assertEquals(GameStatus.ONGOING, fromFen("8/8/4kb2/8/8/3BK3/8/8 w - - 0 1").getStatus(), "fous de couleurs opposées");
        assertEquals(GameStatus.ONGOING, fromFen("8/8/4k3/8/8/3NKN2/8/8 w - - 0 1").getStatus());
        assertEquals(GameStatus.ONGOING, fromFen("8/8/4k3/8/8/3PK3/8/8 w - - 0 1").getStatus());
    }

    @Test
    void threefoldRepetitionIsDraw() {
        ChessGame game = playAll(new ChessGame(), "g1f3", "g8f6", "f3g1", "f6g8", "g1f3", "g8f6", "f3g1");
        assertEquals(GameStatus.ONGOING, game.getStatus());
        assertTrue(game.play(Move.fromUci("f6g8")));
        assertEquals(GameStatus.DRAW_REPETITION, game.getStatus());
    }

    @Test
    void fiftyMoveRuleIsDraw() {
        ChessGame game = fromFen("4k3/8/8/8/8/8/8/R3K3 w - - 99 80");
        assertTrue(game.play(Move.fromUci("a1a2")));
        assertEquals(GameStatus.DRAW_FIFTY_MOVES, game.getStatus());
    }

    @Test
    void sanDisambiguatesByFileThenRank() {
        ChessGame byFile = fromFen("4k3/8/8/8/8/8/4K3/R6R w - - 0 1");
        assertTrue(byFile.play(Move.fromUci("a1d1")));
        assertEquals("Rad1", byFile.getSanHistory().get(0));

        ChessGame byRank = fromFen("4k3/8/R7/8/8/8/8/R3K3 w - - 0 1");
        assertTrue(byRank.play(Move.fromUci("a1a3")));
        assertEquals("R1a3", byRank.getSanHistory().get(0));
    }

    @Test
    void resignationGivesTheWinToTheOpponent() {
        ChessGame game = new ChessGame();
        game.resign(true);
        assertEquals(GameStatus.RESIGNATION, game.getStatus());
        assertEquals(Boolean.FALSE, game.getWinnerIsWhite());
        assertTrue(game.getLegalMoves().isEmpty());
    }

    @Test
    void uciRoundTrip() {
        assertEquals("e7e8q", Move.fromUci("e7e8q").toUci());
        assertThrows(IllegalArgumentException.class, () -> Move.fromUci("e9e4"));
    }

    @Test
    void fenRoundTrip() {
        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
        assertEquals(fen, Fen.toFen(Fen.fromFen(fen)));
    }
}
