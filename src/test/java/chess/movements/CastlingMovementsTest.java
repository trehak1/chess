package chess.movements;

import chess.enums.Player;
import chess.movements.figures.CastlingMovements;
import org.junit.Test;

public class CastlingMovementsTest extends MovementsTest {

    private CastlingMovements castlingMovementsWhite = new CastlingMovements(Player.WHITE);
    private CastlingMovements castlingMovementsBlack = new CastlingMovements(Player.BLACK);

    @Test
    public void testCastling() {
        String fileName = "castling/castling.txt";
        testNumbersOfPossibleMovements(fileName, castlingMovementsWhite, 2);
        testNumbersOfPossibleMovements(fileName, castlingMovementsBlack, 1);
    }

}
