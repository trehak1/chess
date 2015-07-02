package chess.movements;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.enums.Coord;
import chess.enums.Player;
import chess.movements.figures.PawnMovements;
import chess.movements.transformers.CoordinateNotationTransformer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Tom on 28.6.2015.
 */
public class PawnMovementsTest extends MovementsTest {

    private PawnMovements pawnMovements = new PawnMovements(Player.WHITE);

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

    @Test
    public void testEnPassant() {
        testNumbersOfPossibleMovements("pawn/enPassant.txt", 2);
    }

    @Test
    public void testEnPassant2() {
        testNumbersOfPossibleMovements("pawn/enPassant2.txt", 2);
    }
    
    @Test
    public void testEp() {
        Board board = new BoardLoader().loadBoard("pawn/enPassant2.txt");
        MovementFactory factory = MovementFactory.getFor(Player.WHITE);
        CoordinateNotationTransformer transformer = new CoordinateNotationTransformer(board, Player.WHITE);
        Movement m = transformer.fromNotation("H5-G6");
        Assert.assertEquals(EnPassant.class,m.getClass());
    }

    @Test
    public void testBlockedPawn() {
        testNumbersOfPossibleMovements("pawn/blockedPawn.txt", 0);
    }

    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, pawnMovements, number);
    }

}
