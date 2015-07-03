package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.enums.Player;
import org.junit.Test;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {


	@Test
	public void perftTest() throws InterruptedException {
		Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE, PerftResults.POSITION_1);
		perft.perft(3);
	}

    @Test
    public void perftPosition2Test() throws InterruptedException {
        Board board = new BoardLoader().loadBoard("perft/perftPosition2.txt");
        Perft perft = new Perft(board, Player.WHITE, PerftResults.POSITION_2);
        perft.perft(3);
    }

    @Test
    public void perftPosition3Test() throws InterruptedException {
        Board board = new BoardLoader().loadBoard("perft/perftPosition3.txt");
        Perft perft = new Perft(board, Player.WHITE, PerftResults.POSITION_3);
        perft.perft(3);
    }

    @Test
    public void perftPosition4Test() throws InterruptedException {
        Board board = new BoardLoader().loadBoard("perft/perftPosition4.txt");
        Perft perft = new Perft(board, Player.WHITE, PerftResults.POSITION_4);
        perft.perft(3);
    }

}
