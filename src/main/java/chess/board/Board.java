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

    final Figure[][] board = new Figure[8][8];
    final Coord enPassantAllowed;
    // white king, white queen, black king, black queen
    final boolean[] castlings;

    Board() {
        this.castlings = new boolean[]{true,true,true,true};
        this.enPassantAllowed = null;
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Figure.NONE);
        }
    }

    Board(boolean[] castlings, Figure[][] board, Coord enPassantAllowed) {
        // castling
        this.castlings = Arrays.copyOf(castlings,castlings.length);
        this.enPassantAllowed = enPassantAllowed;
        copyFigures(board);
    }

    private void copyFigures(Figure[][] board) {
        // figures
        for (int x = 0; x < board.length; x++) {
            System.arraycopy(board[x],0,this.board[x],0,board[x].length);
        }
    }

    public Board allowEnPassant(Col col, Row row) {
        Figure figure = get(col, row);
        Preconditions.checkArgument((figure == Figure.WHITE_PAWN && row == Row._4) || (figure == Figure.BLACK_PAWN && row == Row._5),"No pawn on requested coords");
        Board nb = new Board(castlings, board, Coord.get(col, row));
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

    private int castlingIndex(Player player, CastlingType castlingType) {
		int s;
		switch (player) {
			case WHITE:
				s = 0;
				break;
			case BLACK:
				s = 2;
				break;
			default:
				throw new IllegalArgumentException("wtf");
		}
		switch (castlingType) {
			case KING_SIDE:
				return s;
			case QUEEN_SIDE:
				return s+1;
			default:
				throw new IllegalArgumentException("wtf");
		}
	} 
	
    public boolean isCastlingEnabled(Player player, CastlingType castlingType) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castlingType);
      	return castlings[castlingIndex(player, castlingType)];
    }

    public Board disableCastling(Player player, CastlingType castlingType) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castlingType);
		int index = castlingIndex(player, castlingType);
		boolean[] copy = Arrays.copyOf(castlings, castlings.length);
		copy[index] = false;
		return new Board(copy, board, enPassantAllowed);
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
        Preconditions.checkArgument(get(col, row).equals(Figure.NONE),"There is a figure "+get(col, row)+" on target square "+Coord.get(col, row));
        Board clone = new Board(castlings, board, enPassantAllowed);
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
        Preconditions.checkArgument(get(col, row) != Figure.NONE, "Unable to remove nonexisting figure from "+Coord.get(col,row));
        Board clone = new Board(castlings, board, enPassantAllowed);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Board board1 = (Board) o;

		if (!Arrays.deepEquals(board, board1.board)) return false;
		if (enPassantAllowed != board1.enPassantAllowed) return false;
		return Arrays.equals(castlings, board1.castlings);

	}

	@Override
	public int hashCode() {
		int result = board != null ? Arrays.deepHashCode(board) : 0;
		result = 31 * result + (enPassantAllowed != null ? enPassantAllowed.hashCode() : 0);
		result = 31 * result + (castlings != null ? Arrays.hashCode(castlings) : 0);
		return result;
	}

	public Board clearEnPassant() {
        return new Board(castlings,board,null);
    }

	public EnumSet<CastlingType> getCastlingsEnabled(Player player) {
		Preconditions.checkNotNull(player);
		EnumSet<CastlingType> res = EnumSet.noneOf(CastlingType.class);
		if(isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
			res.add(CastlingType.QUEEN_SIDE);
		}
		if(isCastlingEnabled(player, CastlingType.KING_SIDE)) {
			res.add(CastlingType.KING_SIDE);
		}
		return res;
	}

	Board enableCastling(Player player, CastlingType castlingType) {
		int index = castlingIndex(player, castlingType);
		boolean[] copy = Arrays.copyOf(castlings, castlings.length);
		copy[index] = true;
		return new Board(copy,board,enPassantAllowed);
	}
}
