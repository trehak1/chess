package chess.movements;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Figure;

/**
 * Created by Tom on 27.6.2015.
 */
public class Promotion extends Movement {

    private final Figure promotedTo;

    public Promotion(Coord from, Coord to, Figure f, Board resultingBoard) {
        super(resultingBoard, from, to);
        this.promotedTo = f;
    }

    public Figure getPromotedTo() {
        return promotedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Promotion m = (Promotion) o;

        return super.equals(o) && m.promotedTo == promotedTo;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + promotedTo.hashCode();
        return result;
    }

}
