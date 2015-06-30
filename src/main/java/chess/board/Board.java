package chess.board;

import chess.enums.*;
import chess.movements.Castling;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by Tom on 26.6.2015.
 */
public class Board {

    private final Figure[][] board = new Figure[8][8];
    final EnumSet<Coord> enPassantAllowed;
    final EnumSet<Castling> whiteCastlingEnabled = EnumSet.allOf(Castling.class);
    final EnumSet<Castling> blackCastlingEnabled = EnumSet.allOf(Castling.class);

    Board() {
        this.enPassantAllowed = EnumSet.noneOf(Coord.class);
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Figure.NONE);
        }
    }

    Board(EnumSet<Castling> whiteCastlings, EnumSet<Castling> blackCastlings, Figure[][] board, EnumSet<Coord> enPassantAllowed) {
        // castling
        this.whiteCastlingEnabled.clear();
        this.whiteCastlingEnabled.addAll(whiteCastlings);
        this.blackCastlingEnabled.clear();
        this.blackCastlingEnabled.addAll(blackCastlings);
        this.enPassantAllowed = enPassantAllowed.clone();
        // figures
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                this.board[x][y] = board[x][y];
            }
        }
    }

    public Board allowEnPassant(Col col, Row row) {
        Figure figure = get(col, row);
        Preconditions.checkArgument((figure == Figure.WHITE_PAWN && row == Row._4) || (figure == Figure.BLACK_PAWN && row == Row._5));
        Board nb = new Board(whiteCastlingEnabled, blackCastlingEnabled, board, enPassantAllowed);
        nb.enPassantAllowed.add(Coord.get(col, row));
        return nb;
    }

    public Board allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        return allowEnPassant(coord.getCol(), coord.getRow());
    }

    public EnumSet<Coord> getEnPassantAllowed() {
        return enPassantAllowed.clone();
    }

    public boolean isEnPassantAllowed(Coord coord) {
        Preconditions.checkNotNull(coord);
        return isEnPassantAllowed(coord.getCol(), coord.getRow());
    }

    public boolean isEnPassantAllowed(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(col);
        return enPassantAllowed.contains(Coord.get(col, row));
    }

    public boolean isCastlingEnabled(Player player, Castling castling) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castling);
        switch (player) {
            case WHITE:
                return whiteCastlingEnabled.contains(castling);
            case BLACK:
                return blackCastlingEnabled.contains(castling);
            default:
                throw new IllegalArgumentException("wtf");
        }
    }

    public Board disableCastling(Player player, Castling castling) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castling);
        switch (player) {
            case WHITE:
                EnumSet<Castling> whiteCastlings = whiteCastlingEnabled.clone();
                whiteCastlings.remove(castling);
                Board b = new Board(whiteCastlings, this.blackCastlingEnabled, board, enPassantAllowed);
                return b;
            case BLACK:
                EnumSet<Castling> blackCastlings = blackCastlingEnabled.clone();
                blackCastlings.remove(castling);
                Board b1 = new Board(this.whiteCastlingEnabled, blackCastlings, board, enPassantAllowed);
                return b1;
            default:
                throw new IllegalStateException("wtf");
        }
    }

    public Figure get(Coord coord) {
        Preconditions.checkNotNull(coord);
        return get(coord.getCol(), coord.getRow());
    }

    public Figure get(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(col);
        return board[col.ordinal()][row.ordinal()];
    }

    public Board set(Coord coord, Figure figure) {
        Preconditions.checkNotNull(coord);
        return set(coord.getCol(), coord.getRow(), figure);
    }

    public Board set(Col col, Row row, Figure figure) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(figure);
        Preconditions.checkArgument(get(col, row).equals(Figure.NONE));
        Board clone = new Board(whiteCastlingEnabled, blackCastlingEnabled, board, enPassantAllowed);
        clone.board[col.ordinal()][row.ordinal()] = figure;
        return clone;
    }

    public Board remove(Coord coord) {
        Preconditions.checkNotNull(coord);
        return remove(coord.getCol(), coord.getRow());
    }

    public Board remove(Col col, Row row) {
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

        if (!Arrays.deepEquals(board, board1.board)) return false;
        if (enPassantAllowed != null ? !enPassantAllowed.equals(board1.enPassantAllowed) : board1.enPassantAllowed != null)
            return false;
        if (whiteCastlingEnabled != null ? !whiteCastlingEnabled.equals(board1.whiteCastlingEnabled) : board1.whiteCastlingEnabled != null)
            return false;
        return !(blackCastlingEnabled != null ? !blackCastlingEnabled.equals(board1.blackCastlingEnabled) : board1.blackCastlingEnabled != null);

    }

    @Override
    public int hashCode() {
        int result = board != null ? Arrays.deepHashCode(board) : 0;
        result = 31 * result + (enPassantAllowed != null ? enPassantAllowed.hashCode() : 0);
        result = 31 * result + (whiteCastlingEnabled != null ? whiteCastlingEnabled.hashCode() : 0);
        result = 31 * result + (blackCastlingEnabled != null ? blackCastlingEnabled.hashCode() : 0);
        return result;
    }


}
