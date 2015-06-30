package chess.movements.figures;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Player;
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
        Coord kingCoords = MoveUtils.locateKing(player, board);
        createMoves(movements, kingCoords, board);
        return movements;
    }

    private void createMoves(List<Movement> movements, Coord kingCoords, Board board) {
        MoveUtils moveUtils = new MoveUtils(board, kingCoords);
        moveTo(kingCoords.east(), movements, moveUtils);
        moveTo(kingCoords.west(), movements, moveUtils);
        moveTo(kingCoords.north(), movements, moveUtils);
        moveTo(kingCoords.south(), movements, moveUtils);
        moveTo(kingCoords.southEast(), movements, moveUtils);
        moveTo(kingCoords.southWest(), movements, moveUtils);
        moveTo(kingCoords.northEast(), movements, moveUtils);
        moveTo(kingCoords.northWest(), movements, moveUtils);
    }

    private void moveTo(Coord targetCoords, List<Movement> movements, MoveUtils moveUtils) {
        if (targetCoords != Coord.INVALID) {
            if (moveUtils.isEmpty(targetCoords)) {
                Move m = new Move(moveUtils.myCoords(), targetCoords, moveUtils.moveTo(targetCoords)
                        .disableCastling(player, Castling.QUEEN_SIDE)
                        .disableCastling(player, Castling.KING_SIDE));
                movements.add(m);
            } else if (moveUtils.isEnemy(targetCoords)) {
                Capture c = new Capture(moveUtils.myCoords(), targetCoords, moveUtils.capture(targetCoords)
                        .disableCastling(player, Castling.QUEEN_SIDE)
                        .disableCastling(player, Castling.KING_SIDE));
                movements.add(c);
            }
        }
    }

}
