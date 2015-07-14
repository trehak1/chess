package chess.game;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementExecutor;
import chess.movements.MovementFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameFactory {

    private static final Function<Movement, MoveCommand> TO_COMMAND = (m) -> {
        Preconditions.checkNotNull(m);
        return new MoveCommand(m.getFrom(), m.getTo());
    };
    private final Game game;

    public GameFactory(Game game) {
        Preconditions.checkNotNull(game);
        this.game = game;
    }

    public static Game newGame() {
        return new Game(new BoardFactory().newGameBoard(), 0, GameState.IN_PROGRESS, null);
    }

    public boolean isValid(MoveCommand moveCommand) {
        if (game.getGameState() != GameState.IN_PROGRESS) {
            return false;
        }
        return findCommand(moveCommand, getPossibleMoves()) != null;
    }
    
    private boolean isValid(MoveCommand moveCommand, List<Movement> movements) {
        if (game.getGameState() != GameState.IN_PROGRESS) {
            return false;
        }
        return findCommand(moveCommand, movements) != null;
    }

    public Game move(MoveCommand moveCommand) throws InvalidMoveException {
        Preconditions.checkNotNull(moveCommand);
        if (moveCommand.getSpecialCommand() != null) {
            return specialCommand(moveCommand);
        }
        // generate possible moves
        List<Movement> possibleMoves = getPossibleMoves();
        // no more legal moves - should never happen
        if (possibleMoves.isEmpty()) {
            throw new IllegalStateException("wtf");
        }
        // is this command valid ?
        if (!isValid(moveCommand, possibleMoves)) {
            throw new InvalidMoveException(moveCommand);
        }
        // locate movement from move command
        Movement movement = findCommand(moveCommand, possibleMoves);
        // do move
        // 1. mutated board
        Board mutatedBoard = MovementExecutor.move(game.getCurrentBoard(), movement);
        GameState state = checkEnding(mutatedBoard, possibleMoves);
        // 2. put move to movement history
        List<Movement> moves = Lists.newArrayList(game.getMovements());
        moves.add(movement);
        // 3. check and modify 50 movement rule
        int rule50 = evaluateRule50(game.getRule50MovesCounter(), movement);
        return new Game(mutatedBoard, rule50, state, null, moves.toArray(new Movement[0]));
    }

    private GameState checkEnding(Board mutated, List<Movement> playerMoves) {
        Player enemy = game.getCurrentBoard().getPlayerOnTurn().enemy();
        List<Movement> moves = MovementFactory.getFor(enemy).getMoves(mutated);
        if (!moves.isEmpty()) {
            return game.getGameState();
        } else {
            if (Iterables.any(playerMoves, (m) -> m.getMovementEffect().getCaptured() == Piece.KING)) {
                return game.getCurrentBoard().getPlayerOnTurn() == Player.WHITE ? GameState.WHITE_WON : GameState.BLACK_WON;
            } else {
                return GameState.DRAW;
            }
        }
    }

    private Game specialCommand(MoveCommand moveCommand) throws InvalidMoveException {
        Preconditions.checkNotNull(moveCommand.getSpecialCommand());
        Player player = game.getCurrentBoard().getPlayerOnTurn();
        if (moveCommand.getSpecialCommand() == MoveCommand.SpecialCommand.SURRENDER) {
            GameState state = player == Player.WHITE ? GameState.BLACK_WON : GameState.WHITE_WON;
            return new Game(game.getCurrentBoard(), game.getRule50MovesCounter(), state, moveCommand.getSpecialCommand(), game.getMovements().toArray(new Movement[0]));
        } else if (moveCommand.getSpecialCommand() == MoveCommand.SpecialCommand.CLAIM_DRAW) {
            // 50 rules draw
            if (rule50Applies() || repetitionOf3Applies()) {
                return new Game(game.getCurrentBoard(), game.getRule50MovesCounter(), GameState.DRAW, moveCommand.getSpecialCommand(), game.getMovements().toArray(new Movement[0]));
            }
        } else if (moveCommand.getSpecialCommand() == MoveCommand.SpecialCommand.DRAW_AGREED) {
            return new Game(game.getCurrentBoard(), game.getRule50MovesCounter(), GameState.DRAW, moveCommand.getSpecialCommand(), game.getMovements().toArray(new Movement[0]));
        }
        throw new InvalidMoveException(moveCommand);
    }

    private int evaluateRule50(int rule50MovesCounter, Movement movement) {
        if (game.getCurrentBoard().get(movement.getFrom()).getPiece() == Piece.PAWN) {
            return 0;
        }
        if (movement.getMovementEffect().getCaptured() != null) {
            return 0;
        }
        return rule50MovesCounter++;
    }

    private boolean repetitionOf3Applies() {
        // TODO implement 3 fold repetition rule
        return false;
    }

    private boolean rule50Applies() {
        return game.getRule50MovesCounter() >= 50;
    }

    private Movement findCommand(MoveCommand command, List<Movement> movements) {
        for (Movement m : movements) {
            if (m.getFrom() == command.getFrom()) {
                if (m.getTo() == command.getTo()) {
                    return m;
                }
            }
        }
        return null;
    }

    public List<MoveCommand> getPossibleMoveCommands() {
        return getPossibleMoves().stream().map(TO_COMMAND).collect(Collectors.toList());
    }

    public List<Movement> getPossibleMoves() {
        MovementFactory factory = MovementFactory.getFor(game.getCurrentBoard().getPlayerOnTurn());
        List<Movement> moves = factory.getMoves(game.getCurrentBoard());
        return moves;
    }

}
