package chess.enums;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Row {

    _1, _2, _3, _4, _5, _6, _7, _8, INVALID;

    public Row south() {
        if (ordinal() - 1 > -1) {
            return Row.values()[ordinal() - 1];
        }
        return INVALID;
    }

    public Row north() {
        if (ordinal() + 1 < 8) {
            return Row.values()[ordinal() + 1];
        }
        return INVALID;
    }

    public boolean isValid() {
        return this != INVALID;
    }

}
