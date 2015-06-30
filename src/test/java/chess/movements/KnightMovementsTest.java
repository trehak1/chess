package chess.movements;

import chess.board.Board;
import chess.board.BoardLoader;
import chess.enums.Player;
import chess.movements.figures.KnightMovements;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class KnightMovementsTest {
    
    BoardLoader boardLoader = new BoardLoader();

    @Test
    public void testCornerKnight() {
        Board board = boardLoader.loadBoard("cornerKnight.txt");
        KnightMovements knightMovements = new KnightMovements(Player.WHITE);
        List<Movement> movements = knightMovements.getMovements(board);
        Assert.assertEquals(2, movements.size());
    }

    @Test
    public void testAlmostCornerKnight() {
        Board board = boardLoader.loadBoard("almostCornerKnight.txt");
        KnightMovements knightMovements = new KnightMovements(Player.WHITE);
        List<Movement> movements = knightMovements.getMovements(board);
        Assert.assertEquals(3, movements.size());
    }

    @Test
    public void testMiddleKnight() {
        Board board = boardLoader.loadBoard("middleKnight.txt");
        KnightMovements knightMovements = new KnightMovements(Player.WHITE);
        List<Movement> movements = knightMovements.getMovements(board);
        Assert.assertEquals(8, movements.size());
    }

    @Test
    public void testSideKnight() {
        Board board = boardLoader.loadBoard("sideKnight.txt");
        KnightMovements knightMovements = new KnightMovements(Player.WHITE);
        List<Movement> movements = knightMovements.getMovements(board);
        Assert.assertEquals(4, movements.size());
    }
}
