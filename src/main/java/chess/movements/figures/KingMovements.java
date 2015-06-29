package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.*;

import java.util.ArrayList;
import java.util.List;

public class KingMovements implements MovementProducer {

    private final Player player;

    public KingMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        for (Col c : Col.validValues()) {
            for (Row r : Row.validValues()) {
                Figure f = board.get(c, r);
                if (f != Figure.NONE && f.getPiece() == Piece.KING && f.getPlayer() == player) {
                    createMoves(movements, c, r, board);
                }
            }
        }
        return movements;
    }

    private void createMoves(List<Movement> movements, Col c, Row r, Board board) {
        MoveUtils moveUtils = new MoveUtils(board, c, r);
        Coord my = moveUtils.myCoords();
        moveTo(my.east(), movements, moveUtils);
        moveTo(my.west(), movements, moveUtils);
        moveTo(my.north(), movements, moveUtils);
        moveTo(my.south(), movements, moveUtils);
        moveTo(my.southEast(), movements, moveUtils);
        moveTo(my.southWest(), movements, moveUtils);
        moveTo(my.northEast(), movements, moveUtils);
        moveTo(my.northWest(), movements, moveUtils);
    }

    private void moveTo(Coord targetCoords, List<Movement> movements, MoveUtils moveUtils) {
        if (targetCoords != Coord.INVALID) {
            if (moveUtils.isEmpty(targetCoords)) {
                Move m = new Move(moveUtils.myCoords(), targetCoords, moveUtils.moveTo(targetCoords)
                        .disableCastling(player, Castling.QUEEN_SIDE)
                        .disableCastling(player, Castling.KING_SIDE));
                movements.add(m);
                return;
            } else if (moveUtils.isEnemy(targetCoords)) {
                Capture c = new Capture(moveUtils.myCoords(), targetCoords, moveUtils.capture(targetCoords)
                        .disableCastling(player, Castling.QUEEN_SIDE)
                        .disableCastling(player, Castling.KING_SIDE));
                movements.add(c);
                return;
            }
        }
    }

}
