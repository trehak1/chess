package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementExecutor;
import chess.movements.MovementFactory;
import chess.movements.MovementType;
import com.google.common.util.concurrent.AtomicLongMap;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Perft {

	private final Board board;
	private final Player player;
	private final AtomicLongMap<MovementType> types = AtomicLongMap.create();
	private int depth;

	public Perft(Board board, Player player) {
		this.board = board;
		this.player = player;
	}

	private long perft(Board board, Player player, int depth) {
		if (depth == 0) {
			return 1;
		}

		List<Movement> moves = MovementFactory.getFor(player).getMoves(board);
		if (depth == 1) {
			moves.forEach((m) -> types.incrementAndGet(m.getType()));
		}
		AtomicLong nodes = new AtomicLong(0);
		moves.stream().forEach((m) -> {
			Board nb = new MovementExecutor(board).doMove(m);
			nodes.addAndGet(perft(nb, player.enemy(), depth - 1));
		});
//		for (int i = 0; i < moves.size(); i++) {
//			Board nb = new MovementExecutor(board).doMove(moves.get(i));
//			count += perft(nb, player.enemy(), depth - 1);
//		}
//		return count;
		return nodes.get();
	}

	public long perft(int depth) {
		this.depth = depth;
		types.clear();
		return perft(board, player, depth);
	}

	public boolean validate(PerftResult perftResult) {
		boolean res = true;
		res &= compare(perftResult.getCaptures(depth), MovementType.CAPTURE);
		res &= compare(perftResult.getCastlings(depth), MovementType.CASTLING);
		res &= compare(perftResult.getEnPassants(depth), MovementType.EN_PASSANT);
		res &= compareSum(perftResult.getNodes(depth));
		return res;
	}

	private boolean compareSum(long nodes) {
		long total = types.sum();
		if (total != nodes) {
			System.err.println("Mismatching number of total nodes, expected " + nodes + ", got " + total);
			return false;
		}
		return true;
	}

	private boolean compare(long captures, MovementType t) {
		if (captures != types.get(t)) {
			System.err.println("Mismatching number of " + t + ", expected " + captures + ", got " + types.get(t));
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws InterruptedException {
		Board newGameBoard = new BoardFactory().newGameBoard();

		for (int i = 0; i < 6; i++) {
			Perft perft = new Perft(newGameBoard, Player.WHITE);
			System.out.println(perft.perft(i));
			System.out.println(perft.types);
		}

	}


}
