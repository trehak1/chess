package chess.movements;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Piece;
import chess.enums.Player;
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
        producers.add(new CastlingMoves(player));
        producers.add(new KingMovements(player));
        producers.add(new KnightMovements(player));
        producers.add(new PawnMovements(player));
        producers.add(new QueenMovements(player));
        producers.add(new RookMovements(player));
    }

    private List<Movement> getPseudoLegalMoves(Board board) {
        List<Movement> list = new ArrayList<>();
        for (MovementProducer producer : producers) {
            list.addAll(producer.getMovements(board));
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
            if (isInCheck(nextBoard)) {
                it.remove();
            } else if ((m instanceof CastlingMove) && castlingFieldsInCheck((CastlingMove) m, nextBoard)) {
                it.remove();
            }
        }
        return list;
    }

    // check if any of castling field of interest is in check
    private boolean castlingFieldsInCheck(CastlingMove m, Board nextBoard) {
        // TODO complete - only fields used by king are to be checked
        return false;
    }

    // check if king is in check
    private boolean isInCheck(Board nextBoard) {
        // king coords
        Coord kingCoord = MoveUtils.locateKing(player, nextBoard);
        // move factory for enemy
        MovementFactory enemyFactory = MovementFactory.getFor(player.enemy());
        List<Movement> enemyPossibleMoves = enemyFactory.getPseudoLegalMoves(nextBoard);
        for (Movement enemyMove : enemyPossibleMoves) {
            if (enemyMove.getResultingBoard().get(kingCoord) != Figure.get(player, Piece.KING)) {
                return true;
            }
        }
        return false;
    }


}
