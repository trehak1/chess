package chess.movements;

import chess.enums.Player;
import chess.movements.figures.BishopMovements;
import org.junit.Test;

public class BishopMovementsTest extends MovementsTest {

    private BishopMovements bishopMovements = new BishopMovements(Player.WHITE);

    @Test
    public void testCornerBishop() {
        testNumbersOfPossibleMovements("bishop/cornerBishop.txt", 7);
    }

    @Test
    public void testMiddleBishop() {
        testNumbersOfPossibleMovements("bishop/middleBishop.txt", 13);
    }

    @Test
    public void testSideBishop() {
        testNumbersOfPossibleMovements("bishop/sideBishop.txt", 7);
    }

    @Test
    public void testBlockedBishop() {
        testNumbersOfPossibleMovements("bishop/blockedBishop.txt", 0);
    }

    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, bishopMovements, number);
    }
}
