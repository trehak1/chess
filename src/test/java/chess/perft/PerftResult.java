package chess.perft;

public interface PerftResult {
    
    long getNodes(int depth);
    
    long getCaptures(int depth);
}
