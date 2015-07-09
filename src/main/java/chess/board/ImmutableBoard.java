package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Tom on 26.6.2015.
 */
public class ImmutableBoard implements Board {

    private final BitBoard bitBoard;
    private final Coord enPassantAllowed;
    // white king, white queen, black king, black queen
    private final CastlingRights castlingRights;
    private final Player onTurn;

    ImmutableBoard() {
        this.castlingRights = new CastlingRights();
        this.bitBoard = new BitBoard();
        this.enPassantAllowed = null;
        this.onTurn = Player.WHITE;
    }

    private ImmutableBoard(CastlingRights castlingRights, Coord enPassantAllowed, Player onTurn, BitBoard bitBoard) {
        // castling
        this.castlingRights = castlingRights;
        this.enPassantAllowed = enPassantAllowed;
        this.onTurn = onTurn;
        this.bitBoard = bitBoard.copy();
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
        Board nb = new ImmutableBoard(castlingRights, coord, onTurn, bitBoard);
        return nb;
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
        return new ImmutableBoard(castlingRights.disableCastling(player, castlingType), enPassantAllowed, onTurn, bitBoard);
    }

    @Override
    public Figure get(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.isValid());
        return bitBoard.get(coord);
    }

    @Override
    public Board set(Coord coord, Figure figure) {
        ImmutableBoard clone = new ImmutableBoard(castlingRights, enPassantAllowed, onTurn, bitBoard);
        clone.bitBoard.set(coord, figure);
        return clone;
    }

    @Override
    public Board remove(Coord coord) {
        ImmutableBoard clone = new ImmutableBoard(castlingRights, enPassantAllowed, onTurn, bitBoard);
        clone.bitBoard.clear(coord);
        return clone;
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
        return new ImmutableBoard(castlingRights, enPassantAllowed, player, bitBoard);
    }

    @Override
    public Board clearEnPassant() {
        return new ImmutableBoard(castlingRights, null, onTurn, bitBoard);
    }

    @Override
    public Board enableCastling(Player player, CastlingType castlingType) {
        return new ImmutableBoard(castlingRights.enableCastling(player, castlingType), enPassantAllowed, onTurn, bitBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableBoard board = (ImmutableBoard) o;

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
        return bitBoard.copy();
    }

    @Override
    public void checkSanity() {
        // check number of pieces
        for(Figure f: EnumSet.complementOf(EnumSet.of(Figure.NONE))) {
            List<Coord> coords = locateAll(f);
            Preconditions.checkArgument(coords.size() <= 32,"too many pieces!");
        }
        // check single king
        Preconditions.checkArgument(locateAll(Figure.WHITE_KING).size() == 1,"Multiple white kings");
        Preconditions.checkArgument(locateAll(Figure.BLACK_KING).size() == 1,"Multiple black kings");
        // check castlings
        List<Coord> whiteRooks = locateAll(Figure.WHITE_ROOK);
        if(!whiteRooks.contains(Coord.A1)) {
            if(castlingRights.isCastlingEnabled(Player.WHITE,CastlingType.QUEEN_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        if(!whiteRooks.contains(Coord.H1)) {
            if(castlingRights.isCastlingEnabled(Player.WHITE,CastlingType.KING_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        List<Coord> blackRooks = locateAll(Figure.BLACK_ROOK);
        if(!blackRooks.contains(Coord.A8)) {
            if(castlingRights.isCastlingEnabled(Player.BLACK,CastlingType.QUEEN_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        if(!blackRooks.contains(Coord.H8)) {
            if(castlingRights.isCastlingEnabled(Player.BLACK,CastlingType.KING_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
    }

}
