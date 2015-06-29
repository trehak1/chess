package chess.movements;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.figures.PawnMovements;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Tom on 28.6.2015.
 */
public class PawnMovementsTest {

    @Test
    public void testWhites() {
        Board board = new BoardFactory().newGameBoard();
        PawnMovements pawnMovements = new PawnMovements(Player.WHITE);
        List<Movement> movements = pawnMovements.getMovements(board);
        Assert.assertEquals(16, movements.size());
    }

    @Test
    public void testBlacks() {
        Board board = new BoardFactory().newGameBoard();
        PawnMovements pawnMovements = new PawnMovements(Player.BLACK);
        List<Movement> movements = pawnMovements.getMovements(board);
        Assert.assertEquals(16, movements.size());
    }

}
