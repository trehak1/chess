package chess.movements.figures;

import chess.board.Board;
import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementProducer;
import chess.movements.MovementType;

import java.util.ArrayList;
import java.util.List;


public class KingMovements implements MovementProducer {

    private final Player player;

    public KingMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        Coord kingCoords = MoveUtils.locateKing(player, board);
        List<Movement> movements = createMoves(kingCoords, board);
        return movements;
    }

    private List<Movement> createMoves(Coord kingCoords, Board board) {
        List<Movement> movements = new ArrayList<>();
        MoveUtils moveUtils = new MoveUtils(board, kingCoords);
        moveTo(kingCoords.east(), movements, moveUtils);
        moveTo(kingCoords.west(), movements, moveUtils);
        moveTo(kingCoords.north(), movements, moveUtils);
        moveTo(kingCoords.south(), movements, moveUtils);
        moveTo(kingCoords.southEast(), movements, moveUtils);
        moveTo(kingCoords.southWest(), movements, moveUtils);
        moveTo(kingCoords.northEast(), movements, moveUtils);
        moveTo(kingCoords.northWest(), movements, moveUtils);
        return movements;
    }

    private void moveTo(Coord targetCoords, List<Movement> movements, MoveUtils moveUtils) {
        if (targetCoords != Coord.INVALID) {
            MovementEffect me = new MovementEffect()
                    .disableCastling(CastlingType.KING_SIDE)
                    .disableCastling(CastlingType.QUEEN_SIDE);
            if (moveUtils.isEmpty(targetCoords)) {
                Movement movement = new Movement(MovementType.MOVE, moveUtils.myCoords(), targetCoords, me);
                movements.add(movement);
            } else if (moveUtils.isEnemy(targetCoords)) {
                Movement movement = new Movement(MovementType.CAPTURE, moveUtils.myCoords(), targetCoords, me);
                movements.add(movement);
            }
        }
    }

}
