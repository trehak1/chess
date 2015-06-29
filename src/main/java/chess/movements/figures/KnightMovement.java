package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;
import chess.movements.MovementProducer;
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
        add(movementList, this::getNorthWest);
        add(movementList, this::getNorthEast);
        add(movementList, this::getSouthWest);
        add(movementList, this::getSouthEast);
        return movementList;
    }

    private Movement get(Row targetRow, Col targetCol) {
        if (targetRow == Row.INVALID || targetCol == Col.INVALID) {
            return null;
        }
        Coord from = Coord.get(this.col, this.row);
        Coord to = Coord.get(targetCol, targetRow);
        if (moveUtils.isEmpty(targetCol, targetRow)) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.set(targetCol, targetRow, figure);
            Move m = new Move(from, to, resultingBoard);
            return m;
        } else if (moveUtils.isEnemy(targetCol, targetRow)) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.remove(targetCol,targetRow);
            resultingBoard = resultingBoard.set(targetCol, targetRow, figure);
            Capture c = new Capture(from, to, resultingBoard);
            return c;
        }
        return null;
    }

    private Movement getSouthEast() {
        return get(row.south().south(), col.east());
    }

    private Movement getSouthWest() {
        return get(row.south().south(), col.west());
    }

    private Movement getNorthEast() {
        return get(row.north().north(), col.east());
    }

    private Movement getNorthWest() {
        return get(row.north().north(), col.west());
    }

    private void add(List<Movement> list, Supplier<Movement> supplier) {
        Movement m = supplier.get();
        if (m != null) {
            list.add(m);
        }
    }
    
}
