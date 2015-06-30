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
        testNumbersOfPossibleMovements("cornerKnight.txt", 2);
    }

    @Test
    public void testAlmostCornerKnight() {
        testNumbersOfPossibleMovements("almostCornerKnight.txt", 3);
    }

    @Test
    public void testMiddleKnight() {
        testNumbersOfPossibleMovements("middleKnight.txt", 8);
    }

    @Test
    public void testSideKnight() {
        testNumbersOfPossibleMovements("sideKnight.txt", 4);
    }
    
    private void testNumbersOfPossibleMovements(String fileName, int number) {
        Board board = boardLoader.loadBoard(fileName);
        KnightMovements knightMovements = new KnightMovements(Player.WHITE);
        List<Movement> movements = knightMovements.getMovements(board);
        Assert.assertEquals(number, movements.size());
    }
}
