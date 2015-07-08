package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.board.BoardSerializer;
import chess.enums.Player;
import org.junit.Test;

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

    // following tests from http://www.chessprogramming.net/perfect-perft/
    @Test
    public void testMore1() {
        Board b = new BoardSerializer().readFromFEN("1k6/1b6/8/8/7R/8/8/4K2R b K - 0 1");
        Perft perft = new Perft(b,b.getOnTurn());
        perft.perft(5);
        perft.validateTotalNodes(1063513);
    }

    @Test
    public void testMore2() {
        Board b = new BoardSerializer().readFromFEN("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1");
        Perft perft = new Perft(b,b.getOnTurn());
        perft.perft(6);
        perft.validateTotalNodes(1134888);
    }

    @Test
    public void testMore3() {
        Board b = new BoardSerializer().readFromFEN("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1");
        Perft perft = new Perft(b,b.getOnTurn());
        perft.perft(4);
        perft.validateTotalNodes(1720476);
    }

}
