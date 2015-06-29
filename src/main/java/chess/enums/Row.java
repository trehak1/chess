package chess.enums;

import java.util.EnumSet;
import java.util.function.Function;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Row {

    _1, _2, _3, _4, _5, _6, _7, _8, INVALID;

    public static final Function<Row, Row> SOUTH = (r) -> r.south();
    public static final Function<Row, Row> NORTH = (r) -> r.north();

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

    public static EnumSet<Row> validValues() {
        EnumSet<Row> set = EnumSet.allOf(Row.class);
        set.remove(INVALID);
        return set;
    }

    public boolean isValid() {
        return this != INVALID;
    }

}
