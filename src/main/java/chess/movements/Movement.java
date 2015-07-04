package chess.movements;

import chess.enums.Coord;
import com.google.common.base.Preconditions;

/**
 * Created by Tom on 27.6.2015.
 */
public class Movement {

    protected final Coord from;
    protected final Coord to;
    private final MovementEffect movementEffect;
    private final MovementType type;

    public Movement(MovementType movementType, Coord from, Coord to, MovementEffect movementEffect) {
        Preconditions.checkNotNull(movementType);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(movementEffect);
        Preconditions.checkArgument((movementType == MovementType.CAPTURE || movementType == MovementType.PROMOTION_CAPTURE) && movementEffect.getCaptured() != null);
        this.type = movementType;
        this.from = from;
        this.to = to;
        this.movementEffect = movementEffect;
    }

    public MovementType getType() {
        return type;
    }

    public MovementEffect getMovementEffect() {
        return movementEffect;
    }

    public Coord getFrom() {
        return from;
    }

    public Coord getTo() {
        return to;
    }

    public boolean sameCoordsAndTypeAs(Movement other) {
        if (other.getType() != getType()) {
            return false;
        }
        return other.from == from && other.to == to;
    }

}
