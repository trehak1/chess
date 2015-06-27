package chess.movements;

import chess.board.Board;

import java.util.List;

/**
 * Created by Tom on 27.6.2015.
 *
 *
 *
 */
public interface MovementProducer {

    List<Movement> getMovements(Board board);

}
