package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementProducer;

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
//        board.get(c.west(), r)
        // TODO
    }
}
