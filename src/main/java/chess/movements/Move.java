package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class Move extends Movement {

    private final Coord from;
    private final Coord to;

    public Move(Coord from, Coord to, Board resultingBoard) {
        super(resultingBoard);
        this.from = from;
        this.to = to;
    }

    public Coord getFrom() {
        return from;
    }

    public Coord getTo() {
        return to;
    }

}
