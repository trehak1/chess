package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by Tom on 26.6.2015.
 */
public class MutableBoard implements Board {

    private final BitBoard bitBoard;
    private Coord enPassantAllowed;
    // white king, white queen, black king, black queen
    private CastlingRights castlingRights;
    private Player onTurn;

    MutableBoard() {
        this(new CastlingRights(), new BitBoard(), null, Player.WHITE);
    }

    private MutableBoard(CastlingRights castlingRights, BitBoard bitBoard, Coord enPassantAllowed, Player player) {
        this.castlingRights = castlingRights;
        this.bitBoard = bitBoard;
        this.enPassantAllowed = enPassantAllowed;
        this.onTurn = player;
    }

    @Override
    public Player getPlayerOnTurn() {
        return onTurn;
    }

    @Override
    public Board allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        Figure figure = get(coord);
        Preconditions.checkArgument(get(coord).getPiece() == Piece.PAWN, "No PAWN on %s", coord);
        if (figure.getPlayer() == Player.WHITE && coord.getRow() != Row._4) {
            throw new IllegalArgumentException("Unsuitable row " + coord.getRow() + " for white en passant");
        }
        if (figure.getPlayer() == Player.BLACK && coord.getRow() != Row._5) {
            throw new IllegalArgumentException("Unsuitable row " + coord.getRow() + " for black en passant");
        }
        this.enPassantAllowed = coord;
        return this;
    }

    @Override
    public Coord getEnPassantAllowed() {
        return enPassantAllowed;
    }

    @Override
    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    @Override
    public Board disableCastling(Player player, CastlingType castlingType) {
        this.castlingRights = this.castlingRights.disableCastling(player, castlingType);
        return this;
    }

    @Override
    public Figure get(Coord coord) {
        return bitBoard.get(coord);
    }

    @Override
    public Board set(Coord coord, Figure figure) {
        bitBoard.set(coord, figure);
        return this;
    }

    @Override
    public Board remove(Coord coord) {
        bitBoard.clear(coord);
        return this;
    }

    @Override
    public boolean isEmpty(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        return get(coord) == Figure.NONE;
    }

    @Override
    public Board setOnTurn(Player player) {
        Preconditions.checkNotNull(player);
        this.onTurn = player;
        return null;
    }

    @Override
    public Board clearEnPassant() {
        this.enPassantAllowed = null;
        return this;
    }

    @Override
    public Board enableCastling(Player player, CastlingType castlingType) {
        this.castlingRights = castlingRights.enableCastling(player, castlingType);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MutableBoard board = (MutableBoard) o;

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

    @Override
    public List<Coord> locateAll(Figure figure) {
        return bitBoard.getAll(figure);
    }

    @Override
    public BitBoard getBitBoard() {
        return bitBoard;
    }

    @Override
    public void checkSanity() {

    }

    public static Board from(Board board) {
        MutableBoard mb = new MutableBoard(board.getCastlingRights(), board.getBitBoard(), board.getEnPassantAllowed(), board.getPlayerOnTurn());
        return mb;
    }
}
