package chess.board;

import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Row;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by Tom on 26.6.2015.
 */
public class Board {

    private final Figure[][] board = new Figure[8][8];
    private final EnumSet<Coord> enPassantAllowed;
    private final boolean whiteCastlingEnabled;
    private final boolean blackCastlingEnabled;

    Board() {
        this.blackCastlingEnabled = true;
        this.whiteCastlingEnabled = true;
        this.enPassantAllowed = EnumSet.noneOf(Coord.class);
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Figure.NONE);
        }
    }

    Board(boolean whiteCastlingEnabled, boolean blackCastlingEnabled, Figure[][] board, EnumSet<Coord> enPassantAllowed) {
        // castling
        this.blackCastlingEnabled = blackCastlingEnabled;
        this.whiteCastlingEnabled = whiteCastlingEnabled;
        this.enPassantAllowed = enPassantAllowed.clone();
        // figures
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                this.board[x][y] = board[x][y];
            }
        }
    }

    Board allowEnPassant(Col col, Row row) {
        Figure figure = get(col, row);
        Preconditions.checkArgument((figure == Figure.WHITE_PAWN && row == Row._4) || (figure == Figure.BLACK_PAWN && row == Row._5));
        Board nb = new Board(whiteCastlingEnabled, blackCastlingEnabled, board, enPassantAllowed);
        nb.enPassantAllowed.add(Coord.get(col, row));
        return nb;
    }

    public EnumSet<Coord> getEnPassantAllowed() {
        return enPassantAllowed.clone();
    }

    public boolean isEnPassantAllowed(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(col);
        return enPassantAllowed.contains(Coord.get(col, row));
    }

    public boolean isWhiteCastlingEnabled() {
        return whiteCastlingEnabled;
    }

    public boolean isBlackCastlingEnabled() {
        return blackCastlingEnabled;
    }

    Board disableWhiteCastling() {
        return new Board(false, blackCastlingEnabled, board, enPassantAllowed);
    }

    Board disableBlackCastling() {
        return new Board(whiteCastlingEnabled, false, board, enPassantAllowed);
    }

    public Figure get(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(col);
        return board[col.ordinal()][row.ordinal()];
    }

    Board set(Col col, Row row, Figure figure) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(figure);
        Preconditions.checkArgument(get(col, row).equals(Figure.NONE));
        Board clone = new Board(whiteCastlingEnabled, blackCastlingEnabled, board, enPassantAllowed);
        clone.board[col.ordinal()][row.ordinal()] = figure;
        return clone;
    }

    Board remove(Col col, Row row) {
        Preconditions.checkNotNull(col, "Null col");
        Preconditions.checkNotNull(row, "Null row");
        Preconditions.checkArgument(get(col, row) != Figure.NONE, "Unable to remove nonexisting figure");
        Board clone = new Board(whiteCastlingEnabled, blackCastlingEnabled, board, enPassantAllowed);
        clone.board[col.ordinal()][row.ordinal()] = Figure.NONE;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        if (whiteCastlingEnabled != board1.whiteCastlingEnabled) return false;
        if (blackCastlingEnabled != board1.blackCastlingEnabled) return false;
        if (!Arrays.deepEquals(board, board1.board)) return false;
        return !(enPassantAllowed != null ? !enPassantAllowed.equals(board1.enPassantAllowed) : board1.enPassantAllowed != null);

    }

    @Override
    public int hashCode() {
        int result = board != null ? Arrays.deepHashCode(board) : 0;
        result = 31 * result + (enPassantAllowed != null ? enPassantAllowed.hashCode() : 0);
        result = 31 * result + (whiteCastlingEnabled ? 1 : 0);
        result = 31 * result + (blackCastlingEnabled ? 1 : 0);
        return result;
    }

    // TODO test this!
    public Board mirror() {
        return new Board(whiteCastlingEnabled, blackCastlingEnabled, mirrorBoard(board), enPassantAllowed);
    }

    private Figure[][] mirrorBoard(Figure[][] board) {
        Figure[][] nb = new Figure[8][8];
        for(Col c : Col.values()) {
            for(Row r : Row.values()) {
                
            }
        }
        return nb;
    }
}
