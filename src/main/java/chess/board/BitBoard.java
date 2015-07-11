package chess.board;

import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Piece;
import chess.enums.Player;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Faster representation of 8x8 figure board
 */
public class BitBoard {

    // 8*8 board * # of distinct pieces
    // index as in Piece enum
    // first 64 = WHITE_PAWN
    // second 64 = WHITE_ROOK
    // ...
    // 6th 64 = WHITE_KNIGHT
    // 7th 64 = BLACK_PAWN
    // ...
    // 12th 64 = BLACK_KNIGHT
    // 8 * 8 * players * pieces
    private final BitSet board;

    public BitBoard() {
        this(new BitSet(8 * 8 * 2 * 6));
    }

    public BitBoard(BitSet board) {
        this.board = board;
    }

    public void set(Coord coord, Figure figure) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        Preconditions.checkNotNull(figure);
        Preconditions.checkArgument(get(coord) == Figure.NONE);
        board.set(getIndex(coord, figure.getPlayer(), figure.getPiece()));
    }

    private int getIndex(Coord coord, Player player, Piece piece) {
        int coordIndex = coord.ordinal();
        int playerShift = player.ordinal() * 8 * 8 * 6;
        int pieceShift = piece.ordinal();
        int index = playerShift + (pieceShift * 8 * 8) + coordIndex;
        return index;
    }

    public Figure get(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        int coordIndex = coord.ordinal();
        for (int i = 0; i < 12; i++) {
            if (board.get((64 * i) + coordIndex)) {
                Player pl = i < 6 ? Player.WHITE : Player.BLACK;
                Piece pi = Piece.get(i % 6);
                return Figure.get(pl, pi);
            }
        }
        return Figure.NONE;
    }

    public void clear(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        Figure figure = get(coord);
        if (figure == Figure.NONE) {
            throw new IllegalArgumentException("wtf");
        }
        board.clear(getIndex(coord, figure.getPlayer(), figure.getPiece()));
    }

    public BitBoard copy() {
        return new BitBoard(BitSet.valueOf(board.toLongArray()));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitBoard bitBoard = (BitBoard) o;

        return !(board != null ? !board.equals(bitBoard.board) : bitBoard.board != null);

    }

    @Override
    public int hashCode() {
        return board != null ? board.hashCode() : 0;
    }

    private int getPageStart(Figure figure) {
        int playerShift = figure.getPlayer().ordinal() * 8 * 8 * 6;
        int pieceShift = figure.getPiece().ordinal();
        int pageStart = playerShift + (pieceShift * 8 * 8);
        return pageStart;
    }

    public Set<Coord> getAll(Figure figure) {
        int pageStart = getPageStart(figure);
        Set<Coord> res = EnumSet.noneOf(Coord.class);
        for (int i = 0; i < Coord.VALID_VALUES.size(); i++) {
            if (board.get(pageStart + i)) {
                res.add(Coord.get(i));
            }
        }
        return res;
    }
}
