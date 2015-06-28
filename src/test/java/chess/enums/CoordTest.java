package chess.enums;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Tom on 27.6.2015.
 */
public class CoordTest {

    @Test
    public void testAllCoordsPresent() {
        for (Col c : Col.validValues()) {
            for (Row r : Row.validValues()) {
                Coord coord = Coord.get(c, r);
                Assert.assertNotNull(coord);
                Assert.assertEquals(c, coord.getCol());
                Assert.assertEquals(r, coord.getRow());
            }
        }
    }

}
