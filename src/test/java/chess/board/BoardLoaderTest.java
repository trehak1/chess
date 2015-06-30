package chess.board;

import junit.framework.Assert;
import org.junit.Test;

public class BoardLoaderTest {
    
    BoardLoader boardLoader = new BoardLoader();
    BoardFactory boardFactory = new BoardFactory();
    
    @Test
    public void test() {
        Board board = boardLoader.loadBoard("newGameBoard.txt");
        Board newGameBoard = boardFactory.newGameBoard();
        Assert.assertEquals(newGameBoard, board);
    }
}
