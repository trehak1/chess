package chess.movements;

import chess.enums.Player;
import chess.movements.figures.QueenMovements;
import org.junit.Test;

public class QueenMovementsTest extends MovementsTest {

    private QueenMovements queenMovements = new QueenMovements(Player.WHITE);

    @Test
    public void testCornerQueen() {
        testNumbersOfPossibleMovements("queen/cornerQueen.txt", 21);
    }

    @Test
    public void testMiddleQueen() {
        testNumbersOfPossibleMovements("queen/middleQueen.txt", 27);
    }

    @Test
    public void testSideQueen() {
        testNumbersOfPossibleMovements("queen/sideQueen.txt", 21);
    }

    @Test
    public void testBlockedQueen() {
        testNumbersOfPossibleMovements("queen/blockedQueen.txt", 0);
    }

    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, queenMovements, number);
    }
}
