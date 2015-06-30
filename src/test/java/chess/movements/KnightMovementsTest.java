package chess.movements;

import chess.enums.Player;
import chess.movements.figures.KnightMovements;
import org.junit.Test;

public class KnightMovementsTest extends MovementsTest {

    private KnightMovements knightMovements = new KnightMovements(Player.WHITE);

    @Test
    public void testCornerKnight() {
        testNumbersOfPossibleMovements("knight/cornerKnight.txt", 2);
    }

    @Test
    public void testAlmostCornerKnight() {
        testNumbersOfPossibleMovements("knight/almostCornerKnight.txt", 3);
    }

    @Test
    public void testMiddleKnight() {
        testNumbersOfPossibleMovements("knight/middleKnight.txt", 8);
    }

    @Test
    public void testSideKnight() {
        testNumbersOfPossibleMovements("knight/sideKnight.txt", 4);
    }

    @Test
    public void testBlockedKnight() {
        testNumbersOfPossibleMovements("knight/blockedKnight.txt", 0);
    }
    
    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, knightMovements, number);
    }
}
