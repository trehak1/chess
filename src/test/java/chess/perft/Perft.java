package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Capture;
import chess.movements.Movement;
import chess.movements.MovementFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        System.out.println("Start: "+start);
        for (int i = 0; i < iterations; i++) {
            Set<Movement> allMoves = getAllMoves(boards, player);
            Assert.assertEquals(perftResult.getNodes(i), allMoves.size());
            boards.clear();
            allMoves.forEach((m) -> boards.add(m.getResultingBoard()));
            player = player.enemy();
            System.out.println("iteration: " + (i+1) + ", boards: " + allMoves.size() + ", completed on " + new Date());
            if (i == iterations - 1) {
                return allMoves.size();
            }
        }
        return -1;
    }

    private Set<Movement> getAllMoves(List<Board> boards, Player player) throws InterruptedException {
        Set<Movement> moves = Sets.newConcurrentHashSet();
        MovementFactory factory = MovementFactory.getFor(player);
//        boards.parallelStream().forEach((b) -> moves.addAll(factory.getMoves(b)));
        boards.forEach((b) -> moves.addAll(factory.getMoves(b)));
        return moves;
    }
    
    private Set<Capture> getAllCaptures(Set<Movement> movements) {
        Set<Capture> captures = new HashSet<>();
        movements.forEach((m) -> {if (m instanceof Capture){ captures.add((Capture) m); }});
        return captures;
    }

    public static void main(String[] args) throws InterruptedException {
        Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE, PerftResults.POSITION_1);
        long res = perft.perft(5);
        System.out.println(res);
    }

}
