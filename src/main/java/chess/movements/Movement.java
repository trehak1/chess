package chess.movements;

import chess.board.Board;

/**
 * Created by Tom on 27.6.2015.
 */
public abstract class Movement {

    private final Board resultingBoard;

    public Movement(Board resultingBoard) {
        this.resultingBoard = resultingBoard;
    }

    public Board getResultingBoard() {
        return resultingBoard;
    }
}
