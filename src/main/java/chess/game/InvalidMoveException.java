package chess.game;

public class InvalidMoveException extends Exception {

    private final MoveCommand moveCommand;

    public InvalidMoveException(MoveCommand moveCommand) {
        this.moveCommand = moveCommand;
    }

    public MoveCommand getMoveCommand() {
        return moveCommand;
    }
}
