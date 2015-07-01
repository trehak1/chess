package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class Move extends Movement {

    public Move(Coord from, Coord to, Board resultingBoard) {
        super(resultingBoard, from, to);
    }

}
