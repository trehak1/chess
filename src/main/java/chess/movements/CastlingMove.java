package chess.movements;

import chess.board.Board;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMove extends Movement {

    private final Castling type;

    public CastlingMove(Board resultingBoard, Castling type) {
        super(resultingBoard);
        this.type = type;
    }

    public Castling getType() {
        return type;
    }
}
