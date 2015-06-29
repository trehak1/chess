package chess.movements.figures;

import chess.board.Board;
import chess.enums.Player;
import chess.movements.Castling;
import chess.movements.CastlingMove;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMoves implements MovementProducer {

    private final Player player;

    public CastlingMoves(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> list = new ArrayList<>();
        CastlingMove ksc = kingSide(board);
        if (ksc != null) {
            list.add(ksc);
        }
        CastlingMove qsc = queenSide(board);
        if (qsc != null) {
            list.add(qsc);
        }
        return list;
    }

    private CastlingMove queenSide(Board board) {
        if (!board.isCastlingEnabled(player, Castling.QUEEN_SIDE)) {
            return null;
        }
        // TODO
        return null;
    }

    private CastlingMove kingSide(Board board) {
        return null;
    }
}
