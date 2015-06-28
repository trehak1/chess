package chess.movements;

import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class Promotion implements Movement {

    private final Coord from;
    private final Coord to;

    public Promotion(Coord from, Coord to) {
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
