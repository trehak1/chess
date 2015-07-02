package chess.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public enum CastlingType {

    QUEEN_SIDE(Col.B, Col.C, Col.D), KING_SIDE(Col.F, Col.G);

    private final List<Col> cols;

    CastlingType(Col... cols) {
        this.cols = Lists.newArrayList(cols);
    }

    public List<Col> getCols() {
        return Lists.newArrayList(cols);
    }
}
