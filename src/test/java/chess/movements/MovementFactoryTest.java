package chess.movements;

import chess.board.BoardFactory;
import chess.board.BoardSerializer;
import chess.enums.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MovementFactoryTest {

    @Test
    public void firstMovesTest() {
        Player player = Player.WHITE;
        MovementFactory factory = MovementFactory.getFor(player);
        List<Movement> moves = factory.getMoves(new BoardFactory().newGameBoard());
        Assert.assertEquals(20, moves.size());
    }

}
