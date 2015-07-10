package chess.enums;

import com.google.common.base.Preconditions;

import java.util.EnumSet;
import java.util.function.Function;

/**
 * Created by Tom on 27.6.2015.
 */
public enum Coord {

    A1(Col.A, Row._1), A2(Col.A, Row._2), A3(Col.A, Row._3), A4(Col.A, Row._4), A5(Col.A, Row._5), A6(Col.A, Row._6), A7(Col.A, Row._7), A8(Col.A, Row._8),
    B1(Col.B, Row._1), B2(Col.B, Row._2), B3(Col.B, Row._3), B4(Col.B, Row._4), B5(Col.B, Row._5), B6(Col.B, Row._6), B7(Col.B, Row._7), B8(Col.B, Row._8),
    C1(Col.C, Row._1), C2(Col.C, Row._2), C3(Col.C, Row._3), C4(Col.C, Row._4), C5(Col.C, Row._5), C6(Col.C, Row._6), C7(Col.C, Row._7), C8(Col.C, Row._8),
    D1(Col.D, Row._1), D2(Col.D, Row._2), D3(Col.D, Row._3), D4(Col.D, Row._4), D5(Col.D, Row._5), D6(Col.D, Row._6), D7(Col.D, Row._7), D8(Col.D, Row._8),
    E1(Col.E, Row._1), E2(Col.E, Row._2), E3(Col.E, Row._3), E4(Col.E, Row._4), E5(Col.E, Row._5), E6(Col.E, Row._6), E7(Col.E, Row._7), E8(Col.E, Row._8),
    F1(Col.F, Row._1), F2(Col.F, Row._2), F3(Col.F, Row._3), F4(Col.F, Row._4), F5(Col.F, Row._5), F6(Col.F, Row._6), F7(Col.F, Row._7), F8(Col.F, Row._8),
    G1(Col.G, Row._1), G2(Col.G, Row._2), G3(Col.G, Row._3), G4(Col.G, Row._4), G5(Col.G, Row._5), G6(Col.G, Row._6), G7(Col.G, Row._7), G8(Col.G, Row._8),
    H1(Col.H, Row._1), H2(Col.H, Row._2), H3(Col.H, Row._3), H4(Col.H, Row._4), H5(Col.H, Row._5), H6(Col.H, Row._6), H7(Col.H, Row._7), H8(Col.H, Row._8),
    INVALID(Col.INVALID, Row.INVALID);

    public static final EnumSet<Coord> VALID_VALUES = EnumSet.complementOf(EnumSet.of(INVALID));
    private static final Coord[] vals = Coord.values();
    public static final Function<Coord, Coord> NORTH = (c) -> c.north();
    public static final Function<Coord, Coord> NORTH_WEST = (c) -> c.northWest();
    public static final Function<Coord, Coord> NORTH_EAST = (c) -> c.northEast();
    public static final Function<Coord, Coord> SOUTH = (c) -> c.south();
    public static final Function<Coord, Coord> SOUTH_EAST = (c) -> c.southEast();
    public static final Function<Coord, Coord> SOUTH_WEST = (c) -> c.southWest();
    public static final Function<Coord, Coord> EAST = (c) -> c.east();
    public static final Function<Coord, Coord> WEST = (c) -> c.west();

    private final Col col;
    private final Row row;

    Coord(Col col, Row row) {
        this.col = col;
        this.row = row;
    }

    public boolean isValid() {
        return this != INVALID;
    }

    public Coord apply(Function<Coord, Coord> function) {
        return function.apply(this);
    }

    public Coord east() {
        return Coord.get(col.east(), row);
    }

    public Coord west() {
        return Coord.get(col.west(), row);
    }

    public Coord north() {
        return Coord.get(col, row.north());
    }

    public Coord south() {
        return Coord.get(col, row.south());
    }

    public Coord northEast() {
        return north().east();
    }

    public Coord northWest() {
        return north().west();
    }

    public Coord southEast() {
        return south().east();
    }

    public Coord southWest() {
        return south().west();
    }


    public Col getCol() {
        return col;
    }

    public Row getRow() {
        return row;
    }

    public static Coord get(int ord) {
        return vals[ord];
    }
    
    public static Coord get(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(row);
        if (col == Col.INVALID || row == Row.INVALID) {
            return INVALID;
        }
        int colOrd = col.ordinal();
        int rowOrd = row.ordinal();
        return Coord.get(colOrd*8+rowOrd);
    }
}
