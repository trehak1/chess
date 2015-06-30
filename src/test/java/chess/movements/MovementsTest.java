package chess.movements;

import chess.board.Board;
import chess.board.BoardLoader;
import org.junit.Assert;

import java.util.List;

public class MovementsTest {

    BoardLoader boardLoader = new BoardLoader();
    
    public void testNumbersOfPossibleMovements(String fileName, MovementProducer movementProducer, int number) {
        Board board = boardLoader.loadBoard(fileName);
        List<Movement> movements = movementProducer.getMovements(board);
        Assert.assertEquals(number, movements.size());
    }
}
