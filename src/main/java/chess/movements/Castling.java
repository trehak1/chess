package chess.movements;

import chess.board.Board;
import chess.enums.CastlingType;
import chess.enums.Coord;

/**
 * Created by Tom on 29.6.2015.
 */
public class Castling extends Movement {

    private final CastlingType type;

    public Castling(Board resultingBoard, CastlingType type, Coord originalKingCoord, Coord newKingCoord) {
        super(resultingBoard, originalKingCoord, newKingCoord);
        this.type = type;
    }

    public CastlingType getType() {
        return type;
    }

}
