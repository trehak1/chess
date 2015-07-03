package chess.movements.figures;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementProducer;
import com.google.common.base.Supplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tom on 27.6.2015.
 */
public class PawnMovements implements MovementProducer {

    private final Player player;

    public PawnMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        List<Coord> pawnCoords = MoveUtils.locateAll(Figure.get(player, Piece.PAWN), board);
        for (Coord c : pawnCoords) {
            movements.addAll(movesFor(c, board));
        }
        return movements;
    }

    private Collection<? extends Movement> movesFor(Coord c, Board board) {
        PawnMovement pawnMovement = PawnMovement.getFor(board, c);
        List<Movement> list = new ArrayList<>();
        add(list, pawnMovement::forwardByOne);
        add(list, pawnMovement::forwardByTwo);
        addAll(list, pawnMovement::captures);
        add(list, pawnMovement::enPassantEast);
        add(list, pawnMovement::enPassantWest);
        addAll(list, pawnMovement::promotions);
        return list;
    }

    private void addAll(List<Movement> list, Supplier<List<Movement>> supplier) {
        List<Movement> m = supplier.get();
        if (m != null) {
            list.addAll(m);
        }
    }

    private void add(List<Movement> list, Supplier<Movement> supplier) {
        Movement m = supplier.get();
        if (m != null) {
            list.add(m);
        }
    }

}
