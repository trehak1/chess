package chess.movements.transformers;

import chess.movements.Movement;

/**
 * Created by Tom on 30.6.2015.
 */
public interface NotationTransformer {

    String toNotation(Movement movement);

    Movement fromNotation(String notation);

}
