package chess.enums;

import java.util.EnumSet;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Col {

    A, B, C, D, E, F, G, H, INVALID;

    public Col west() {
        if (ordinal() - 1 > -1) {
            return Col.values()[ordinal() - 1];
        }
        return INVALID;
    }

    public Col east() {
        if (ordinal() + 1 < 8) {
            return Col.values()[ordinal() + 1];
        }
        return INVALID;
    }

    public static EnumSet<Col> validValues() {
        EnumSet<Col> set = EnumSet.allOf(Col.class);
        set.remove(INVALID);
        return set;
    }

    public boolean isValid() {
        return this != INVALID;
    }

}
