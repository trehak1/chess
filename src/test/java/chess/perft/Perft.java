package chess.perft;

import chess.board.Board;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementExecutor;
import chess.movements.MovementFactory;
import chess.movements.MovementType;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicLongMap;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Perft {

    private final Board board;
    private final Player player;
    private final AtomicLongMap<MovementType> types = AtomicLongMap.create();
    private final AtomicLongMap<String> breakdown = AtomicLongMap.create();
    private int depth;

    public Perft(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    private void perft(Board board, Player player, int depth, Movement rootMove) {
        if (depth == 0) {
            return;
        }
        List<Movement> moves = MovementFactory.getFor(player).getMoves(board);
        if (depth == 1) {
            final Movement finalRootMove = rootMove;
            moves.forEach((m) -> {
                types.incrementAndGet(m.getType());
                Movement cm = finalRootMove != null ? finalRootMove : m;
                String val = (cm.getFrom() + "" + cm.getTo()).toLowerCase();
                breakdown.incrementAndGet(val);
            });
        }
        for (int i = 0; i < moves.size(); i++) {
            Movement m = moves.get(i);
            MovementExecutor movementExecutor = new MovementExecutor(board);
            Board nb = movementExecutor.doMove(m);
            if (depth == this.depth) {
                rootMove = m;
            }
            perft(nb, player.enemy(), depth - 1, rootMove);
//            Board undone = movementExecutor.undo();
//            if(!nb.equals(undone)) {
//                throw new IllegalArgumentException("wtf");
//            }
        }
    }

    public void perft(int depth) {
        this.depth = depth;
        types.clear();
        breakdown.clear();
        perft(board, player, depth, null);
    }

    public AtomicLongMap<String> getBreakdown() {
        return breakdown;
    }

    public boolean validateTotalNodes(long total) {
        boolean val = compareSum(total);
        Assert.assertTrue("perft does not match!", val);
        return val;
    }

    public boolean validate(PerftResult perftResult) {
        boolean res = true;
        res &= compare(perftResult.getCaptures(depth - 1), MovementType.CAPTURE, MovementType.EN_PASSANT, MovementType.PROMOTION_CAPTURE);
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
                System.err.println("Mismatching number of " + Arrays.toString(movementTypes) + "expected " + expected + ", got " + actual);
            }
            return false;
        }
        return true;
    }

    public void printDivide() {
        AtomicLongMap<String> vals = getBreakdown();
        for(Map.Entry<String, Long> e : vals.asMap().entrySet()) {
            System.out.println(e.getKey()+" "+e.getValue());
        }
    }
}
