package chess.movements;

import chess.enums.Coord;
import chess.enums.Piece;
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
        if (movementType == MovementType.PROMOTION || movementType == MovementType.PROMOTION_CAPTURE) {
            if (movementEffect.getPromotedTo() == null) {
                throw new IllegalStateException("Movement type promotion or promotion capture and no promotion result set!");
            }
        }
        if (movementType == MovementType.CAPTURE || movementType == MovementType.PROMOTION_CAPTURE) {
            if (movementEffect.getCaptured() == null) {
                throw new IllegalStateException("Movement type capture or promotion capture and no capture piece set!");
            }
        }
        this.type = movementType;
        this.from = from;
        this.to = to;
        this.movementEffect = movementEffect;
    }

    public boolean isCheck() {
        return movementEffect.getCaptured() == Piece.KING;
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
