package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovements extends RayMovements implements MovementProducer {

    public RookMovements(Player player) {
        super(player, Piece.ROOK);
    }

    @Override
    protected Collection<Movement> createMoves(Board board, Col c, Row r) {
        MoveUtils moveUtils = new MoveUtils(board, c, r);
        List<Movement> l = new ArrayList<>();
        processRayList(l, moveUtils.getRayNorth(), moveUtils);
        processRayList(l, moveUtils.getRayWest(), moveUtils);
        processRayList(l, moveUtils.getRayEast(), moveUtils);
        processRayList(l, moveUtils.getRaySouth(), moveUtils);
        return addCastlingChangeInformation(l, moveUtils);
    }

    private Collection<Movement> addCastlingChangeInformation(List<Movement> l, MoveUtils moveUtils) {
        List<Movement> modified = new ArrayList<>();
        for (Movement m : l) {
            if (m instanceof Move) {
                Move move = (Move) m;
                modified.add(new Move(move.getFrom(), move.getTo(), modify(move.getFrom(), move.getResultingBoard())));
            } else if (m instanceof Capture) {
                Capture c = (Capture) m;
                modified.add(new Capture(c.getFrom(), c.getTo(), modify(c.getFrom(), c.getResultingBoard())));
            } else {
                throw new IllegalArgumentException("wtf");
            }
        }
        return modified;
    }

    private Board modify(Coord from, Board board) {
        switch (player) {
            case WHITE:
                if (from == Coord.A1) {
                    return board.disableCastling(player, Castling.QUEEN_SIDE);
                } else if (from == Coord.A8) {
                    return board.disableCastling(player, Castling.KING_SIDE);
                } else {
                    return board;
                }
            case BLACK:
                if (from == Coord.H1) {
                    return board.disableCastling(player, Castling.QUEEN_SIDE);
                } else if (from == Coord.H8) {
                    return board.disableCastling(player, Castling.KING_SIDE);
                } else {
                    return board;
                }
            default:
                throw new IllegalStateException("wtf");
        }
    }


}
