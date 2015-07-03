package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Piece;
import chess.enums.Player;
import chess.enums.Row;
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
        List<Movement> l = new ArrayList<>();
        processRayList(l, moveUtils.getRayNorth(), moveUtils, board);
        processRayList(l, moveUtils.getRayWest(), moveUtils, board);
        processRayList(l, moveUtils.getRayEast(), moveUtils, board);
        processRayList(l, moveUtils.getRaySouth(), moveUtils, board);
        processRayList(l, moveUtils.getRayNorthEast(), moveUtils, board);
        processRayList(l, moveUtils.getRayNorthWest(), moveUtils, board);
        processRayList(l, moveUtils.getRaySouthEast(), moveUtils, board);
        processRayList(l, moveUtils.getRaySouthWest(), moveUtils, board);
        return l;
    }


}
