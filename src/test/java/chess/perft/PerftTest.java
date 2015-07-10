package chess.perft;

import chess.board.*;
import chess.enums.Coord;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.AtomicLongMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {


    @Test
    public void perftTest() {
        Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE);
        perft.perft(5);
        perft.validate(PerftResults.POSITION_1);
    }

    @Test
    public void testCastlingPawnEndangers() {
        Board board = new BoardSerializer().readFromFEN("r3k2r/4P3/8/8/8/8/8/4K3 b kq - 0 1");
        MovementFactory f = MovementFactory.getFor(board.getPlayerOnTurn());
        List<Movement> moves = f.getMoves(board);
        Assert.assertFalse(Iterables.any(moves,(m)->m.getType()==MovementType.CASTLING));
    }

    @Test
    public void perftPosition2Test() {
        Board board = new BoardSerializer().readFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        Perft perft = new Perft(board, board.getPlayerOnTurn());
        perft.perft(4);
        AtomicLongMap<String> vals = perft.getBreakdown();
        for(Map.Entry<String, Long> e : vals.asMap().entrySet()) {
            System.out.println(e.getKey()+" "+e.getValue());
        }
        perft.validate(PerftResults.POSITION_2);
    }

    @Test
    public void perftPosition3Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition3.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(5);
        perft.validate(PerftResults.POSITION_3);
    }

    @Test
    public void perftPosition4Test() {
        Board board = new BoardSerializer().readFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        Perft perft = new Perft(board, board.getPlayerOnTurn());
        perft.perft(5);
        perft.validate(PerftResults.POSITION_4);
    }

    @Test
    public void perftPosition5Test() {
        Board board = new BoardSerializer().readFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        Perft perft = new Perft(board, board.getPlayerOnTurn());
        perft.perft(3);
        perft.validateTotalNodes(62379);
    }

    @Test
    public void perftPosition6Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition6.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(5);
        perft.validate(PerftResults.POSITION_6);
    }

    @Test
    public void perftPromotionTest() {
        Board board = new BoardLoader().loadBoard("perft/perftPromotion.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(5);
        perft.validate(PerftResults.PROMOTION);
    }

    @Test
    public void fensTestSuite() throws IOException {
        fensTest("/perft/testsuite.txt",4);
    }

    @Test
    public void fensTest() throws IOException {
       fensTest("/perft/fens.txt",9);
    }

    @Test
    public void castlingFens() throws IOException {
        fensTest("/perft/castlingFens.txt",9);
    }

    private void fensTest(String filename, int maxDepth) throws IOException {
        InputStream in = PerftTest.class.getResourceAsStream(filename);
        List<String> lines = Splitter.on('\n').trimResults().omitEmptyStrings().splitToList(new String(ByteStreams.toByteArray(in), StandardCharsets.UTF_8));
        in.close();
        boolean total = true;
        for (String l : lines) {
            if (l.startsWith("#")) {
                continue;
            }
            List<String> vals = Splitter.on(';').trimResults().omitEmptyStrings().splitToList(l);
            String fen = vals.get(0);
            List<String> expected = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(vals.get(1));
            Board b = new BoardSerializer().readFromFEN(fen);
            Perft perft = new Perft(b, b.getPlayerOnTurn());
            for (String ex : expected) {
                List<String> spl = Splitter.on('=').trimResults().splitToList(ex);
                int depth = Integer.parseInt(spl.get(0));
                if(depth > maxDepth) {
                    System.out.println("skipping perft depth "+depth);
                    continue;
                }
                perft.perft(depth);
                boolean res = perft.compareSum(Long.parseLong(spl.get(1)));
                System.out.println("Perft (" + spl.get(0) + ") for " + fen + " calculated, result " + res + " got " + perft.getSum() + "/" + spl.get(1) + " nodes");
                total &= res;
            }
        }
        Assert.assertTrue("Some of perft tests failed", total);
    }

}
