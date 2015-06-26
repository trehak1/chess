package chess.board;

import chess.Col;
import chess.Figure;
import chess.Row;
import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * Created by Tom on 26.6.2015.
 */
public class Board {

    private final Figure[][] board = new Figure[8][8];

    Board() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Figure.NONE);
        }
    }

    public Figure get(Col col, Row row) {
        return board[col.ordinal()][row.ordinal()];
    }

    void set(Col col, Row row, Figure figure) {
        Preconditions.checkArgument(get(col, row).equals(Figure.NONE));
        board[col.ordinal()][row.ordinal()] = figure;
    }

    void remove(Col col, Row row) {
        board[col.ordinal()][row.ordinal()] = Figure.NONE;
    }

}
