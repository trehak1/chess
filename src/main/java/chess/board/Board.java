package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * Created by Tom on 26.6.2015.
 */
public class Board {

    private final Figure[][] board = new Figure[8][8];
    private final Coord enPassantAllowed;
    // white king, white queen, black king, black queen
    private final CastlingRights castlingRights;
    private final Player onTurn;

    Board() {
        this.castlingRights = new CastlingRights();
        this.enPassantAllowed = null;
        this.onTurn = Player.WHITE;
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Figure.NONE);
        }
    }

    private Board(CastlingRights castlingRights, Figure[][] board, Coord enPassantAllowed, Player onTurn) {
        // castling
        this.castlingRights = castlingRights.clone();
        this.enPassantAllowed = enPassantAllowed;
        this.onTurn = onTurn;
        copyFigures(board);
    }

    public Player getOnTurn() {
        return onTurn;
    }

    private void copyFigures(Figure[][] board) {
        // figures
        for (int x = 0; x < board.length; x++) {
            System.arraycopy(board[x], 0, this.board[x], 0, board[x].length);
        }
    }

    public Board allowEnPassant(Col col, Row row) {
        Figure figure = get(col, row);
        Preconditions.checkArgument((figure == Figure.WHITE_PAWN && row == Row._4) || (figure == Figure.BLACK_PAWN && row == Row._5), "No pawn on requested coords");
        Board nb = new Board(castlingRights, board, Coord.get(col, row), onTurn);
        return nb;
    }

    public Board allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        return allowEnPassant(coord.getCol(), coord.getRow());
    }

    public Coord getEnPassantAllowed() {
        return enPassantAllowed;
    }

    public boolean isEnPassantAllowed(Coord coord) {
        Preconditions.checkNotNull(coord);
        return isEnPassantAllowed(coord.getCol(), coord.getRow());
    }

    public boolean isEnPassantAllowed(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(col);
        return enPassantAllowed == Coord.get(col, row);
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public Board disableCastling(Player player, CastlingType castlingType) {
        return new Board(castlingRights.disableCastling(player, castlingType), board, enPassantAllowed, onTurn);
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
        Preconditions.checkArgument(get(col, row).equals(Figure.NONE), "There is a figure " + get(col, row) + " on target square " + Coord.get(col, row));
        Board clone = new Board(castlingRights, board, enPassantAllowed, onTurn);
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
        Preconditions.checkArgument(get(col, row) != Figure.NONE, "Unable to remove nonexisting figure from " + Coord.get(col, row));
        Board clone = new Board(castlingRights, board, enPassantAllowed, onTurn);
        clone.board[col.ordinal()][row.ordinal()] = Figure.NONE;
        return clone;
    }

    public Figure[][] getBoard() {
        Figure[][] copy = new Figure[board.length][board[0].length];
        for (int i = 0; i < copy.length; i++) {
            for (int x = 0; x < copy[i].length; x++) {
                copy[i][x] = board[i][x];
            }
        }
        return copy;
    }

    public Board setOnTurn(Player player) {
        Preconditions.checkNotNull(player);
        return new Board(castlingRights, board, enPassantAllowed, player);
    }

    public Board clearEnPassant() {
        return new Board(castlingRights, board, null, onTurn);
    }

    Board enableCastling(Player player, CastlingType castlingType) {
        return new Board(castlingRights.enableCastling(player, castlingType), board, enPassantAllowed, onTurn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        if (!Arrays.deepEquals(board, board1.board)) return false;
        if (enPassantAllowed != board1.enPassantAllowed) return false;
        if (castlingRights != null ? !castlingRights.equals(board1.castlingRights) : board1.castlingRights != null)
            return false;
        return onTurn == board1.onTurn;

    }

    @Override
    public int hashCode() {
        int result = board != null ? Arrays.deepHashCode(board) : 0;
        result = 31 * result + (enPassantAllowed != null ? enPassantAllowed.hashCode() : 0);
        result = 31 * result + (castlingRights != null ? castlingRights.hashCode() : 0);
        result = 31 * result + (onTurn != null ? onTurn.hashCode() : 0);
        return result;
    }
}
