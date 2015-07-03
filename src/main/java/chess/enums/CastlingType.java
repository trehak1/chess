package chess.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public enum CastlingType {

    QUEEN_SIDE(Lists.newArrayList(Col.B, Col.C), Col.D, Col.F, Col.A), KING_SIDE(Lists.newArrayList(Col.F), Col.G, Col.F, Col.H);

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
}
