package chess;

import com.google.common.base.Preconditions;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Figure {


    NONE(null,null),
    WHITE_PAWN(Player.WHITE,Piece.PAWN),
    WHITE_ROOK(Player.WHITE, Piece.ROOK),
    WHITE_KNIGHT(Player.WHITE, Piece.KNIGHT),
    WHITE_BISHOP(Player.WHITE, Piece.BISHOP),
    WHITE_QUEEN(Player.WHITE,Piece.QUEEN),
    WHITE_KING(Player.WHITE, Piece.KING),
    BLACK_PAWN(Player.BLACK,Piece.PAWN),
    BLACK_ROOK(Player.BLACK, Piece.ROOK),
    BLACK_KNIGHT(Player.BLACK, Piece.KNIGHT),
    BLACK_BISHOP(Player.BLACK, Piece.BISHOP),
    BLACK_QUEEN(Player.BLACK,Piece.QUEEN),
    BLACK_KING(Player.BLACK, Piece.KING);

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
        for (Figure f : Figure.values()) {
            if (f.getPlayer() == player) {
                if (f.getPiece() == piece) {
                    return f;
                }
            }
        }
        throw new IllegalArgumentException("wtf");
    }

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }

}
