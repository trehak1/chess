package chess.perft;

import org.junit.Test;

/**
 * Created by Tom on 1.7.2015.
 */
public class PerftTest {


	@Test
	public void perftTest() throws InterruptedException {
		Perft perft = new Perft();
		perft.perft(3);
	}


}
