package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class KingMovements implements MovementProducer {

    private final Player player;
    private MoveUtils moveUtils;
    private Row row;
    private Col col;

    public KingMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        for (Col c : Col.validValues()) {
            for (Row r : Row.validValues()) {
                Figure f = board.get(c, r);
                if (f != Figure.NONE && f.getPiece() == Piece.KING && f.getPlayer() == player) {
                    createMoves(movements, c, r, board);
                }
            }
        }
        return movements;
    }

    private void createMoves(List<Movement> movements, Col c, Row r, Board board) {
        moveUtils = new MoveUtils(board, c, r);
        col = c;
        row = r;
        add(movements, this::getNorthWest);
        add(movements, this::getNorthEast);
        add(movements, this::getWest);
        add(movements, this::getSouth);
        add(movements, this::getSouthWest);
        add(movements, this::getSouthEast);
        add(movements, this::getNorth);
        add(movements, this::getEast);
        // TODO
    }

    private Movement get(Row targetRow, Col targetCol) {
        if (targetRow == Row.INVALID || targetCol == Col.INVALID) {
            return null;
        }
        Coord from = Coord.get(this.col, this.row);
        Coord to = Coord.get(targetCol, targetRow);
        if (moveUtils.isEmpty(targetCol, targetRow)) {
            Move m = new Move(from, to, moveUtils.moveTo(Coord.get(targetCol, targetRow)));
            return m;
        } else if (moveUtils.isEnemy(targetCol, targetRow)) {
            Capture c = new Capture(from, to, moveUtils.capture(Coord.get(targetCol, targetRow)));
            return c;
        }
        return null;
    }

    private Movement getSouth() {
        return get(row.south(), col);
    }

    private Movement getWest() {
        return get(row, col.west());
    }

    private Movement getEast() {
        return get(row, col.east());
    }

    private Movement getNorth() {
        return get(row.north(), col);
    }

    private Movement getNorthEast() {
        return get(row.north(), col.east());
    }

    private Movement getNorthWest() {
        return get(row.north(), col.west());
    }

    private Movement getSouthEast() {
        return get(row.south(), col.east());
    }

    private Movement getSouthWest() {
        return get(row.south(), col.west());
    }

    private void add(List<Movement> list, Supplier<Movement> supplier) {
        Movement m = supplier.get();
        if (m != null) {
            list.add(m);
        }
    }
}
