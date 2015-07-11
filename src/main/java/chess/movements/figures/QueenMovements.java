package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovements extends RayMovements implements MovementProducer {

    public QueenMovements(Player player) {
        super(player, Piece.QUEEN);
    }

    @Override
    protected Collection<Movement> createMoves(Board board, Col c, Row r) {
        MoveUtils moveUtils = new MoveUtils(board, c, r);
        Coord myCoord = Coord.get(c, r);
        List<Movement> l = new ArrayList<>();
        processRayList(l, moveUtils.getRayNorth(), myCoord, board);
        processRayList(l, moveUtils.getRayWest(), myCoord, board);
        processRayList(l, moveUtils.getRayEast(), myCoord, board);
        processRayList(l, moveUtils.getRaySouth(), myCoord, board);
        processRayList(l, moveUtils.getRayNorthEast(), myCoord, board);
        processRayList(l, moveUtils.getRayNorthWest(), myCoord, board);
        processRayList(l, moveUtils.getRaySouthEast(), myCoord, board);
        processRayList(l, moveUtils.getRaySouthWest(), myCoord, board);
        return l;
    }


}
