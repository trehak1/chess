package chess.perft;

public interface PerftResult {
    
    long getNodes(int depth);
    
    long getCaptures(int depth);
    
    long getEnPassants(int depth);

    long getCastlings(int depth);

    boolean hasCaptures();

    boolean hasEnPassants();
    
    boolean hasCastlings();
}
