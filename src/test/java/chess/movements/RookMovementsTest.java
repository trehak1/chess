package chess.movements;

import chess.enums.Player;
import chess.movements.figures.RookMovements;
import org.junit.Test;

public class RookMovementsTest extends MovementsTest {

    private RookMovements rookMovements = new RookMovements(Player.WHITE);

    @Test
    public void testCornerRook() {
        testNumbersOfPossibleMovements("rook/cornerRook.txt", 14);
    }

    @Test
    public void testMiddleRook() {
        testNumbersOfPossibleMovements("rook/middleRook.txt", 14);
    }

    @Test
    public void testSideRook() {
        testNumbersOfPossibleMovements("rook/sideRook.txt", 14);
    }

    @Test
    public void testBlockedRook() {
        testNumbersOfPossibleMovements("rook/blockedRook.txt", 0);
    }

    private void testNumbersOfPossibleMovements(String fileName, int number) {
        testNumbersOfPossibleMovements(fileName, rookMovements, number);
    }
}
