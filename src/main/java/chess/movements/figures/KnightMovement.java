package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementType;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class KnightMovement {
    
    private final Board board;
    private final Player player;
    private final MoveUtils moveUtils;
    private final Figure figure;
    private final Coord coord;


    public KnightMovement(Coord coord, Board board) {
        this.coord =coord;
        this.board = board;
        this.player = board.get(coord).getPlayer();
        this.moveUtils = new MoveUtils(board, coord);
        this.figure = board.get(coord);
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
        if (board.isEmpty(target)) {
            return new Movement(MovementType.MOVE, from, target, new MovementEffect().disableEnPassantIfAllowed(board));
        } else if (moveUtils.isEnemy(target)) {
            return new Movement(MovementType.CAPTURE, from, target, new MovementEffect().captured(board.get(target).getPiece()).disableEnPassantIfAllowed(board));
        } else {
            return null;
        }
    }


    private void add(List<Movement> list, Movement m) {
        if (m != null) {
            list.add(m);
        }
    }

}
