package chess.enums;

import com.google.common.base.Preconditions;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Figure {
    
    WHITE_PAWN(Player.WHITE, Piece.PAWN),
    WHITE_ROOK(Player.WHITE, Piece.ROOK),
    WHITE_QUEEN(Player.WHITE, Piece.QUEEN),
    WHITE_KING(Player.WHITE, Piece.KING),
    WHITE_BISHOP(Player.WHITE, Piece.BISHOP),
    WHITE_KNIGHT(Player.WHITE, Piece.KNIGHT),
    BLACK_PAWN(Player.BLACK, Piece.PAWN),
    BLACK_ROOK(Player.BLACK, Piece.ROOK),
    BLACK_QUEEN(Player.BLACK, Piece.QUEEN),
    BLACK_KING(Player.BLACK, Piece.KING),
    BLACK_BISHOP(Player.BLACK, Piece.BISHOP),
    BLACK_KNIGHT(Player.BLACK, Piece.KNIGHT),
    NONE(null, null);

    private static final Figure[] VALS = Figure.values();
    
    private final Piece piece;
    private final Player player;

    Figure(Player player, Piece piece) {
        this.player = player;
        this.piece = piece;
    }

    public static Figure white(Piece piece) {
        return get(Player.WHITE, piece);
    }

    public static Figure black(Piece piece) {
        return get(Player.BLACK, piece);
    }

    public static Figure get(Player player, Piece piece) {
        Preconditions.checkNotNull(piece);
        Preconditions.checkNotNull(player);
        return VALS[6*player.ordinal()+piece.ordinal()];
    }

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }
    
}
