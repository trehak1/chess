package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tom on 26.6.2015.
 */
public class Board {

	private final BitBoard bitBoard;
	private final Coord enPassantAllowed;
	// white king, white queen, black king, black queen
	private final CastlingRights castlingRights;
	private final Player onTurn;

	Board() {
		this.castlingRights = new CastlingRights();
		this.bitBoard = new BitBoard();
		this.enPassantAllowed = null;
		this.onTurn = Player.WHITE;
	}

	private Board(CastlingRights castlingRights, Coord enPassantAllowed, Player onTurn, BitBoard bitBoard) {
		// castling
		this.castlingRights = castlingRights.clone();
		this.enPassantAllowed = enPassantAllowed;
		this.onTurn = onTurn;
		this.bitBoard = bitBoard.copy();
	}

	public Player getOnTurn() {
		return onTurn;
	}
	
	public Board allowEnPassant(Coord coord) {
		Preconditions.checkNotNull(coord);
		Preconditions.checkArgument(coord.isValid());
		Figure figure = get(coord);
		Preconditions.checkArgument(get(coord).getPiece() == Piece.PAWN, "No PAWN on %s", coord);
		if(figure.getPlayer() == Player.WHITE && coord.getRow() != Row._4) {
			throw new IllegalArgumentException("Unsuitable row "+coord.getRow()+" for white en passant");
		}
		if(figure.getPlayer() == Player.BLACK && coord.getRow() != Row._5) {
			throw new IllegalArgumentException("Unsuitable row "+coord.getRow()+" for black en passant");
		}
		Board nb = new Board(castlingRights, coord, onTurn, bitBoard);
		return nb;
	}

	public Coord getEnPassantAllowed() {
		return enPassantAllowed;
	}

	public boolean isEnPassantAllowed(Coord coord) {
		Preconditions.checkNotNull(coord);
		Preconditions.checkArgument(coord.isValid());
		return enPassantAllowed == coord;
	}
	
	public CastlingRights getCastlingRights() {
		return castlingRights;
	}

	public Board disableCastling(Player player, CastlingType castlingType) {
		return new Board(castlingRights.disableCastling(player, castlingType), enPassantAllowed, onTurn, bitBoard);
	}

	public Figure get(Coord coord) {
		Preconditions.checkNotNull(coord);
		Preconditions.checkArgument(coord.isValid());
		return bitBoard.get(coord);
	}
	
	public Board set(Coord coord, Figure figure) {
		Board clone = new Board(castlingRights, enPassantAllowed, onTurn, bitBoard);
		clone.bitBoard.set(coord, figure);
		return clone;
	}

	public Board remove(Coord coord) {
		Board clone = new Board(castlingRights, enPassantAllowed, onTurn, bitBoard);
		clone.bitBoard.clear(coord);
		return clone;
	}
	
	public boolean isEmpty(Coord coord) {
		Preconditions.checkNotNull(coord);
		Preconditions.checkArgument(coord.isValid());
		return get(coord) == Figure.NONE;
	}

	public Board setOnTurn(Player player) {
		Preconditions.checkNotNull(player);
		return new Board(castlingRights, enPassantAllowed, player, bitBoard);
	}

	public Board clearEnPassant() {
		return new Board(castlingRights, null, onTurn, bitBoard);
	}

	public Board enableCastling(Player player, CastlingType castlingType) {
		return new Board(castlingRights.enableCastling(player, castlingType), enPassantAllowed, onTurn, bitBoard);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Board board = (Board) o;

		if (bitBoard != null ? !bitBoard.equals(board.bitBoard) : board.bitBoard != null) return false;
		if (enPassantAllowed != board.enPassantAllowed) return false;
		if (castlingRights != null ? !castlingRights.equals(board.castlingRights) : board.castlingRights != null)
			return false;
		return onTurn == board.onTurn;

	}

	@Override
	public int hashCode() {
		int result = bitBoard != null ? bitBoard.hashCode() : 0;
		result = 31 * result + (enPassantAllowed != null ? enPassantAllowed.hashCode() : 0);
		result = 31 * result + (castlingRights != null ? castlingRights.hashCode() : 0);
		result = 31 * result + (onTurn != null ? onTurn.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new BoardSerializer().serializeIntoUtf8(this);
	}

	public List<Coord> locateAll(Figure figure) {
		return bitBoard.getAll(figure);
	}
}
