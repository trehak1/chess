package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public class EnPassant extends Movement {

    public EnPassant(Coord from, Coord to, Board resultingBoard) {
        super(resultingBoard, from, to);
    }

    public Coord getTargetPiece() {
        return Coord.get(to.getCol(), from.getRow());
    }

}
