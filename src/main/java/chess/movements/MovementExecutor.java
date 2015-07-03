package chess.movements;

import chess.board.Board;
import chess.enums.CastlingType;
import chess.enums.Figure;
import chess.enums.Player;
import com.google.common.base.Preconditions;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementExecutor {

    private final Board board;

    public MovementExecutor(Board currentBoard) {
        Preconditions.checkNotNull(currentBoard);
        this.board = currentBoard;
    }

    public Board doMove(Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        Preconditions.checkArgument(board.get(movement.getFrom()).getPlayer() == board.getOnTurn(), "Trying to move enemy figure");
        MovementEffect effect = movement.getMovementEffect();
        // remove moved figure
        Board mutated = board.remove(movement.getFrom());
        // remove target figure if any
        if (mutated.get(movement.getTo()) != Figure.NONE) {
            mutated = mutated.remove(movement.getTo());
        }
        // clear en passant information
        mutated = mutated.clearEnPassant();
        // promotion ?
        if (effect.getPromotedTo() != null) {
            mutated = mutated.set(movement.getTo(), Figure.get(board.getOnTurn(), effect.getPromotedTo()));
        } else {
            mutated = mutated.set(movement.getTo(), board.get(movement.getFrom()));
        }
        // allow en passant ?
        if (effect.getAllowEnPassant() != null) {
            mutated = mutated.allowEnPassant(effect.getAllowEnPassant());
        }
        // disable castlings?
        for (Player p : Player.values()) {
            for (CastlingType ct : CastlingType.values()) {
                if (!effect.getDisableCastlings().isCastlingEnabled(p, ct)) {
                    mutated = mutated.disableCastling(p, ct);
                }
            }
        }
        mutated = mutated.setOnTurn(mutated.getOnTurn().enemy());
        return mutated;
    }

    public Board undoMove(Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
