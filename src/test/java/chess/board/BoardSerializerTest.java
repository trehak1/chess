package chess.board;

import org.junit.Assert;
import org.junit.Test;

public class BoardSerializerTest {
    
    @Test
    public void testSerializingNewGameBoard() {
        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.newGameBoard();
        BoardSerializer boardSerializer = new BoardSerializer();
        String boardSerialized = boardSerializer.serialize(board);

        Board boardDeserialized = boardSerializer.deserialize(boardSerialized);
        Assert.assertEquals(board, boardDeserialized);
    }
}
