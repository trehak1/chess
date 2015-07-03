package chess.perft;

import chess.board.Board;
import chess.board.BoardFactory;
import chess.enums.Player;
import chess.movements.*;
import com.google.common.collect.Lists;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Perft {

    private Board board;
    private Player player;
    private PerftResult perftResult;

    public Perft(Board board, Player player, PerftResult perftResult) {
        this.board = board;
        this.player = player;
        this.perftResult = perftResult;
    }

    public long perft(int iterations) throws InterruptedException {
        final List<Board> boards = Lists.newArrayList(board);
        Date start = new Date();
        System.out.println("Start: "+start);
        for (int i = 0; i < iterations; i++) {
            List<Movement> allMoves = getAllMoves(boards, player);
            //Assert.assertEquals("number of nodes differ", perftResult.getNodes(i), allMoves.size());
            if (perftResult.hasCaptures()) {
                List<Capture> captures = getAllCaptures(allMoves);
                Assert.assertEquals("number of captures differ", perftResult.getCaptures(i), captures.size());
            }
            if (perftResult.hasEnPassants()) {
                List<EnPassant> enPassants = getAllEnPassants(allMoves);
                Assert.assertEquals("number of enpassants differ", perftResult.getEnPassants(i), enPassants.size());
            }
            if (perftResult.hasCastlings()) {
                List<Castling> castlings = getAllCastlings(allMoves);
                Assert.assertEquals("number of castlings differ", perftResult.getCastlings(i), castlings.size());
            }
            boards.clear();
            allMoves.forEach((m) -> boards.add(m.getResultingBoard()));
            player = player.enemy();
            System.out.println("iteration: " + (i+1) + ", boards: " + allMoves.size() + ", completed on " + new Date());
            if (i == iterations - 1) {
                return allMoves.size();
            }
        }
        return -1;
    }

    private List<Movement> getAllMoves(List<Board> boards, Player player) throws InterruptedException {
        List<Movement> moves = new ArrayList<>();
        MovementFactory factory = MovementFactory.getFor(player);
//        boards.parallelStream().forEach((b) -> moves.addAll(factory.getMoves(b)));
        boards.forEach((b) -> moves.addAll(factory.getMoves(b)));
        return moves;
    }
    
    private List<Capture> getAllCaptures(List<Movement> movements) {
        List<Capture> captures = new ArrayList<>();
        movements.forEach((m) -> {if (m instanceof Capture){ captures.add((Capture) m); }});
        return captures;
    }

    private List<EnPassant> getAllEnPassants(List<Movement> movements) {
        List<EnPassant> captures = new ArrayList<>();
        movements.forEach((m) -> {if (m instanceof EnPassant){ captures.add((EnPassant) m); }});
        return captures;
    }

    private List<Castling> getAllCastlings(List<Movement> movements) {
        List<Castling> captures = new ArrayList<>();
        movements.forEach((m) -> {if (m instanceof Castling){ captures.add((Castling) m); }});
        return captures;
    }

    public static void main(String[] args) throws InterruptedException {
        Perft perft = new Perft(new BoardFactory().newGameBoard(), Player.WHITE, PerftResults.POSITION_1);
        long res = perft.perft(5);
        System.out.println(res);
    }

}
