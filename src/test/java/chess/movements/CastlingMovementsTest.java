package chess.movements;

import chess.enums.Player;
import chess.movements.figures.CastlingMoves;
import org.junit.Test;

public class CastlingMovementsTest extends MovementsTest {

    private CastlingMoves castlingMovementsWhite = new CastlingMoves(Player.WHITE);
    private CastlingMoves castlingMovementsBlack = new CastlingMoves(Player.BLACK);

    @Test
    public void testCastling() {
        String fileName = "castling/castling.txt";
        testNumbersOfPossibleMovements(fileName, castlingMovementsWhite, 2);
        testNumbersOfPossibleMovements(fileName, castlingMovementsBlack, 1);
    }

}
