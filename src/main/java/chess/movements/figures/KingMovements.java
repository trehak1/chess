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
        moveTo(kingCoords.east(), movements, kingCoords, board);
        moveTo(kingCoords.west(), movements, kingCoords, board);
        moveTo(kingCoords.north(), movements, kingCoords, board);
        moveTo(kingCoords.south(), movements, kingCoords, board);
        moveTo(kingCoords.southEast(), movements, kingCoords, board);
        moveTo(kingCoords.southWest(), movements, kingCoords, board);
        moveTo(kingCoords.northEast(), movements, kingCoords, board);
        moveTo(kingCoords.northWest(), movements, kingCoords, board);
        return movements;
    }

    private void moveTo(Coord targetCoords, List<Movement> movements, Coord kingCoords, Board board) {
        if (targetCoords != Coord.INVALID) {
            MovementEffect me = new MovementEffect()
                    .disableCastlingIfAllowed(board, CastlingType.KING_SIDE, player)
                    .disableCastlingIfAllowed(board, CastlingType.QUEEN_SIDE, player)
                    .disableEnPassantIfAllowed(board);
            if (board.isEmpty(targetCoords)) {
                Movement movement = new Movement(MovementType.MOVE, kingCoords, targetCoords, me);
                movements.add(movement);
            } else if (board.isPlayers(kingCoords, player.enemy())) {
                Movement movement = new Movement(MovementType.CAPTURE, kingCoords, targetCoords, me.captured(board.get(targetCoords).getPiece()));
                movements.add(movement);
            }
        }
    }

}
