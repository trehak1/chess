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
    private final Figure figure;
    private final Coord coord;


    public KnightMovement(Coord coord, Board board) {
        this.coord =coord;
        this.board = board;
        this.player = board.get(coord).getPlayer();
        this.figure = board.get(coord);
        Preconditions.checkArgument(figure.getPiece() == Piece.KNIGHT);
    }

    public List<Movement> getMoves() {
        List<Movement> movementList = new ArrayList<>();
        add(movementList, get(coord.north().north().west()));
        add(movementList, get(coord.north().north().east()));
        add(movementList, get(coord.north().east().east()));
        add(movementList, get(coord.north().west().west()));
        add(movementList, get(coord.south().south().west()));
        add(movementList, get(coord.south().south().east()));
        add(movementList, get(coord.south().east().east()));
        add(movementList, get(coord.south().west().west()));
        return movementList;
    }

    private Movement get(Coord target) {
        if (target == Coord.INVALID) {
            return null;
        }
        if (board.isEmpty(target)) {
            return new Movement(MovementType.MOVE, coord, target, new MovementEffect().disableEnPassantIfAllowed(board));
        } else if (board.isPlayers(target, player.enemy())) {
            return new Movement(MovementType.CAPTURE, coord, target, new MovementEffect().captured(board.get(target).getPiece()).disableEnPassantIfAllowed(board));
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
