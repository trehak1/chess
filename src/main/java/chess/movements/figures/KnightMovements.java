package chess.movements.figures;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.List;

public class KnightMovements implements MovementProducer {

    private final Player player;

    public KnightMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        for (Coord c : MoveUtils.locateAll(Figure.get(player, Piece.KNIGHT), board)) {
            movements.addAll(new KnightMovement(c.getCol(), c.getRow(), board).getMoves());
        }

        return movements;
    }
}
