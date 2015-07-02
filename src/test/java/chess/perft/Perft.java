package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementFactory;
import com.google.common.collect.Lists;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Perft {

	private static final long[] PERFT = new long[]{20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L, 2439530234167L, 69352859712417L};

	public long perft(int iterations) throws InterruptedException {
		Board newGame = new BoardFactory().newGameBoard();
		Player player = Player.WHITE;
		List<Board> boards = Lists.newArrayList(newGame);
		for (int i = 0; i < iterations; i++) {
			List<Movement> allMoves = getAllMoves(boards, player);
			Assert.assertEquals(PERFT[i], allMoves.size());
			boards = Lists.transform(allMoves, (m) -> m.getResultingBoard());
			player = player.enemy();
			System.out.println("iteration: "+i+", boards: "+allMoves.size());
			if (i == iterations - 1) {
				return allMoves.size();
			}
		}
		return -1;
	}

	private List<Movement> getAllMoves(List<Board> boards, Player player) throws InterruptedException {
		List<Movement> moves = new ArrayList<>();
		MovementFactory factory = MovementFactory.getFor(player);
		for (Board b : boards) {
//			Thread.sleep(1);
			moves.addAll(factory.getMoves(b));
		}
		return moves;
	}

	public static void main(String[] args) throws InterruptedException {
		Perft perft = new Perft();
		long res = perft.perft(5);
		System.out.println(res);
	}

}
