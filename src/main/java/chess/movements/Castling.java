package chess.movements;

import chess.enums.Col;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public enum Castling {

    QUEEN_SIDE(Col.B, Col.C, Col.D), KING_SIDE(Col.F, Col.G);

    private final List<Col> cols;

    Castling(Col... cols) {
        this.cols = Lists.newArrayList(cols);
    }

    public List<Col> getCols() {
        return Lists.newArrayList(cols);
    }
}
