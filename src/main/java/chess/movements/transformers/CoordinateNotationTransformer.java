package chess.movements.transformers;

import chess.board.Board;
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
        if (movement instanceof CastlingMove) {
            CastlingMove cm = (CastlingMove) movement;
            if (cm.getType() == Castling.KING_SIDE) {
                return notation(cm.getOriginalKingCoord(), cm.getOriginalKingCoord().east().east());
            } else if (cm.getType() == Castling.QUEEN_SIDE) {
                return notation(cm.getOriginalKingCoord(), cm.getOriginalKingCoord().west().west());
            } else {
                throw new IllegalStateException("wtf");
            }
        } else if (movement instanceof Capture) {
            Capture c = (Capture) movement;
            return notation(c.getFrom(), c.getTo());
        } else if (movement instanceof Move) {
            Move m = (Move) movement;
            return notation(m.getFrom(), m.getTo());
        } else if (movement instanceof Promotion) {
            Promotion p = (Promotion) movement;
            return notation(p.getFrom(), p.getTo(), p.getPromotedTo().getPiece());
        } else {
            throw new IllegalArgumentException("wtf " + movement.getClass());
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
}
