package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.board.BoardSerializer;
import chess.enums.Player;
import chess.movements.MovementFactory;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Stack;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {

	private static final long[] PERFT = new long[]{20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L, 2439530234167L, 69352859712417L};

	@Test
	public void perftTest() {

	}



}
