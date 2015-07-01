package chess.movements;

import chess.board.Board;
import chess.enums.Coord;

/**
 * Created by Tom on 27.6.2015.
 */
public abstract class Movement {

    private final Board resultingBoard;
    protected final Coord from;
    protected final Coord to;

    public Movement(Board resultingBoard, Coord from, Coord to) {
        this.resultingBoard = resultingBoard;
        this.from = from;
        this.to = to;
    }

    public Board getResultingBoard() {
        return resultingBoard;
    }
    
    public Coord getFrom() {
        return from;
    }
    
    public Coord getTo() {
        return to;
    }
}
