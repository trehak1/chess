package chess.movements;

import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class Move implements Movement {

    private final Coord from;
    private final Coord to;

    public Move(Coord from, Coord to) {
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
