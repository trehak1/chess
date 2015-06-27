package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementProducer;

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
                    movements.addAll(movesFor(col, row));
                }
            }
        }
        return movements;
    }

    private Collection<? extends Movement> movesFor(Col col, Row row) {
        return null;
    }

}
