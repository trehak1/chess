package chess.movements.transformers;

import chess.board.Board;
import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.*;

import java.util.List;

/**
 * Created by Tom on 30.6.2015.
 */
public class CoordinateNotationTransformer implements NotationTransformer {

    private final Board board;
    private final MovementFactory movementFactory;

    public CoordinateNotationTransformer(Board currentBoard, Player player) {
        this.board = currentBoard;
        this.movementFactory = MovementFactory.getFor(player);
    }

    @Override
    public String toNotation(Movement movement) {
        if(movement.getType() == MovementType.PROMOTION_CAPTURE || movement.getType() == MovementType.PROMOTION) {
            return notation(movement.getFrom(),movement.getTo(),movement.getMovementEffect().getPromotedTo().getPiece());
        } else {
            return notation(movement.getFrom(),movement.getTo());
        }
    }

    private String notation(Coord from, Coord to) {
        return from.name() + "-" + to.name();
    }

    private String notation(Coord from, Coord to, Piece promotionPiece) {
        char pp = pp(promotionPiece);
        return from.name() + "-" + to.name() + "[" + pp + "]";
    }

    private char pp(Piece promotionPiece) {
        switch (promotionPiece) {
            case PAWN:
                return 'p';
            case BISHOP:
                return 'b';
            case KING:
                return 'k';
            case QUEEN:
                return 'q';
            case ROOK:
                return 'r';
            case KNIGHT:
                return 'n';
            default:
                throw new IllegalStateException("wtf");
        }
    }

    @Override
    public Movement fromNotation(String notation) {
        List<Movement> moves = movementFactory.getMoves(board);
        for (Movement m : moves) {
            if (toNotation(m).equals(notation)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Coord coordFromNotation(String notation) {
        return Coord.valueOf(notation);
    }

    @Override
    public String coordToNotation(Coord coord) {
        return coord.name();
    }
}
