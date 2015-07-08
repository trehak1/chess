package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardSerializer;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementExecutor;
import chess.movements.MovementFactory;
import chess.movements.MovementType;
import com.google.common.util.concurrent.AtomicLongMap;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class Perft {

    private final Board board;
    private final Player player;
    private final AtomicLongMap<MovementType> types = AtomicLongMap.create();
    private int depth;

    public Perft(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    private void perft(Board board, Player player, int depth) {
        if (depth == 0) {
            return;
        }

//        System.out.println("FROM ::");
//        System.out.println("==================");
//        System.out.println(new BoardSerializer().serializeIntoUtf8(board));

        List<Movement> moves = MovementFactory.getFor(player).getMoves(board);
        if (depth == 1) {
            moves.forEach((m) -> types.incrementAndGet(m.getType()));
        }
        for (int i = 0; i < moves.size(); i++) {
            Movement m = moves.get(i);
            Board nb = new MovementExecutor(board).doMove(m);
//            System.out.println("TO ::");
//            System.out.println(new BoardSerializer().serializeIntoUtf8(nb));
//            System.out.println("==================");
            perft(nb, player.enemy(), depth - 1);
            Board undo = new MovementExecutor(nb).undoMove(m);
            if (!board.equals(undo)) {
                new MovementExecutor(nb).undoMove(m);
                Assert.assertEquals("Invalid undo of " + m, board, undo);
            }
        }
    }

    public void perft(int depth) {
        this.depth = depth;
        types.clear();
        perft(board, player, depth);
    }

    public boolean validateTotalNodes(long total) {
        boolean val = compareSum(total);
        Assert.assertTrue("perft does not match!", val);
        return val;
    }

    public boolean validate(PerftResult perftResult) {
        boolean res = true;
        res &= compare(perftResult.getCaptures(depth - 1), MovementType.CAPTURE, MovementType.EN_PASSANT);
        res &= compare(perftResult.getCastlings(depth - 1), MovementType.CASTLING);
        res &= compare(perftResult.getEnPassants(depth - 1), MovementType.EN_PASSANT);
        res &= compareSum(perftResult.getNodes(depth - 1));
        Assert.assertTrue("perft does not match!", res);
        return res;
    }

    public boolean compareSum(long nodes) {
        long total = types.sum();
        if (total != nodes) {
            System.err.println("Mismatching number of total nodes, expected " + nodes + ", got " + total);
            return false;
        }
        return true;
    }

    public long getSum() {
        return types.sum();
    }

    private boolean compare(long expected, MovementType... movementTypes) {
        long actual = 0;
        for (MovementType t : movementTypes) {
            actual += types.get(t);
        }
        if (expected != actual) {
            if (expected == -1) {
                System.err.println("No information about expected count of " + Arrays.toString(movementTypes) + "actual # was " + actual);
                return true;
            } else {
                System.err.println("Mismatching number of " +  Arrays.toString(movementTypes) + "expected " + expected + ", got " + actual);
            }
            return false;
        }
        return true;
    }

}
