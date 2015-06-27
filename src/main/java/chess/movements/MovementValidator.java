package chess.movements;

import chess.board.Board;

/**
 * Validates corner cases:
 *
 * 1) will this move lead to check (not allowed)
 * 2) will this move
 *
 */
public interface MovementValidator {

    boolean isValid(Movement movement, Board board);

}
