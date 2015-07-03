package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class EnPassant extends Capture {

    public EnPassant(Coord from, Coord to, Board resultingBoard) {
        super(from, to, resultingBoard);
    }

}
