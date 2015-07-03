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
				List<Movement> producerMovements = producer.getMovements(board);
				List<Movement> movements = disableCastlingOnRookCaptures(board, producerMovements);
				list.addAll(movements);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return list;
    }

    private List<Movement> disableCastlingOnRookCaptures(Board prevBoard,List<Movement> movements) {
        List<Movement> resList = new ArrayList<>();
        for(Movement m : movements) {
            Coord target = m.getTo();
            Figure f = prevBoard.get(target);
			// target square is rook
			if (f.getPiece() == Piece.ROOK) {
			// in it's starting row
               	if(target.getRow() == f.getPlayer().getStartingRow()) {
					if(!(m instanceof Capture)) {
						throw new IllegalStateException("wtf");
					}
                	CastlingType ct = target.getCol() == Col.A ? CastlingType.QUEEN_SIDE : CastlingType.KING_SIDE;
                    Capture nc = new Capture(m.getFrom(), target, m.getResultingBoard().disableCastling(f.getPlayer(), ct));
                    resList.add(nc);
					continue;
				}
			}
			resList.add(m);
        }
        return resList;
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
    private boolean castlingFieldEndangered(Castling m, Board b) {
        Preconditions.checkNotNull(m);
        Preconditions.checkNotNull(b);
        Preconditions.checkArgument(m.getType() == CastlingType.KING_SIDE || m.getType() == CastlingType.QUEEN_SIDE);
        CastlingType ct = m.getType();
        List<Coord> mustNotBeEndangered = Lists.newArrayList();
        // all empty fields must not be in check
        ct.getEmptyCols().forEach((c)->mustNotBeEndangered.add(Coord.get(c,player.getStartingRow())));
        // original king field must not be in check
        mustNotBeEndangered.add(Coord.get(Col.E, player.getStartingRow()));
        // ending king field must not be in check
        mustNotBeEndangered.add(ct.getKingDestinationCoord(player));
        return Iterables.any(mustNotBeEndangered,(c)->isEndangered(b,c));
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
