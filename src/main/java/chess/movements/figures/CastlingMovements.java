package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementProducer;
import chess.movements.MovementType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMovements implements MovementProducer {

    private final Player player;

    public CastlingMovements(Player player) {
        Preconditions.checkNotNull(player);
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> list = new ArrayList<>();
        Movement ksc = createCastling(board, player, CastlingType.KING_SIDE);
        if (ksc != null) {
            list.add(ksc);
        }
        Movement qsc = createCastling(board, player, CastlingType.QUEEN_SIDE);
        if (qsc != null) {
            list.add(qsc);
        }
        return list;
    }

    private boolean isAvailable(Board board, Player player, CastlingType castlingType) {
        // check if castling is enabled
        if (!board.getCastlingRights().isCastlingEnabled(player, castlingType)) {
            return false;
        }
        if (Iterables.any(castlingType.requiredEmpty(player), (coord) -> board.get(coord) != Figure.NONE)) {
            return false;
        }
        return true;
    }

    private Movement createCastling(Board board, Player player, CastlingType castlingType) {
        if (!isAvailable(board, player, castlingType)) {
            return null;
        }
        MovementEffect me = new MovementEffect()
                .disableCastlingIfAllowed(board, CastlingType.KING_SIDE, player)
                .disableCastlingIfAllowed(board, CastlingType.QUEEN_SIDE, player)
                .disableEnPassantIfAllowed(board);
        Coord kingFrom = MoveUtils.locateKing(player, board);
        Coord kingTo = castlingType.getKingDestinationCoord(player);
        Preconditions.checkArgument(kingFrom.getRow() == kingTo.getRow());
        Movement castling = new Movement(MovementType.CASTLING, kingFrom, kingTo, me);
        return castling;
    }

}
