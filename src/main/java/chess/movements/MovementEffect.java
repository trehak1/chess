package chess.movements;

import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Figure;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementEffect {


    public static final MovementEffect NONE = new MovementEffect();
    private final List<CastlingType> disableCastlings = new ArrayList<>();
    private final Coord allowEnPassant;
    private final Figure promotedTo;

    private MovementEffect(Coord allowEnPassant, Figure promotedTo) {
        this.allowEnPassant = allowEnPassant;
        this.promotedTo = promotedTo;
    }

    public MovementEffect() {
        this(null, null);
    }

    public List<CastlingType> getDisableCastlings() {
        return Lists.newArrayList(disableCastlings);
    }

    public Coord getAllowEnPassant() {
        return allowEnPassant;
    }

    public Figure getPromotedTo() {
        return promotedTo;
    }

    public MovementEffect allowEnPassant(Coord coord) {
        Preconditions.checkNotNull(coord);
        return new MovementEffect(coord, promotedTo);
    }

    public MovementEffect disableCastling(CastlingType castlingType) {
        Preconditions.checkNotNull(castlingType);
        MovementEffect me = new MovementEffect(allowEnPassant, promotedTo);
        me.disableCastlings.add(castlingType);
        return me;
    }

    public MovementEffect promotedTo(Figure figure) {
        Preconditions.checkNotNull(figure);
        MovementEffect me = new MovementEffect(allowEnPassant, figure);
        me.disableCastlings.addAll(disableCastlings);
        return me;
    }

}
