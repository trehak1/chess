package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

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
        Coord my = moveUtils.myCoords();
        add(movementList, get(my.north().north().west()));
        add(movementList, get(my.north().north().east()));
        add(movementList, get(my.north().east().east()));
        add(movementList, get(my.north().west().west()));
        add(movementList, get(my.south().south().west()));
        add(movementList, get(my.south().south().east()));
        add(movementList, get(my.south().east().east()));
        add(movementList, get(my.south().west().west()));
        return movementList;
    }

    private Movement get(Coord target) {
        if (target == Coord.INVALID) {
            return null;
        }
        Coord from = moveUtils.myCoords();
        if (moveUtils.isEmpty(target)) {
            Move m = new Move(from, target, moveUtils.moveTo(target));
            return m;
        } else if (moveUtils.isEnemy(target)) {
            Capture c = new Capture(from, target, moveUtils.capture(target));
            return c;
        }
        return null;
    }


    private void add(List<Movement> list, Movement m) {
        if (m != null) {
            list.add(m);
        }
    }

}
