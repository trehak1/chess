package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tom on 27.6.2015.
 */
public class PawnMovements implements MovementProducer {

    private final Player player;

    public PawnMovements(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> movements = new ArrayList<>();
        for (Col col : Col.values()) {
            for (Row row : Row.values()) {
                Figure f = board.get(col, row);
                if (f.getPiece() == Piece.PAWN && f.getPlayer() == player) {
                    movements.addAll(movesFor(col, row, board));
                }
            }
        }
        return movements;
    }

    private Collection<? extends Movement> movesFor(Col col, Row row, Board board) {
        PawnMovement pawnMovement = new PawnMovement(col, row, board);
        List<Movement> list = new ArrayList<>();
        list.add(pawnMovement.forwardByOne());
        list.add(pawnMovement.forwardByTwo());
        list.add(pawnMovement.captures());
        list.add(pawnMovement.enPassants());
        list.add(pawnMovement.promotion());
        return list;
    }

    private static class PawnMovement {

        private final Col col;
        private final Row row;
        private final Board board;
        private final Player player;

        private PawnMovement(Col col, Row row, Board board) {
            this.col = col;
            this.row = row;
            this.player = board.get(col, row).getPlayer();
            if (player == Player.BLACK) {
                this.board = board.mirror();
            } else {
                this.board = board;
            }
        }


        public Movement forwardByOne() {
            int frontOrd = row.ordinal() + 1;

        }
    }

}
