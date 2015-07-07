package chess.board;

import chess.enums.Col;
import chess.enums.Coord;
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
            assertEquals(Figure.WHITE_PAWN, board.get(Coord.get(c, Row._2)));
        }
        assertEquals(Figure.WHITE_ROOK, board.get(Coord.A1));
        assertEquals(Figure.WHITE_ROOK, board.get(Coord.H1));
        assertEquals(Figure.WHITE_KNIGHT, board.get(Coord.B1));
        assertEquals(Figure.WHITE_KNIGHT, board.get(Coord.G1));
        assertEquals(Figure.WHITE_BISHOP, board.get(Coord.C1));
        assertEquals(Figure.WHITE_BISHOP, board.get(Coord.F1));
        assertEquals(Figure.WHITE_QUEEN, board.get(Coord.D1));
        assertEquals(Figure.WHITE_KING, board.get(Coord.E1));


        for (Col c : Col.validValues()) {
            assertEquals(Figure.BLACK_PAWN, board.get(Coord.get(c, Row._7)));
        }
        assertEquals(Figure.BLACK_ROOK, board.get(Coord.A8));
        assertEquals(Figure.BLACK_ROOK, board.get(Coord.H8));
        assertEquals(Figure.BLACK_KNIGHT, board.get(Coord.B8));
        assertEquals(Figure.BLACK_KNIGHT, board.get(Coord.G8));
        assertEquals(Figure.BLACK_BISHOP, board.get(Coord.C8));
        assertEquals(Figure.BLACK_BISHOP, board.get(Coord.F8));
        assertEquals(Figure.BLACK_QUEEN, board.get(Coord.D8));
        assertEquals(Figure.BLACK_KING, board.get(Coord.E8));

    }
}
