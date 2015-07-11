package chess.movements;

import chess.board.Board;
import chess.enums.*;
import chess.movements.figures.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import java.util.*;
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
        Set<Coord> attackMapBefore = getAttackMap(board, player.enemy());
        while (it.hasNext()) {
            Movement m = it.next();
            MovementFactory enemyFactory = MovementFactory.getFor(player.enemy());

            // is this illegal castling out of check?
            if (m.getType() == MovementType.CASTLING) {
                if (shouldRemoveIllegalCastling(m, attackMapBefore)) {
                    it.remove();
                    continue;
                }
            }

            Board nextBoard = new MovementExecutor(board).doMove(m);
            Set<Coord> attackMapAfterMove = getAttackMap(nextBoard, player.enemy());

            if (shouldRemoveMoveToCheck(nextBoard, attackMapAfterMove)) {
                it.remove();
            }
//            nextBoard = new MovementExecutor(nextBoard).undoMove(m);
        }
        return list;
    }

    private boolean shouldRemoveIllegalCastling(Movement m, Set<Coord> attackMapBefore) {
        CastlingType ct = CastlingType.fromKingDestCol(m.getTo().getCol());
        if (Iterables.any(ct.requiredNotEndangered(player), (c) -> attackMapBefore.contains(c))) {
            return true;
        }
        return false;
    }

    private boolean shouldRemoveMoveToCheck(Board board, Set<Coord> attackMapAfterMove) {
        Coord kingCoord = MoveUtils.locateKing(player, board);
        return attackMapAfterMove.contains(kingCoord);
    }

    // build attack map
    private Set<Coord> getAttackMap(Board board, Player player) {
        Set<Coord> attackMap = EnumSet.noneOf(Coord.class);

        // factory for player, generate pseudo legal moves
        MovementFactory f = getFor(player);
        List<Movement> pseudoLegalMoves = f.getPseudoLegalMoves(board);

        // locate all pawn, find their move function
        Set<Coord> pawns = board.locateAll(Figure.get(player, Piece.PAWN));
        Function<Coord, Coord> moveFc = player == Player.WHITE ? Coord.NORTH : Coord.SOUTH;
        // for each pseudo legal moves, which is not a pawn (pawns endangers W and E squares in direction of their movements)
        Iterator<Movement> it = pseudoLegalMoves.iterator();
        while (it.hasNext()) {
            Movement m = it.next();
            if (!pawns.contains(m.getFrom())) {
                attackMap.add(m.getTo());
            }
        }
        for (Coord pawnCoord : pawns) {
            // pawns are special...
            Coord eW = pawnCoord.west().apply(moveFc);
            if (eW.isValid()) {
                attackMap.add(eW);
            }
            Coord eE = pawnCoord.east().apply(moveFc);
            if (eE.isValid()) {
                attackMap.add(eE);
            }
        }
        return attackMap;
    }

}
