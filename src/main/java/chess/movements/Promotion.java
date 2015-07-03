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

}
