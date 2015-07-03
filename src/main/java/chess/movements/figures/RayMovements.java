package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementProducer;
import chess.movements.MovementType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RayMovements implements MovementProducer {

    protected final Player player;
    protected final Piece piece;

    public RayMovements(Player player, Piece piece) {
        this.player = player;
        this.piece = piece;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> moves = new ArrayList<>();
        List<Coord> coords = MoveUtils.locateAll(Figure.get(player, piece), board);
        for (Coord c : coords) {
            moves.addAll(createMoves(board, c.getCol(), c.getRow()));
        }
        return moves;
    }

    protected abstract Collection<? extends Movement> createMoves(Board board, Col c, Row r);

    protected void processRayList(List<Movement> list, List<Coord> coords, MoveUtils moveUtils) {
        for (Coord c : coords) {
            // move
            if (moveUtils.isEmpty(c.getCol(), c.getRow())) {
                Movement move = new Movement(MovementType.MOVE, moveUtils.myCoords(), c, MovementEffect.NONE);
                list.add(move);
            } else if (moveUtils.isEnemy(c.getCol(), c.getRow())) {
                // capture
                Movement move = new Movement(MovementType.CAPTURE, moveUtils.myCoords(), c, MovementEffect.NONE);
                list.add(move);
            } else {
                throw new IllegalStateException("wtf");
            }
        }
    }

}
