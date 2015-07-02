package chess.movements;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardLoader;
import chess.board.BoardSerializer;
import chess.enums.Coord;
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
    
    @Test
    public void castlingEnabledTest() {
        BoardLoader boardLoader = new BoardLoader();
        Board board = boardLoader.loadBoard("castling/castling.txt");
        MovementFactory movementFactory = MovementFactory.getFor(Player.WHITE);
        List<Movement> moves = movementFactory.getMoves(board);
        
        CastlingMove cm = new CastlingMove(board, Castling.KING_SIDE, Coord.E1, Coord.G1);
        for (Movement movement : moves) {
            if (movement.getFrom() == cm.getFrom()
                    && movement.getTo() == cm.getTo()
                    && movement instanceof CastlingMove
                    && ((CastlingMove) movement).getType() == cm.getType()) {
                return;
            }
        }
        throw new IllegalStateException("castling move not found in moves");
    }

}
