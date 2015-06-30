package chess.board;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BoardLoader {

    private ClassLoader classLoader;
    private BoardSerializer boardSerializer;
    
    public BoardLoader() {
        classLoader = getClass().getClassLoader();
        boardSerializer = new BoardSerializer();
    }

    public Board loadBoard(String fileName) {
        try {
            try(InputStream stream = classLoader.getResourceAsStream(fileName)) {
                byte[] bytes = ByteStreams.toByteArray(stream);
                String s = new String(bytes, StandardCharsets.UTF_8);
                return boardSerializer.deserializeFromUtf8(s);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
