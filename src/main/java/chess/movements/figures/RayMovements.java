package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RayMovements implements MovementProducer {

    private final Player player;
    private final Piece piece;

    public RayMovements(Player player, Piece piece) {
        this.player = player;
        this.piece = piece;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> moves = new ArrayList<>();
        for (Col c : Col.validValues()) {
            for (Row r : Row.validValues()) {
                Figure f = board.get(c, r);
                if (f != Figure.NONE && f.getPlayer() == player && f.getPiece() == piece) {
                    moves.addAll(createMoves(board, c, r));
                }
            }
        }
        return moves;
    }

    protected abstract Collection<? extends Movement> createMoves(Board board, Col c, Row r);

    protected void processRayList(List<Movement> list, List<Coord> coords, MoveUtils moveUtils) {
        for (Coord c : coords) {
            // move
            if (moveUtils.isEmpty(c.getCol(), c.getRow())) {
                Move move = new Move(moveUtils.myCoords(), c, moveUtils.moveTo(c));
                list.add(move);
            } else if (moveUtils.isEnemy(c.getCol(), c.getRow())) {
                // capture
                Capture capture = new Capture(moveUtils.myCoords(), c, moveUtils.capture(c));
                list.add(capture);
            } else {
                throw new IllegalStateException("wtf");
            }
        }
    }

}