package chess.board;

import chess.enums.CastlingType;
import chess.enums.Player;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by Tom on 3.7.2015.
 */
public class CastlingRights implements Cloneable {

    private final boolean[] castlings;

    public CastlingRights(boolean[] castlings) {
        this.castlings = Arrays.copyOf(castlings, castlings.length);
    }

    public CastlingRights() {
        this(new boolean[]{true, true, true, true});
    }

    public boolean isCastlingEnabled(Player player, CastlingType castlingType) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castlingType);
        return castlings[castlingIndex(player, castlingType)];
    }

    public CastlingRights disableCastling(Player player, CastlingType castlingType) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(castlingType);
        int index = castlingIndex(player, castlingType);
        boolean[] copy = Arrays.copyOf(castlings, castlings.length);
        copy[index] = false;
        return new CastlingRights(copy);
    }

    public EnumSet<CastlingType> getCastlingsEnabled(Player player) {
        Preconditions.checkNotNull(player);
        EnumSet<CastlingType> res = EnumSet.noneOf(CastlingType.class);
        if (isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
            res.add(CastlingType.QUEEN_SIDE);
        }
        if (isCastlingEnabled(player, CastlingType.KING_SIDE)) {
            res.add(CastlingType.KING_SIDE);
        }
        return res;
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
                return s + 1;
            default:
                throw new IllegalArgumentException("wtf");
        }
    }

    public CastlingRights clone() {
        return new CastlingRights(castlings);
    }

    CastlingRights enableCastling(Player player, CastlingType castlingType) {
        int index = castlingIndex(player, castlingType);
        boolean[] copy = Arrays.copyOf(castlings, castlings.length);
        copy[index] = true;
        return new CastlingRights(copy);
    }
}
