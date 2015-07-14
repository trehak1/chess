package chess.game;

import chess.enums.Coord;
import com.google.common.base.Preconditions;

public class MoveCommand {
    
    public enum SpecialCommand {
        SURRENDER, CLAIM_DRAW, DRAW_AGREED
    }
    
    private final Coord from;
    private final Coord to;
    private final SpecialCommand specialCommand;

    public MoveCommand(Coord from, Coord to) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(from.isValid());
        Preconditions.checkArgument(to.isValid());
        this.from = from;
        this.to = to;
        specialCommand = null;
    }
    
    public MoveCommand(SpecialCommand specialCommand) {
        this.specialCommand = specialCommand;
        from = null;
        to = null;
    }

    public SpecialCommand getSpecialCommand() {
        return specialCommand;
    }

    public Coord getFrom() {
        return from;
    }
    
    public Coord getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "MoveCommand{" +
                "from=" + from +
                ", to=" + to +
                ", specialCommand=" + specialCommand +
                '}';
    }
}
