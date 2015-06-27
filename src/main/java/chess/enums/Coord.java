package chess.enums;

import com.google.common.base.Preconditions;

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
    H1(Col.H, Row._1), H2(Col.H, Row._2), H3(Col.H, Row._3), H4(Col.H, Row._4), H5(Col.H, Row._5), H6(Col.H, Row._6), H7(Col.H, Row._7), H8(Col.H, Row._8);

    private final Col col;
    private final Row row;

    Coord(Col col, Row row) {
        this.col = col;
        this.row = row;
    }

    public Col getCol() {
        return col;
    }

    public Row getRow() {
        return row;
    }

    public static Coord get(Col col, Row row) {
        Preconditions.checkNotNull(col);
        Preconditions.checkNotNull(row);
        for (Coord c : Coord.values()) {
            if (c.col == col && c.row == row) {
                return c;
            }
        }
        throw new IllegalStateException("wtf");
    }
}
