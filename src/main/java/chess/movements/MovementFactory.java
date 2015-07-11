package chess.movements;

import chess.board.Board;
import chess.enums.*;
import chess.movements.figures.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

    public List<Movement> getPseudoLegalMoves(Board board) {
        List<Movement> list = new ArrayList<>();
        for (MovementProducer producer : producers) {
            try {
                List<Movement> producerMovements = producer.getMovements(board);
                List<Movement> movements = disableCastlingOnRookCaptures(board, producerMovements);
                list.addAll(movements);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<Movement> disableCastlingOnRookCaptures(Board board, List<Movement> movements) {
        List<Movement> resList = new ArrayList<>();
        for (Movement m : movements) {
//            if (m.getFrom() == Coord.A1 && m.getTo() == Coord.A8 && m.getType() == MovementType.CAPTURE) {
//                System.out.println("fsd");
//            }
            // must be a rook capture from it's starting location
            if (!isRookCaptureInStartingLocation(board, m)) {
                resList.add(m);
                continue;
            }
            if (m.getTo().getCol() == Col.A) {
                Movement disabled = disableCastling(CastlingType.QUEEN_SIDE, m, board);
                resList.add(disabled);
                continue;
            } else if (m.getTo().getCol() == Col.H) {
                Movement disabled = disableCastling(CastlingType.KING_SIDE, m, board);
                resList.add(disabled);
                continue;
            } else {
                resList.add(m);
                continue;
            }
        }
        return resList;
    }

    private Movement disableCastling(CastlingType ct, Movement m, Board board) {
        Figure captured = board.get(m.getTo());
        if (board.getCastlingRights().isCastlingEnabled(captured.getPlayer(), ct)) {
            MovementEffect me = m.getMovementEffect().disableCastlingIfAllowed(board, ct, captured.getPlayer());
            Movement movement = new Movement(m.getType(), m.getFrom(), m.getTo(), me);
            return movement;
        } else {
            return m;
        }
    }

    private boolean isRookCaptureInStartingLocation(Board board, Movement m) {
        if (m.getType() != MovementType.CAPTURE && m.getType() != MovementType.PROMOTION_CAPTURE) {
            return false;
        }
        Figure capturedFigure = board.get(m.getTo());
        if (capturedFigure.getPiece() != Piece.ROOK) {
            return false;
        } else {
            if (m.getMovementEffect().getCaptured() != Piece.ROOK) {
                throw new IllegalStateException("wtf");
            }
            return m.getTo().getRow() == capturedFigure.getPlayer().getStartingRow();
        }
    }

    public List<Movement> getMoves(Board board) {
        List<Movement> pseudoLegalMoves = getPseudoLegalMoves(board);
        return filterOutIllegalMoves(board, pseudoLegalMoves);
    }

    // TODO CHECK FOR CHECK WITHOUT CREATING NEW BOARD (expensive)
    public List<Movement> filterOutIllegalMoves(Board board, List<Movement> list) {
        Iterator<Movement> it = list.iterator();
        while (it.hasNext()) {
            Movement m = it.next();
            MovementFactory enemyFactory = MovementFactory.getFor(player.enemy());

            // is this illegal castling out of check?
            if(m.getType() == MovementType.CASTLING) {
                if (shouldRemoveIllegalCastling(board, m, enemyFactory.getPseudoLegalMoves(board))) {
                    it.remove();
                    continue;
                }
            }

            Board nextBoard = new MovementExecutor(board).doMove(m);
            List<Movement> enemyPossibleMoves = enemyFactory.getPseudoLegalMoves(nextBoard);

            if(shouldRemoveMoveToCheck(nextBoard, enemyPossibleMoves)) {
                it.remove();
            }
//            nextBoard = new MovementExecutor(nextBoard).undoMove(m);
        }
        return list;
    }

    private boolean shouldRemoveIllegalCastling(Board board, Movement m, List<Movement> enemyPossibleMoves) {
        Preconditions.checkNotNull(m);
        if (m.getType() != MovementType.CASTLING) {
            return false;
        }
        CastlingType ct = CastlingType.fromKingDestCol(m.getTo().getCol());
        if (Iterables.any(ct.requiredNotEndangered(player), (c) -> isEndangered(c, enemyPossibleMoves))) {
            return true;
        }
        // here is a bugfix for castling I think
        // pawn actually endangers not the field of it's movement BUT the fields to the east and west in front of him
        // lets do the check
        // 1) locate enemy pawns
        Set<Coord> enemyPawns = board.locateAll(Figure.get(player.enemy(), Piece.PAWN));
        // 2) get coords endagered by them (empty and in front and to the east and west)
        Function<Coord, Coord> moveFc = player.enemy() == Player.WHITE ? Coord.NORTH : Coord.SOUTH;
        List<Coord> endangerd = Lists.newArrayList();
        enemyPawns.forEach((pawnCoord) -> {
            Coord eW = pawnCoord.west().apply(moveFc);
            Coord eE = pawnCoord.east().apply(moveFc);
            if(eW.isValid()) {
                endangerd.add(eW);
            }
            if(eE.isValid()) {
                endangerd.add(eE);
            }
        });
        // 3) check if required not endangered fields are not under attack
        if(Iterables.any(ct.requiredNotEndangered(player),(c)->endangerd.contains(c))) {
            return true;
        }
        return false;
    }

    private boolean shouldRemoveMoveToCheck(Board board, List<Movement> enemyPossibleMoves) {
        Coord kingCoord = MoveUtils.locateKing(player, board);
        if (isEndangered(kingCoord, enemyPossibleMoves)) {
            return true;
        }
        return false;
    }

    // check if field is endangered
    private boolean isEndangered(Coord coordsToCheck, List<Movement> enemyPossibleMoves) {
        return Iterables.any(enemyPossibleMoves, (m) -> m.getTo() == coordsToCheck);
    }


}
