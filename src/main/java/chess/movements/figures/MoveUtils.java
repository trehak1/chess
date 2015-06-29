package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Figure;
import chess.enums.Player;
import chess.enums.Row;

public class MoveUtils {

    private final Board board;
    private final Col col;
    private final Row row;
    private final Player player;

    public MoveUtils(Board board, Col col, Row row) {
        this.board = board;
        this.col = col;
        this.row = row;
        this.player = board.get(col, row).getPlayer();
    }

    public boolean isEmpty(Col col, Row row) {
        return board.get(col, row) == Figure.NONE;
    }

    public boolean isEnemy(Col col, Row row) {
        return board.get(col, row) != Figure.NONE && board.get(col, row).getPlayer() != player;
    }

}
