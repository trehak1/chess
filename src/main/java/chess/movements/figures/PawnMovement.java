package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Player;
import chess.enums.Row;
import chess.movements.Movement;

import java.util.List;

/**
 * Created by Tom on 28.6.2015.
 */
public abstract class PawnMovement {
    protected final Col col;
    protected final Row row;
    protected final Board board;
    protected final Player player;

    public PawnMovement(Board board, Row row, Col col) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.player = board.get(col, row).getPlayer();
    }

    abstract Movement forwardByOne();

    abstract Movement forwardByTwo();

    abstract List<Movement> captures();

    abstract Movement enPassantEast();

    abstract Movement enPassantWest();

    abstract List<Movement> promotions();
}
