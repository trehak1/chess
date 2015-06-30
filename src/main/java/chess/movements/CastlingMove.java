package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMove extends Movement {

    private final Castling type;
    private final Coord originalKingCoord;

    public CastlingMove(Board resultingBoard, Castling type, Coord originalKingCoord) {
        super(resultingBoard);
        this.originalKingCoord = originalKingCoord;
        this.type = type;
    }

    public Coord getOriginalKingCoord() {
        return originalKingCoord;
    }

    public Castling getType() {
        return type;
    }
}
