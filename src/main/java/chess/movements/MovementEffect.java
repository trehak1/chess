package chess.movements;

import chess.board.CastlingRights;
import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Piece;
import chess.enums.Player;
import com.google.common.base.Preconditions;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementEffect {


    public static final MovementEffect NONE = new MovementEffect();
    private final CastlingRights castlingRights;
    private final Coord allowEnPassant;
    private final Piece promotedTo;
    private final Piece captured;

    private MovementEffect(Coord allowEnPassant, Piece promotedTo, CastlingRights castlingRights, Piece captured) {
        this.allowEnPassant = allowEnPassant;
        this.promotedTo = promotedTo;
        this.castlingRights = castlingRights;
        this.captured = captured;
    }

    public MovementEffect() {
        this(null, null, new CastlingRights(), null);
    }

    public CastlingRights getDisableCastlings() {
        return castlingRights;
    }

    public Coord getAllowEnPassant() {
        return allowEnPassant;
    }

    public Piece getPromotedTo() {
        return promotedTo;
    }

    public Piece getCaptured() {
        return captured;
    }

    public MovementEffect allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        return new MovementEffect(coord, promotedTo, castlingRights, captured);
    }

    public MovementEffect disableCastling(CastlingType castlingType, Player player) {
        Preconditions.checkNotNull(castlingType);
        MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights.disableCastling(player, castlingType), captured);
        return me;
    }

    public MovementEffect promotedTo(Piece piece) {
        Preconditions.checkNotNull(piece);
        MovementEffect me = new MovementEffect(allowEnPassant, piece, castlingRights, captured);
        return me;
    }

    public MovementEffect captured(Piece captured) {
        Preconditions.checkNotNull(captured);
        MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights, captured);
        return me;
    }

}
