package chess.api;

import chess.game.MoveCommand;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private final Map<String, Object> items = new HashMap<>();

    private Response() {
    }
    
    private static Response withItem(String itemName, Object val) {
        Preconditions.checkNotNull(itemName);
        Preconditions.checkNotNull(val);
        Response r = new Response();
        r.items.put(itemName, val);
        return r;
    }
    
    public static Response gameNotFound() {
        return error("Game not found");
    }

    public static Response ok(Session session) {
        return withItem("session", session);
    }

    public static Response error(String errorMsg) {
        return withItem("errorMsg", errorMsg);
    }

    public Map<String, Object> getItems() {
        return Maps.newHashMap(items);
    }

    public static Response availableCommands(List<MoveCommand> availableCommands) {
        return withItem("availableMoves", availableCommands);
    }
}
