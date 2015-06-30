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
        BoardSerializer serializer = new BoardSerializer();
        Player player = Player.WHITE;
        MovementFactory factory = MovementFactory.getFor(player);
        List<Movement> moves = factory.getMoves(new BoardFactory().newGameBoard());
        Assert.assertEquals(20, moves.size());

//        System.out.println(serializer.serializeIntoUtf8(moves.get(0).getResultingBoard()));
        
        for (int i = 0; i < 5; i++) {
            player = player.enemy();
            moves = MovementFactory.getFor(player).getMoves(moves.get(0).getResultingBoard());
//            System.out.println(serializer.serializeIntoUtf8(moves.get(0).getResultingBoard()));
        }


    }

}
