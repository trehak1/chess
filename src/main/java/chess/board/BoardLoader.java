package chess.board;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class BoardLoader {

    private ClassLoader classLoader;
    private BoardSerializer boardSerializer;
    
    public BoardLoader() {
        classLoader = getClass().getClassLoader();
        boardSerializer = new BoardSerializer();
    }

    public Board loadBoard(String fileName) {
        try {
            String s = IOUtils.toString(classLoader.getResourceAsStream(fileName));
            return boardSerializer.deserializeFromUtf8(s);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
