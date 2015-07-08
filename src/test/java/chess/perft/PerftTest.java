package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.board.BoardSerializer;
import chess.enums.Player;
import com.google.common.base.Splitter;
import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {


    @Test
    public void perftTest() {
        Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE);
        perft.perft(3);
        perft.validate(PerftResults.POSITION_1);
    }

    @Test
    public void perftPosition2Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition2.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(3);
        perft.validate(PerftResults.POSITION_2);
    }

    @Test
    public void perftPosition3Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition3.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(3);
        perft.validate(PerftResults.POSITION_3);
    }

    @Test
    public void perftPosition4Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition4.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(3);
        perft.validate(PerftResults.POSITION_4);
    }

    @Test
    public void perftPosition5Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition5.txt");
        Perft perft = new Perft(board, Player.WHITE);
//		perft.perft(1);
//		perft.validate(PerftResults.POSITION_5);
//
//		perft.perft(2);
//		perft.validate(PerftResults.POSITION_5);

        perft.perft(3);
        perft.validate(PerftResults.POSITION_5);

    }

    @Test
    public void perftPosition6Test() {
        Board board = new BoardLoader().loadBoard("perft/perftPosition6.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(3);
        perft.validate(PerftResults.POSITION_6);
    }

    @Test
    public void perftPromotionTest() {
        Board board = new BoardLoader().loadBoard("perft/perftPromotion.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(4);
        perft.validate(PerftResults.PROMOTION);
    }

    @Test
    public void fensTest() throws IOException {
        InputStream in = PerftTest.class.getResourceAsStream("/perft/fens.txt");
        List<String> lines = Splitter.on('\n').trimResults().omitEmptyStrings().splitToList(new String(ByteStreams.toByteArray(in), StandardCharsets.UTF_8));
        in.close();
        for(String l : lines) {
            if(l.startsWith("#")) {
                continue;
            }
            List<String> vals = Splitter.on(';').trimResults().omitEmptyStrings().splitToList(l);
            String fen = vals.get(0);
            List<String> expected = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(vals.get(1));
            Board b = new BoardSerializer().readFromFEN(fen);
            Perft perft = new Perft(b,b.getOnTurn());
            for(String ex : expected) {
                List<String> spl = Splitter.on('=').trimResults().splitToList(ex);
                perft.perft(Integer.parseInt(spl.get(0)));
                perft.validateTotalNodes(Long.parseLong(spl.get(1)));
                System.out.println("Perft ("+spl.get(0)+") for "+fen+" calculated OK, got "+spl.get(1)+" nodes");
            }
        }
    }

}
