package chess.board;

import com.google.gson.Gson;

public class BoardSerializer {
    
    public BoardSerializer() {
        
    }
    
    public String serialize(Board board) {
        Gson gson = new Gson();
        return gson.toJson(board);
    }
    
    public Board deserialize(String board) {
        Gson gson = new Gson();
        return gson.fromJson(board, Board.class);
    }
}
