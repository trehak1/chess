package chess.board;

import org.junit.Assert;
import org.junit.Test;

public class BoardSerializerTest {
    
    @Test
    public void testJsonSerializingNewGameBoard() {
        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.newGameBoard();
        BoardSerializer boardSerializer = new BoardSerializer();
        String boardSerialized = boardSerializer.serializeIntoJson(board);

        Board boardDeserialized = boardSerializer.deserializeFromJson(boardSerialized);
        Assert.assertEquals(board, boardDeserialized);
        System.out.println(boardSerialized);
    }
    
    @Test
    public void testUtf8SerializationNewGameBoard() {
        BoardFactory boardFactory = new BoardFactory();
        Board board = boardFactory.newGameBoard();
        BoardSerializer boardSerializer = new BoardSerializer();
        String boardSerialized = boardSerializer.serializeIntoUtf8(board);

        Board boardDeserialized = boardSerializer.deserializeFromUtf8(boardSerialized);
        Assert.assertEquals(board, boardDeserialized);
        System.out.println(boardSerialized);
    }
}
