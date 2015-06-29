package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.List;

public class KnightMovements implements MovementProducer {

    private final Player player;

    public KnightMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        for (Col col : Col.validValues()) {
            for (Row row : Row.validValues()) {
                Figure f = board.get(col, row);
                if (f.getPiece() == Piece.KNIGHT && f.getPlayer() == player) {
                    movements.addAll(new KnightMovement(col, row, board).getMoves());
                }
            }
        }
        return movements;
    }
}
