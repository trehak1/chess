package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMove extends Movement {

    private final Castling type;

    public CastlingMove(Board resultingBoard, Castling type, Coord originalKingCoord, Coord newKingCoord) {
        super(resultingBoard, originalKingCoord, newKingCoord);
        this.type = type;
    }

    public Castling getType() {
        return type;
    }
}
