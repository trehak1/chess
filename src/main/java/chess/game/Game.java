package chess.game;

import chess.board.Board;
import chess.enums.Player;
import chess.movements.Movement;
import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    
    private final Board currentBoard;
    private final List<Movement> movements = new ArrayList<>();
    private final int rule50MovesCounter;
    private final MoveCommand.SpecialCommand endingCommand;
    private final GameState gameState;
    
    Game(Board board, int rule50MovesCounter, GameState gameState, MoveCommand.SpecialCommand endingCommand, Movement... moves) {
        Preconditions.checkNotNull(board);
        Preconditions.checkArgument(rule50MovesCounter > -1);
        Preconditions.checkNotNull(gameState);
        this.endingCommand = endingCommand;
        this.currentBoard = board;
        this.rule50MovesCounter = rule50MovesCounter;
        this.gameState = gameState;
        for(Movement m : moves) {
            Preconditions.checkNotNull(m);
            movements.add(m);
        }
    }
    
    public int getRule50MovesCounter() {
        return rule50MovesCounter;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public List<Movement> getMovements() {
        return Collections.unmodifiableList(movements);
    }

    public GameState getGameState() {
        return gameState;
    }
}
