package chess.api;

import chess.game.Game;
import chess.game.GameFactory;
import com.google.common.io.BaseEncoding;

import java.util.Random;

public class Session {
    
    private final String id;
    private final String whiteKey;
    private final String blackKey;
    private Game game = GameFactory.newGame();

    private Session(String id, String whiteKey, String blackKey) {
        this.id = id;
        this.whiteKey = whiteKey;
        this.blackKey = blackKey;
    }

    public static Session createNew() {
        Session session = new Session(randomId(), randomId(), randomId());
        return session;
    }
    
    private static String randomId() {
        byte[] data = new byte[8];
        new Random().nextBytes(data);
        return BaseEncoding.base16().lowerCase().encode(data);
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
