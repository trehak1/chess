package chess.movements;

import chess.board.BoardFactory;
import chess.enums.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MovementFactoryTest {

    @Test
    public void firstMovesTest() {
        MovementFactory factory = new MovementFactory(Player.WHITE);
        List<Movement> moves = factory.getMoves(new BoardFactory().newGameBoard());
        Assert.assertEquals(20, moves.size());
    }

}
