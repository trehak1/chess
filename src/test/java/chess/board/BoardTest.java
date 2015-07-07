package chess.board;


import chess.enums.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom on 26.6.2015.
 */
public class BoardTest {

    @Test
    public void testCastling() {
        Board b = new BoardFactory().newGameBoard();
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
        b = b.disableCastling(Player.BLACK, CastlingType.KING_SIDE);
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
        b = b.disableCastling(Player.BLACK, CastlingType.QUEEN_SIDE);
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));

        b = b.disableCastling(Player.WHITE, CastlingType.KING_SIDE);
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));

        b = b.disableCastling(Player.WHITE, CastlingType.QUEEN_SIDE);
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
    }
    
    @Test
    public void testWhiteCastlingImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.disableCastling(Player.WHITE, CastlingType.KING_SIDE);

        Assert.assertFalse(mod.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertTrue(orig.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertNotSame(orig, mod);
    }

    @Test
    public void testBlackCastlingImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.disableCastling(Player.BLACK, CastlingType.KING_SIDE);

        Assert.assertFalse(mod.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertTrue(orig.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertNotSame(orig, mod);
    }

    @Test
    public void testSetFigureImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.set(Coord.D4, Figure.BLACK_BISHOP);

        Assert.assertNotSame(orig, mod);
        Assert.assertEquals(Figure.BLACK_BISHOP, mod.get(Coord.D4));
        Assert.assertEquals(Figure.NONE, orig.get(Coord.D4));
    }

    @Test
    public void testRemoveFigureImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Coord.A1);

        Assert.assertNotSame(orig, mod);
        Assert.assertEquals(Figure.NONE, mod.get(Coord.D4));
        Assert.assertEquals(Figure.WHITE_ROOK, orig.get(Coord.A1));
    }

    @Test
    public void testWhiteEnPassantImmutableAndSane() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Coord.A2);
        mod = mod.set(Coord.A4, Figure.WHITE_PAWN);

        Assert.assertFalse(mod.isEnPassantAllowed(Coord.A4));

        Board m2 = mod.allowEnPassant(Coord.A4);

        Assert.assertFalse(mod.isEnPassantAllowed(Coord.A4));
        Assert.assertTrue(m2.isEnPassantAllowed(Coord.A4));

    }

    @Test
    public void testBlackEnPassantImmutableAndSane() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Coord.A7);
        mod = mod.set(Coord.A5, Figure.BLACK_PAWN);

        Assert.assertFalse(mod.isEnPassantAllowed(Coord.A5));

        Board m2 = mod.allowEnPassant(Coord.A5);

        Assert.assertFalse(mod.isEnPassantAllowed(Coord.A5));
        Assert.assertTrue(m2.isEnPassantAllowed(Coord.A5));

    }


}
