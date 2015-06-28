package chess.board;

import chess.enums.Col;
import chess.enums.Figure;
import chess.enums.Row;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Tom on 26.6.2015.
 */
public class BoardFactoryTest {

    private Board board;

    @Before
    public void before() {
        this.board = new BoardFactory().newGameBoard();
    }

    @Test
    public void newGameBoardTest() {
        for (Col c : Col.validValues()) {
            assertEquals(Figure.WHITE_PAWN, board.get(c, Row._2));
        }
        assertEquals(Figure.WHITE_ROOK, board.get(Col.A, Row._1));
        assertEquals(Figure.WHITE_ROOK, board.get(Col.H, Row._1));
        assertEquals(Figure.WHITE_KNIGHT, board.get(Col.B, Row._1));
        assertEquals(Figure.WHITE_KNIGHT, board.get(Col.G, Row._1));
        assertEquals(Figure.WHITE_BISHOP, board.get(Col.C, Row._1));
        assertEquals(Figure.WHITE_BISHOP, board.get(Col.F, Row._1));
        assertEquals(Figure.WHITE_QUEEN, board.get(Col.D, Row._1));
        assertEquals(Figure.WHITE_KING, board.get(Col.E, Row._1));


        for (Col c : Col.validValues()) {
            assertEquals(Figure.BLACK_PAWN, board.get(c, Row._7));
        }
        assertEquals(Figure.BLACK_ROOK, board.get(Col.A, Row._8));
        assertEquals(Figure.BLACK_ROOK, board.get(Col.H, Row._8));
        assertEquals(Figure.BLACK_KNIGHT, board.get(Col.B, Row._8));
        assertEquals(Figure.BLACK_KNIGHT, board.get(Col.G, Row._8));
        assertEquals(Figure.BLACK_BISHOP, board.get(Col.C, Row._8));
        assertEquals(Figure.BLACK_BISHOP, board.get(Col.F, Row._8));
        assertEquals(Figure.BLACK_QUEEN, board.get(Col.D, Row._8));
        assertEquals(Figure.BLACK_KING, board.get(Col.E, Row._8));

    }
}
