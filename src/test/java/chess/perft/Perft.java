package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementExecutor;
import chess.movements.MovementFactory;
import chess.movements.MovementType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Perft {

	private Board board;
	private Player player;
	private PerftResult perftResult;

	public Perft(Board board, Player player, PerftResult perftResult) {
		this.board = board;
		this.player = player;
		this.perftResult = perftResult;
	}

	public long perft(int iterations) throws InterruptedException {
		final List<Board> boards = Lists.newArrayList(board);
		Date start = new Date();
		System.out.println("Start: " + start);
		for (int i = 0; i < iterations; i++) {
			Set<Movement> allMoves = getAllMoves(boards, player);
			Assert.assertEquals("number of nodes differ", perftResult.getNodes(i), allMoves.size());

			List<Movement> captures = allMoves.stream().filter((m) -> m.getType() == MovementType.CAPTURE || m.getType() == MovementType.PROMOTION_CAPTURE).collect(Collectors.toList());
			Assert.assertEquals("number of captures differ", perftResult.getCaptures(i), captures.size());

			List<Movement> enPassants = allMoves.stream().filter((m) -> m.getType() == MovementType.EN_PASSANT).collect(Collectors.toList());
			Assert.assertEquals("number of enpassants differ", perftResult.getEnPassants(i), enPassants.size());

			List<Movement> castlings = allMoves.stream().filter((m) -> m.getType() == MovementType.CASTLING).collect(Collectors.toList());
			Assert.assertEquals("number of castlings differ", perftResult.getCastlings(i), castlings.size());

			boards.clear();
			// TODO FIX PERFT to perform Depth-first search; to achieve this create UNDO in MovementExecutor
//            allMoves.forEach((m) -> {
//                new MovementExecutor().doMove(m)
//                return boards.add(m.getResultingBoard();
//            }));
			player = player.enemy();
			System.out.println("iteration: " + (i + 1) + ", boards: " + allMoves.size() + ", completed on " + new Date());
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
//        boards.forEach((b) -> moves.addAll(factory.getMoves(b)));
		return moves;
	}


	public static void main(String[] args) throws InterruptedException {
//        Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE, PerftResults.POSITION_1);
//        long res = perft.perft(5);
//        System.out.println(res);

		Board newGameBoard = new BoardFactory().newGameBoard();

		for (int i = 0; i < 6; i++) {
			long results = perft2(i, newGameBoard);
			System.out.println(i + " : " + results);
		}

	}

	private static long perft2(int depth, final Board board) {
		if (depth == 0) {
			return 1;
		}
		AtomicLong nodes = new AtomicLong(0);
		
		List<Movement> moves = MovementFactory.getFor(board.getOnTurn()).getMoves(board);
		moves.parallelStream().forEach((m) -> {
			Board mutated = board;
			mutated = new MovementExecutor(mutated).doMove(m);
			nodes.addAndGet(perft2(depth - 1, mutated));
			mutated = new MovementExecutor(mutated).undoMove(m);
		});
//		for (int i = 0; i < moves.size(); i++) {
//			board = new MovementExecutor(board).doMove(moves.get(i));
//			nodes += perft2(depth - 1, board);
//			board = new MovementExecutor(board).undoMove(moves.get(i));
//		}
		return nodes.get();
	}


}
