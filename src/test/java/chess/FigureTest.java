package chess;

import chess.Figure;
import chess.Piece;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom on 26.6.2015.
 */

public class FigureTest {


    @Test
    public void figureInstanceTest() {

        for(Piece p : Piece.values()) {

            Figure figure = Figure.white(p);
            Figure another = Figure.white(p);

            Assert.assertTrue(figure == another);
        }

        for(Piece p : Piece.values()) {

            Figure figure = Figure.black(p);
            Figure another = Figure.black(p);

            Assert.assertTrue(figure == another);
        }

    }

}
