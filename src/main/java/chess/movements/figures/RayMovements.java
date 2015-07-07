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
        List<Coord> coords = board.locateAll(Figure.get(player, piece));
        for (Coord c : coords) {
            moves.addAll(createMoves(board, c.getCol(), c.getRow()));
        }
        return moves;
    }

    protected abstract Collection<? extends Movement> createMoves(Board board, Col c, Row r);

    protected void processRayList(List<Movement> list, List<Coord> coords, MoveUtils moveUtils, Board board) {
        for (Coord c : coords) {
            // move
            if (board.isEmpty(c)) {
                Movement move = new Movement(MovementType.MOVE, moveUtils.myCoords(), c, new MovementEffect().disableEnPassantIfAllowed(board));
                list.add(move);
            } else if (moveUtils.isEnemy(c)) {
                // capture
                Movement move = new Movement(MovementType.CAPTURE, moveUtils.myCoords(), c, new MovementEffect().captured(board.get(c).getPiece()).disableEnPassantIfAllowed(board));
                list.add(move);
            } else {
                throw new IllegalStateException("wtf");
            }
        }
    }

}
