package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementProducer;
import chess.movements.MovementType;
import com.google.common.base.Preconditions;

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
        Coord myCoord = Coord.get(c,r);
        List<Movement> l = new ArrayList<>();
        processRayList(l, moveUtils.getRayNorth(), myCoord, board);
        processRayList(l, moveUtils.getRayWest(), myCoord, board);
        processRayList(l, moveUtils.getRayEast(), myCoord, board);
        processRayList(l, moveUtils.getRaySouth(), myCoord, board);
        return addCastlingChangeInformation(board, l);
    }

    private Collection<Movement> addCastlingChangeInformation(Board board, List<Movement> l) {
        if (!board.getCastlingRights().isCastlingEnabled(player, CastlingType.KING_SIDE) && !board.getCastlingRights().isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
            // no castling was enabled, no need to disable anything
            return l;
        }

        List<Movement> modified = new ArrayList<>();
        for (Movement m : l) {
            if (m.getType() != MovementType.MOVE && m.getType() != MovementType.CAPTURE) {
                throw new IllegalArgumentException("wtf");
            }
            MovementEffect me = m.getMovementEffect().disableEnPassantIfAllowed(board);
            if (m.getFrom() == Coord.get(Col.A, player.getStartingRow()) && board.getCastlingRights().isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
                me = me.disableCastlingIfAllowed(board, CastlingType.QUEEN_SIDE, player);
            } else if (m.getFrom() == Coord.get(Col.H, player.getStartingRow()) && board.getCastlingRights().isCastlingEnabled(player, CastlingType.KING_SIDE)) {
                me = me.disableCastlingIfAllowed(board, CastlingType.KING_SIDE, player);
            }
            Movement movement = new Movement(m.getType(), m.getFrom(), m.getTo(), me);
            modified.add(movement);
        }
        return modified;
    }

}
