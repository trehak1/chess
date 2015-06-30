package chess.enums;

/**
 * Created by Tom on 26.6.2015.
 */
public enum Player {

    WHITE(Row._1), BLACK(Row._8);

    private final Row startingRow;

    Player(Row startingRow) {
        this.startingRow = startingRow;
    }

    public Row getStartingRow() {
        return startingRow;
    }

    public Player enemy() {
        if (this == WHITE) {
            return BLACK;
        } else if (this == BLACK) {
            return WHITE;
        } else {
            throw new IllegalStateException("wtf");
        }
    }

}
