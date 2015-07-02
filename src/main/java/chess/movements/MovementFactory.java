package chess.movements;

import chess.board.Board;
import chess.enums.*;
import chess.movements.figures.*;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovementFactory {

    private final List<MovementProducer> producers = new ArrayList<>();

    private static final MovementFactory WHITE = new MovementFactory(Player.WHITE);
    private static final MovementFactory BLACK = new MovementFactory(Player.BLACK);
    private final Player player;

    public static MovementFactory getFor(Player player) {
        Preconditions.checkNotNull(player);
        switch (player) {
            case WHITE:
                return WHITE;
            case BLACK:
                return BLACK;
        }
        throw new IllegalStateException("wtf");
    }

    private MovementFactory(Player player) {
        this.player = player;
        producers.add(new BishopMovements(player));
        producers.add(new CastlingMovements(player));
        producers.add(new KingMovements(player));
        producers.add(new KnightMovements(player));
        producers.add(new PawnMovements(player));
        producers.add(new QueenMovements(player));
        producers.add(new RookMovements(player));
    }

    private List<Movement> getPseudoLegalMoves(Board board) {
        List<Movement> list = new ArrayList<>();
        for (MovementProducer producer : producers) {
            try {
                list.addAll(producer.getMovements(board));
            } catch (Exception e) {
                System.err.println("Exception in "+producer);
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Movement> getMoves(Board board) {
        List<Movement> pseudoLegalMoves = getPseudoLegalMoves(board);
        return filterOutIllegalMoves(pseudoLegalMoves);
    }

    private List<Movement> filterOutIllegalMoves(List<Movement> list) {
        Iterator<Movement> it = list.iterator();
        while (it.hasNext()) {
            Movement m = it.next();
            Board nextBoard = m.getResultingBoard();
            Coord kingCoord = MoveUtils.locateKing(player, nextBoard);
            if (isEndangered(nextBoard, kingCoord)) {
                it.remove();
            } else if ((m instanceof Castling) && castlingFieldEndangered((Castling) m, nextBoard)) {
                it.remove();
            }
        }
        return list;
    }

    // check if any of castling field of interest is in check
    private boolean castlingFieldEndangered(Castling m, Board nextBoard) {
        CastlingType castlingType = m.getType();
        switch (castlingType) {
            case QUEEN_SIDE:
                return isEndangered(nextBoard, Coord.get(Col.C, player.getStartingRow()))
                        || isEndangered(nextBoard, Coord.get(Col.D, player.getStartingRow()))
                        || isEndangered(nextBoard, Coord.get(Col.E, player.getStartingRow()));
            case KING_SIDE:
                return isEndangered(nextBoard, Coord.get(Col.F, player.getStartingRow()))
                        || isEndangered(nextBoard, Coord.get(Col.G, player.getStartingRow()))
                        || isEndangered(nextBoard, Coord.get(Col.E, player.getStartingRow()));
            default:
                throw new IllegalStateException("wtf");
        }
    }

    // check if field is endangered
    private boolean isEndangered(Board nextBoard, Coord coordsToCheck) {
        MovementFactory enemyFactory = MovementFactory.getFor(player.enemy());
        List<Movement> enemyPossibleMoves = enemyFactory.getPseudoLegalMoves(nextBoard);
        for (Movement enemyMove : enemyPossibleMoves) {
            Figure f = enemyMove.getResultingBoard().get(coordsToCheck);
            if (f != Figure.NONE && f.getPlayer()==player.enemy()) {
                return true;
            }
        }
        return false;
    }


}
