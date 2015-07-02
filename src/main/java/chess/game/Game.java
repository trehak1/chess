package chess.game;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.Capture;
import chess.movements.Movement;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    public enum GameState {
        IN_PROGRESS, DRAW, WHITE_WON, BLACK_WON
    }

    private final String id;
    private final List<Movement> movements = new ArrayList<>();
    private int movesWithoutCaptureOrAdvance = 0;
    private GameState gameState;

    public static final String randomId() {
        Random r = new Random();
        byte[] bytes = new byte[16];
        r.nextBytes(bytes);
        return BaseEncoding.base16().lowerCase().encode(bytes);
    }

    public Game(String id) {
        this.id = id;
        this.gameState = GameState.IN_PROGRESS;
    }

    public String getId() {
        return id;
    }

    public int getMovesWithoutCaptureOrPawnAdvanceInRow() {
        return movesWithoutCaptureOrAdvance;
    }

    public Board getCurrentBoard() {
        if (movements.isEmpty()) {
            return new BoardFactory().newGameBoard();
        } else {
            return movements.get(movements.size() - 1).getResultingBoard();
        }
    }

    public Player getPlayerOnTurn() {
        if (movements.isEmpty()) {
            return Player.WHITE;
        } else {
            return movements.size() % 2 == 0 ? Player.WHITE : Player.BLACK;
        }
    }

    public void addMovement(Movement movement) {
        checkRule50(movement);
        this.movements.add(movement);
    }

    private void checkRule50(Movement movement) {
        if (movement instanceof Capture || isPawnMove(movement)) {
            movesWithoutCaptureOrAdvance = 0;
        }
    }

    private boolean isPawnMove(Movement movement) {
        // first move in game
        if (movements.isEmpty()) {
            return movement.getResultingBoard().get(movement.getFrom()).getPiece() == Piece.PAWN;
        }
        // was there pawn ?
        return movements.get(movements.size() - 1).getResultingBoard().get(movement.getFrom()).getPiece() == Piece.PAWN;
    }

}
