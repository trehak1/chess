package chess.game;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.Movement;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final String id;
    private final List<Movement> movements = new ArrayList<>();

    public static final String randomId() {
        Random r = new Random();
        byte[] bytes = new byte[16];
        r.nextBytes(bytes);
        return BaseEncoding.base16().lowerCase().encode(bytes);
    }

    public Game(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getMovesWithoutCaptureOrPawnAdvanceInRow() {
        //throw new UnsupportedOperationException("not yet implemented");
        return 0;
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
        this.movements.add(movement);
    }

}
