package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class KnightMovement {

    private final Col col;
    private final Row row;
    private final Board board;
    private final Player player;
    private final MoveUtils moveUtils;
    private final Figure figure;


    public KnightMovement(Col col, Row row, Board board) {
        this.col = col;
        this.row = row;
        this.board = board;
        this.player = board.get(col, row).getPlayer();
        this.moveUtils = new MoveUtils(board, col, row);
        this.figure = board.get(col, row);
        Preconditions.checkArgument(figure.getPiece() == Piece.KNIGHT);
    }

    public List<Movement> getMoves() {
        List<Movement> movementList = new ArrayList<>();
        add(movementList, this::getNorthNorthWest);
        add(movementList, this::getNorthNorthEast);
        add(movementList, this::getSouthSouthWest);
        add(movementList, this::getSouthSouthEast);
        add(movementList, this::getNorthWestWest);
        add(movementList, this::getNorthEastEast);
        add(movementList, this::getSouthWestWest);
        add(movementList, this::getSouthEastEast);
        return movementList;
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

    private Movement getSouthSouthEast() {
        return get(row.south().south(), col.east());
    }

    private Movement getSouthSouthWest() {
        return get(row.south().south(), col.west());
    }

    private Movement getSouthEastEast() {
        return get(row.south(), col.east().east());
    }

    private Movement getSouthWestWest() {
        return get(row.south(), col.west().west());
    }

    private Movement getNorthNorthEast() {
        return get(row.north().north(), col.east());
    }

    private Movement getNorthNorthWest() {
        return get(row.north().north(), col.west());
    }

    private Movement getNorthEastEast() {
        return get(row.north(), col.east().east());
    }

    private Movement getNorthWestWest() {
        return get(row.north(), col.west().west());
    }

    private void add(List<Movement> list, Supplier<Movement> supplier) {
        Movement m = supplier.get();
        if (m != null) {
            list.add(m);
        }
    }

}
