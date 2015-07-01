package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class Capture extends Movement {

    public Capture(Coord from, Coord to, Board resultingBoard) {
        super(resultingBoard, from, to);
    }

}
