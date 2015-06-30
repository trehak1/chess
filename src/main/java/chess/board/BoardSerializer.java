package chess.board;

import chess.enums.Col;
import chess.enums.Figure;
import chess.enums.Row;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sun.tools.javac.util.Assert;

import java.util.Arrays;

public class BoardSerializer {
    
    public BoardSerializer() {
        
    }
    
    public String serializeIntoJson(Board board) {
        Gson gson = new Gson();
        return gson.toJson(board);
    }
    
    public Board deserializeFromJson(String board) {
        Gson gson = new Gson();
        return gson.fromJson(board, Board.class);
    }

    /**
     * Serializes the board into matrix 8x8 of chess pieces using UTF-8 symbols.
     * The A1 position is in the bottom-left corner.
     * @param board
     * @return
     */
    public String serializeIntoUtf8(Board board) {
        StringBuilder sb = new StringBuilder();
        Row[] reversedRows = Lists.reverse(Arrays.asList(Row.values())).toArray(new Row[9]);
        for (Row r : reversedRows) {
            if (r == Row.INVALID) {
                continue;
            }
            for (Col c : Col.values()) {
                if (c == Col.INVALID) {
                    continue;
                }
                Figure figure = board.get(c, r);
                sb.append(figure.getUtf8Char());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    public Board deserializeFromUtf8(String s) {
        Board board = new Board();
        String[] lines = s.split("\n");
        Assert.check(lines.length == 8);
        Row r = Row._8;
        for (String line : lines) {
            Assert.check(line.replace("\n", "").length() == 8);
            Col c = Col.A;
            for (int i = 0; i < 8; i++) {
                board = board.set(c, r, Figure.fromUtf8Char(line.charAt(i)));
                c = c.east();
            }
            r = r.south();
        }
        return board;
    }
}
