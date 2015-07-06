package chess.perft;

import java.util.Arrays;

public enum PerftResults implements PerftResult {
    POSITION_1(new long[]{20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L, 2439530234167L, 69352859712417L},
            new long[]{0, 0, 34, 1576, 82719, 2812008},
            null,
            null),
    POSITION_2(new long[]{48, 2039, 97862, 4085603, 193690690L},
            new long[]{8, 351, 17102, 757163, 35043416},
            new long[]{0, 1, 45, 1929, 73365},
            new long[]{2, 91, 3162, 128013, 4993637}),
    POSITION_3(new long[]{14, 191, 2812, 43238, 674624, 11030083, 178633661L},
            new long[]{1, 14, 209, 3348, 52051, 940350, 14519036},
            new long[]{0, 0, 2, 123, 1165, 33325, 294874},
            new long[]{0, 0, 0, 0, 0, 0, 0}),
    POSITION_4(new long[]{6, 264, 9467, 422333, 15833292, 706045033},
            new long[]{0, 87, 1021, 131393, 2046173, 210369132},
            new long[]{0, 0, 4, 0, 6512, 212},
            new long[]{0, 6, 0, 7795, 0, 10882006}),
    POSITION_5(new long[]{42, 1352, 53392},
            null,
            null,
            null),
    POSITION_6(new long[]{46, 2079, 89890, 3894594, 164075551, 6923051137L, 287188994746L, 11923589843526L, 490154852788714L},
            null,
            null,
            null);


    private long[] nodes;
    private long[] captures;
    private long[] enpassants;
    private long[] castlings;

    PerftResults(long[] nodes, long[] captures, long[] enpassants, long[] castlings) {
        this.nodes = nodes;
        captures = fill(captures, nodes);
        this.captures = captures;
        enpassants = fill(enpassants, nodes);
        this.enpassants = enpassants;
        castlings = fill(castlings, nodes);
        this.castlings = castlings;
    }

    private long[] fill(long[] specific, long[] nodes) {
        if (specific != null) {
            return specific;
        }
        long[] vals = new long[nodes.length];
        Arrays.fill(vals, -1L);
        return vals;
    }

    @Override
    public long getNodes(int depth) {
        return nodes[depth];
    }

    @Override
    public long getCaptures(int depth) {
        return captures[depth];
    }

    @Override
    public long getEnPassants(int depth) {
        return enpassants[depth];
    }

    @Override
    public long getCastlings(int depth) {
        return castlings[depth];
    }


}
