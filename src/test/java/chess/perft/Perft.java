package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Perft {

	private static final long[] PERFT = new long[]{20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L, 2439530234167L, 69352859712417L};
	
	public long perft(int iterations) throws InterruptedException {
		Board newGame = new BoardFactory().newGameBoard();
		Player player = Player.WHITE;
		final List<Board> boards = Lists.newArrayList(newGame);
		Date start = new Date();
		System.out.println("Start: "+start);
		for (int i = 0; i < iterations; i++) {
			Set<Movement> allMoves = getAllMoves(boards, player);
			Assert.assertEquals(PERFT[i], allMoves.size());
			boards.clear();
			allMoves.forEach((m) -> boards.add(m.getResultingBoard()));
			player = player.enemy();
			System.out.println("iteration: " + (i+1) + ", boards: " + allMoves.size()+", completed on "+new Date());
			if (i == iterations - 1) {
				return allMoves.size();
			}
		}
		return -1;
	}

	private Set<Movement> getAllMoves(List<Board> boards, Player player) throws InterruptedException {
		Set<Movement> moves = Sets.newConcurrentHashSet();
		MovementFactory factory = MovementFactory.getFor(player);
		boards.parallelStream().forEach((b) -> moves.addAll(factory.getMoves(b)));
		return moves;
	}

	public static void main(String[] args) throws InterruptedException {
		Perft perft = new Perft();
		long res = perft.perft(4);
		System.out.println(res);
	}

}
