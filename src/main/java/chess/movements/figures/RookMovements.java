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

public class RookMovements extends RayMovements implements MovementProducer {

    public RookMovements(Player player) {
        super(player, Piece.ROOK);
    }

    @Override
    protected Collection<Movement> createMoves(Board board, Col c, Row r) {
        MoveUtils moveUtils = new MoveUtils(board, c, r);
        List<Movement> l = new ArrayList<>();
        processRayList(l, moveUtils.getRayNorth(), moveUtils);
        processRayList(l, moveUtils.getRayWest(), moveUtils);
        processRayList(l, moveUtils.getRayEast(), moveUtils);
        processRayList(l, moveUtils.getRaySouth(), moveUtils);
        return addCastlingChangeInformation(board, l);
    }

    private Collection<Movement> addCastlingChangeInformation(Board board, List<Movement> l) {
        if (!board.getCastlingRights().isCastlingEnabled(player, CastlingType.KING_SIDE) && !board.getCastlingRights().isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
            // no castling was enabled, no need to disable anything
            return l;
        }

        List<Movement> modified = new ArrayList<>();
        for (Movement m : l) {
            MovementEffect me;
            if (m.getFrom() == Coord.get(Col.A, player.getStartingRow()) && board.getCastlingRights().isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
                me = new MovementEffect().disableCastling(CastlingType.QUEEN_SIDE);
            } else if (m.getFrom() == Coord.get(Col.H, player.getStartingRow()) && board.getCastlingRights().isCastlingEnabled(player, CastlingType.KING_SIDE)) {
                me = new MovementEffect().disableCastling(CastlingType.KING_SIDE);
            } else {
                me = MovementEffect.NONE;
            }

            if (m.getType() != MovementType.MOVE || m.getType() != MovementType.CAPTURE) {
                throw new IllegalArgumentException("wtf");
            }
            Movement movement = new Movement(m.getType(), m.getFrom(), m.getTo(), me);
            modified.add(movement);
        }
        return modified;
    }

}
