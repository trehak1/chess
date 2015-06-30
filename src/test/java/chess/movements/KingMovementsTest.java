package chess.movements;

import chess.enums.Player;
import chess.movements.figures.KingMovements;
import org.junit.Test;

public class KingMovementsTest extends MovementsTest {

    private KingMovements kingMovements = new KingMovements(Player.WHITE);

    @Test
    public void testCornerKing() {
        testNumbersOfPossibleMovements("king/cornerKing.txt", 3);
    }

    @Test
    public void testMiddleKing() {
        testNumbersOfPossibleMovements("king/middleKing.txt", 8);
    }

    @Test
    public void testSideKing() {
        testNumbersOfPossibleMovements("king/sideKing.txt", 5);
    }

    @Test
    public void testBlockedKing() {
        testNumbersOfPossibleMovements("king/blockedKing.txt", 0);
    }

    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, kingMovements, number);
    }
}
