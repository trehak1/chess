package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.board.MutableBoard;
import chess.enums.Player;
import org.junit.Test;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {


	@Test
	public void perftTest() throws InterruptedException {
		Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE);
		perft.perft(3);
		perft.validate(PerftResults.POSITION_1);
	}

	@Test
	public void perftPosition2Test() throws InterruptedException {
		Board board = new BoardLoader().loadBoard("perft/perftPosition2.txt");
		Board mutable = MutableBoard.from(board);
		Perft perft = new Perft(mutable, Player.WHITE);
		perft.perft(3);
		perft.validate(PerftResults.POSITION_2);
	}

	@Test
	public void perftPosition3Test() throws InterruptedException {
		Board board = new BoardLoader().loadBoard("perft/perftPosition3.txt");
		Perft perft = new Perft(board, Player.WHITE);
		perft.perft(3);
		perft.validate(PerftResults.POSITION_3);
	}

	@Test
	public void perftPosition4Test() throws InterruptedException {
		Board board = new BoardLoader().loadBoard("perft/perftPosition4.txt");
		Perft perft = new Perft(board, Player.WHITE);
		perft.perft(3);
		perft.validate(PerftResults.POSITION_4);
	}

	@Test
	public void perftPosition5Test() throws InterruptedException {
		Board board = new BoardLoader().loadBoard("perft/perftPosition5.txt");
		Perft perft = new Perft(board, Player.WHITE);
		perft.perft(1);
		perft.validate(PerftResults.POSITION_5);

//		perft.perft(2);
//		perft.validate(PerftResults.POSITION_5);

//		perft.perft(3);
//		perft.validate(PerftResults.POSITION_5);
		
	}

	@Test
	public void perftPosition6Test() throws InterruptedException {
		Board board = new BoardLoader().loadBoard("perft/perftPosition6.txt");
		Perft perft = new Perft(board, Player.WHITE);
		perft.perft(3);
		perft.validate(PerftResults.POSITION_6);
	}

    @Test
    public void perftPromotionTest() throws InterruptedException {
        Board board = new BoardLoader().loadBoard("perft/perftPromotion.txt");
        Perft perft = new Perft(board, Player.WHITE);
        perft.perft(4);
        perft.validate(PerftResults.PROMOTION);
    }

}
