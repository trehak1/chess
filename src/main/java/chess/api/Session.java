package chess.api;

import chess.enums.Player;
import chess.game.Game;
import chess.game.GameFactory;
import chess.game.InvalidMoveException;
import chess.game.MoveCommand;
import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;

import java.util.Random;

public class Session {

    private static final int RANDOM_ID_LENGTH = 32;
    private final String id;
    private final String whiteKey;
    private final String blackKey;
    private final boolean isSecured;
    private Game game = GameFactory.newGame();

    public void executeMoveCommand(MoveCommand moveCommand) throws InvalidMoveException {
        GameFactory gf = new GameFactory(game);
        this.game = gf.move(moveCommand);
    }

    public static class MoveCommandEvaluation {
        private final MoveCommand moveCommand;
        private final boolean authorized;
        private final boolean valid;

        public MoveCommandEvaluation(MoveCommand moveCommand, boolean authorized, boolean valid) {
            this.moveCommand = moveCommand;
            this.authorized = authorized;
            this.valid = valid;
        }

        public MoveCommand getMoveCommand() {
            return moveCommand;
        }

        public boolean isAuthorized() {
            return authorized;
        }

        public boolean isValid() {
            return valid;
        }
    }

    private Session(String id, String whiteKey, String blackKey, boolean isSecured) {
        this.id = id;
        this.whiteKey = whiteKey;
        this.blackKey = blackKey;
        this.isSecured = isSecured;
    }

    public MoveCommandEvaluation evaluateMoveCommand(MoveCommand moveCommand, String key) {
        Preconditions.checkNotNull(moveCommand);
        Preconditions.checkNotNull(key);
        // check is valid
        GameFactory gf = new GameFactory(game);
        if (!gf.isValid(moveCommand)) {
            return new MoveCommandEvaluation(moveCommand, true, false);
        }
        // check authorization
        Player player = game.getCurrentBoard().get(moveCommand.getFrom()).getPlayer();
        Player playerOnTurn = game.getCurrentBoard().getPlayerOnTurn();
        if (player == playerOnTurn) {
            if (!isSecured) {
                return new MoveCommandEvaluation(moveCommand, true, true);
            }
            key = key.trim().toLowerCase();
            if (player == Player.WHITE && key == whiteKey) {
                return new MoveCommandEvaluation(moveCommand, true, true);
            } else if (player == Player.BLACK && key == blackKey) {
                return new MoveCommandEvaluation(moveCommand, true, true);
            } else {
                return new MoveCommandEvaluation(moveCommand, false, false);
            }
        } else {
            return new MoveCommandEvaluation(moveCommand, false, false);
        }
    }


    public static Session createNew(boolean isSecured) {
        Session session = new Session(randomId(), randomId(), randomId(), isSecured);
        return session;
    }

    private static String randomId() {
        byte[] data = new byte[RANDOM_ID_LENGTH];
        new Random().nextBytes(data);
        return BaseEncoding.base64Url().omitPadding().encode(data);
    }

    public String getId() {
        return id;
    }

    String getWhiteKey() {
        return whiteKey;
    }

    String getBlackKey() {
        return blackKey;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
