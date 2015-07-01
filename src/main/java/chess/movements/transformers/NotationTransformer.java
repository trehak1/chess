package chess.movements.transformers;

import chess.enums.Coord;
import chess.movements.Movement;

/**
 * Created by Tom on 30.6.2015.
 */
public interface NotationTransformer {

    String toNotation(Movement movement);

    Movement fromNotation(String notation);
    
    Coord coordFromNotation(String notation);

    String coordToNotation(Coord coord);

}
