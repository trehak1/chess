package chess.movements;

import chess.board.Board;
import chess.board.CastlingRights;
import chess.enums.*;
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
    private final Coord disableEnPassant;

    private MovementEffect(Coord allowEnPassant, Piece promotedTo, CastlingRights castlingRights, Piece captured, Coord disableEnPassant) {
        this.allowEnPassant = allowEnPassant;
        this.promotedTo = promotedTo;
        this.castlingRights = castlingRights;
        this.captured = captured;
        this.disableEnPassant = disableEnPassant;
    }

    public MovementEffect() {
        this(null, null, new CastlingRights(), null, null);
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

    public Coord getDisableEnPassant() {
        return disableEnPassant;
    }

    public MovementEffect allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        Preconditions.checkArgument(coord.getRow() == Row._4 || coord.getRow() == Row._5,"Row not suitable for en passant");
        return new MovementEffect(coord, promotedTo, castlingRights, captured, disableEnPassant);
    }

    public MovementEffect disableCastlingIfAllowed(Board board, CastlingType castlingType, Player player) {
        Preconditions.checkNotNull(castlingType);
        Preconditions.checkArgument(castlingRights.isCastlingEnabled(player,castlingType));
        Preconditions.checkNotNull(board);
        if(board.getCastlingRights().isCastlingEnabled(player,castlingType)) {
            MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights.disableCastling(player, castlingType), captured, disableEnPassant);
            return me;
        } else {
            return this;
        }
    }

    public MovementEffect promotedTo(Piece piece) {
        Preconditions.checkNotNull(piece);
        MovementEffect me = new MovementEffect(allowEnPassant, piece, castlingRights, captured, disableEnPassant);
        return me;
    }

    public MovementEffect captured(Piece captured) {
        Preconditions.checkNotNull(captured);
        MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights, captured, disableEnPassant);
        return me;
    }
    
    public MovementEffect disableEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights, captured, coord);
        return me;
    }

    public MovementEffect disableEnPassantIfAllowed(Board board) {
        Preconditions.checkNotNull(board);
        if(board.getEnPassantAllowed()!=null){
            MovementEffect me = new MovementEffect(allowEnPassant, promotedTo, castlingRights, captured, board.getEnPassantAllowed());
            return me;
        }
        return this;
    }

    @Override
    public String toString() {
        return "MovementEffect{" +
                "castlingRights=" + castlingRights +
                ", allowEnPassant=" + allowEnPassant +
                ", promotedTo=" + promotedTo +
                ", captured=" + captured +
                ", disableEnPassant=" + disableEnPassant +
                '}';
    }
}
