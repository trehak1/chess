package chess.perft;

public enum PerftResults implements PerftResult {
    POSITION_1(new long[]{20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L, 2439530234167L, 69352859712417L},
            new long[]{0, 0, 34, 1576, 82719, 2812008}),
    POSITION_2(new long[]{48, 2039, 97862, 4085603, 193690690L},
            new long[]{8, 351, 17102, 757163, 35043416}),
    POSITION_3(new long[]{14, 191, 2812, 43238, 674624, 11030083, 178633661L},
            new long[]{1, 14, 209, 3348, 52051, 940350, 14519036}),
    POSITION_4(new long[]{6, 264, 9467, 422333, 15833292, 706045033},
            new long[]{0, 87, 1021, 131393, 2046173, 210369132})
    ;
    
    
    private long[] nodes;
    private long[] captures;

    PerftResults(long[] nodes, long[] captures) {
        this.nodes = nodes;
        this.captures = captures;
    }

    @Override
    public long getNodes(int depth) {
        return nodes[depth];
    }

    @Override
    public long getCaptures(int depth) {
        return captures[depth];
    }
}
