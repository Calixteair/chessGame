package com.chessgame.model;

import com.chessgame.utils.Fen;
import com.chessgame.utils.Move;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Perft : compte toutes les suites de coups légaux jusqu'à une profondeur donnée et compare aux
 * valeurs de référence (chessprogramming.org/Perft_Results). Couvre roques, prises en passant,
 * promotions, clouages et échecs à la découverte.
 */
class PerftTest {

    static long perft(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        for (Move move : board.legalMoves()) {
            Board next = board.copy();
            next.apply(move);
            nodes += perft(next, depth - 1);
        }
        return nodes;
    }

    @ParameterizedTest(name = "{0} profondeur {1}")
    @CsvSource(delimiter = '|', value = {
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 | 1 | 20",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 | 2 | 400",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 | 3 | 8902",
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 | 4 | 197281",
            // « Kiwipete » : roques, clouages, promotions
            "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 | 1 | 48",
            "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 | 2 | 2039",
            "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 | 3 | 97862",
            // Finale : prises en passant découvrant un échec
            "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 | 1 | 14",
            "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 | 3 | 2812",
            "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 | 4 | 43238",
            // Promotions et roques perdus
            "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1 | 1 | 6",
            "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1 | 2 | 264",
            "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1 | 3 | 9467",
            "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 | 1 | 44",
            "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 | 2 | 1486",
            "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 | 3 | 62379",
    })
    void perftMatchesReference(String fen, int depth, long expected) {
        assertEquals(expected, perft(Fen.fromFen(fen), depth));
    }
}
