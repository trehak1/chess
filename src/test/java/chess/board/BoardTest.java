package chess.board;


import chess.enums.Col;
import chess.enums.Figure;
import chess.enums.Player;
import chess.enums.Row;
import chess.enums.CastlingType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom on 26.6.2015.
 */
public class BoardTest {

    @Test
    public void testCastling() {
        Board b = new BoardFactory().newGameBoard();
        Assert.assertEquals(true, b.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
        b = b.disableCastling(Player.BLACK, CastlingType.KING_SIDE);
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
        b = b.disableCastling(Player.BLACK, CastlingType.QUEEN_SIDE);
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));

        b = b.disableCastling(Player.WHITE, CastlingType.KING_SIDE);
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(true, b.isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));

        b = b.disableCastling(Player.WHITE, CastlingType.QUEEN_SIDE);
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertEquals(false, b.isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE));
        
    }
    
    @Test
    public void testWhiteCastlingImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.disableCastling(Player.WHITE, CastlingType.KING_SIDE);

        Assert.assertFalse(mod.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertTrue(orig.isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE));
        Assert.assertNotSame(orig, mod);
    }

    @Test
    public void testBlackCastlingImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.disableCastling(Player.BLACK, CastlingType.KING_SIDE);

        Assert.assertFalse(mod.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertTrue(orig.isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE));
        Assert.assertNotSame(orig, mod);
    }

    @Test
    public void testSetFigureImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.set(Col.D, Row._4, Figure.BLACK_BISHOP);

        Assert.assertNotSame(orig, mod);
        Assert.assertEquals(Figure.BLACK_BISHOP, mod.get(Col.D, Row._4));
        Assert.assertEquals(Figure.NONE, orig.get(Col.D, Row._4));
    }

    @Test
    public void testRemoveFigureImmutable() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Col.A, Row._1);

        Assert.assertNotSame(orig, mod);
        Assert.assertEquals(Figure.NONE, mod.get(Col.D, Row._4));
        Assert.assertEquals(Figure.WHITE_ROOK, orig.get(Col.A, Row._1));
    }

    @Test
    public void testWhiteEnPassantImmutableAndSane() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Col.A, Row._2);
        mod = mod.set(Col.A, Row._4, Figure.WHITE_PAWN);

        Assert.assertFalse(mod.isEnPassantAllowed(Col.A, Row._4));

        Board m2 = mod.allowEnPassant(Col.A, Row._4);

        Assert.assertFalse(mod.isEnPassantAllowed(Col.A, Row._4));
        Assert.assertTrue(m2.isEnPassantAllowed(Col.A, Row._4));

    }

    @Test
    public void testBlackEnPassantImmutableAndSane() {
        Board orig = new BoardFactory().newGameBoard();
        Board mod = orig.remove(Col.A, Row._7);
        mod = mod.set(Col.A, Row._5, Figure.BLACK_PAWN);

        Assert.assertFalse(mod.isEnPassantAllowed(Col.A, Row._5));

        Board m2 = mod.allowEnPassant(Col.A, Row._5);

        Assert.assertFalse(mod.isEnPassantAllowed(Col.A, Row._5));
        Assert.assertTrue(m2.isEnPassantAllowed(Col.A, Row._5));

    }


}
