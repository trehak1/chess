package chess.enums;

import com.google.common.base.Preconditions;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Piece {

    PAWN, ROOK, QUEEN, KING, BISHOP, KNIGHT;

    private static final Piece[] VALS = Piece.values();
    
    public static Piece get(int ord) {
        Preconditions.checkArgument(ord >= 0 && ord < 6);
        return VALS[ord];
    }
}
