package chess.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public enum CastlingType {

    QUEEN_SIDE(Lists.newArrayList(Col.B, Col.C, Col.D), Col.C, Col.D, Col.A), KING_SIDE(Lists.newArrayList(Col.F, Col.G), Col.G, Col.F, Col.H);

    private final List<Col> emptyCols;
    private final Col rookStartingCol;
    private final Col kingDestinationCol;
    private final Col rookDestinationCol;

    CastlingType(List<Col> emptyCols, Col kingDestinationCol, Col rookDestinationCol, Col rookStartingCol) {
        this.emptyCols = Lists.newArrayList(emptyCols);
        this.kingDestinationCol = kingDestinationCol;
        this.rookDestinationCol = rookDestinationCol;
        this.rookStartingCol = rookStartingCol;
    }
    
    public static CastlingType fromKingDestCol(Col kingDestination) {
        Preconditions.checkNotNull(kingDestination);
        switch (kingDestination) {
            case C:
                return QUEEN_SIDE;
            case G:
                return KING_SIDE;
            default:
                throw new IllegalArgumentException("wtf");
        }
    }

    public List<Col> getEmptyCols() {
        return Lists.newArrayList(emptyCols);
    }
    
    public Col getKingDestinationCol() {
        return kingDestinationCol;
    }
    
    public Coord getKingDestinationCoord(Player player) {
        return Coord.get(kingDestinationCol, player.getStartingRow());
    }

    public Col getRookStartingCol() {
        return rookStartingCol;
    }
    
    public Col getRookDestinationCol() {
        return rookDestinationCol; 
    }
    
    public Coord getRookDestinationCoord(Player player) {
        return Coord.get(rookDestinationCol, player.getStartingRow());
    }

    public CastlingType other() {
        switch (this) {
            case KING_SIDE:
                return QUEEN_SIDE;
            case QUEEN_SIDE:
                return KING_SIDE;
            default:
                throw new IllegalStateException("wtf");
        }
    }

    public Coord getRookStartingCoord(Player player) {
        return Coord.get(rookStartingCol, player.getStartingRow());
    }

    public List<Coord> requiredEmpty(Player player) {
        List<Coord> requiredEmpty = Lists.newArrayList();
        emptyCols.forEach((c) -> requiredEmpty.add(Coord.get(c,player.getStartingRow())));
        return requiredEmpty;
    }
    
    public List<Coord> requiredNotEndangered(Player player) {
        List<Coord> notEndangered = Lists.newArrayList();
        // king coord
        notEndangered.add(Coord.get(Col.E, player.getStartingRow()));
        // coords king is moving through
        // which is rook destination col
        notEndangered.add(Coord.get(rookDestinationCol, player.getStartingRow()));
        // and king destination col
        notEndangered.add(Coord.get(kingDestinationCol, player.getStartingRow()));
        return notEndangered;
    }
    
}
