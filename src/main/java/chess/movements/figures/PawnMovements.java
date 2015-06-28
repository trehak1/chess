package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
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
        for (Col col : Col.values()) {
            for (Row row : Row.values()) {
                Figure f = board.get(col, row);
                if (f.getPiece() == Piece.PAWN && f.getPlayer() == player) {
                    movements.addAll(movesFor(col, row, board));
                }
            }
        }
        return movements;
    }

    private Collection<? extends Movement> movesFor(Col col, Row row, Board board) {
        PawnMovement pawnMovement;
        switch (player) {
            case WHITE:
                pawnMovement = new WhitePawnMovement(col, row, board);
                break;
            case BLACK:
                pawnMovement = new BlackPawnMovement(col, row, board);
                break;
            default:
                throw new IllegalArgumentException("wtf");
        }

        List<Movement> list = new ArrayList<>();
        add(list, pawnMovement::forwardByOne);
        add(list, pawnMovement::forwardByTwo);
        add(list, pawnMovement::captures);
        add(list, pawnMovement::enPassants);
        add(list, pawnMovement::promotion);
        return list;
    }

    private void add(List<Movement> list, Supplier<Movement> supplier) {
        Movement m = supplier.get();
        if (m != null) {
            list.add(m);
        }
    }

}
