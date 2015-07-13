package chess.movements;

import chess.board.Board;
import chess.board.CastlingRights;
import chess.enums.*;
import com.google.common.base.Preconditions;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementExecutor {


    private MovementExecutor() {

    }

    public static Board move(Board board, Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        Preconditions.checkArgument(board.get(movement.getFrom()).getPlayer() == board.getPlayerOnTurn(), "Trying to move enemy figure");

        Board mutated;

        switch (movement.getType()) {
            case MOVE:
                mutated = executeMove(board, movement);
                break;
            case CAPTURE:
                mutated = executeCapture(board, movement);
                break;
            case CASTLING:
                mutated = executeCastling(board, movement);
                break;
            case EN_PASSANT:
                mutated = executeEnPassant(board, movement);
                break;
            case PROMOTION:
                mutated = executePromotion(board, movement);
                break;
            case PROMOTION_CAPTURE:
                mutated = executePromotionCapture(board, movement);
                break;
            default:
                throw new IllegalStateException("wtf");
        }

        MovementEffect effect = movement.getMovementEffect();

        // allow en passant ?
        if (effect.getAllowEnPassant() != null) {
            mutated = mutated.allowEnPassant(effect.getAllowEnPassant());
        } else {
            mutated = mutated.clearEnPassant();
        }
        // disable castlings?
        for (Player p : Player.values()) {
            for (CastlingType ct : CastlingType.values()) {
                if (!effect.getDisableCastlings().isCastlingEnabled(p, ct)) {
                    mutated = mutated.disableCastling(p, ct);
                }
            }
        }
        // set next player
        mutated = mutated.setOnTurn(mutated.getPlayerOnTurn().enemy());

        return mutated;
    }

    private static Board executePromotionCapture(Board board, Movement movement) {
        // remove pawn
        Board mutated = board.remove(movement.getFrom());
        // remove figure in destination
        mutated = mutated.remove(movement.getTo());
        // set pawn to new destination as promoted piece
        mutated = mutated.set(movement.getTo(), Figure.get(board.getPlayerOnTurn(), movement.getMovementEffect().getPromotedTo()));
        return mutated;
    }

    private static Board executePromotion(Board board, Movement movement) {
        // remove pawn
        Board mutated = board.remove(movement.getFrom());
        // set it to new destination as promoted piece
        mutated = mutated.set(movement.getTo(), Figure.get(board.getPlayerOnTurn(), movement.getMovementEffect().getPromotedTo()));
        return mutated;
    }

    private static Board executeEnPassant(Board board, Movement movement) {
        // move pawn
        Board mutated = moveFigure(board, movement.getFrom(), movement.getTo());
        // remove captured en passant
        Coord captureCoord = getEnPassantCaptured(movement);
        mutated = mutated.remove(captureCoord);
        return mutated;
    }

    private static Board executeCastling(Board board, Movement movement) {
        CastlingType ct = CastlingType.fromKingDestCol(movement.getTo().getCol());
        Row castlingRow = board.get(movement.getFrom()).getPlayer().getStartingRow();
        Coord rookDestCoord = Coord.get(ct.getRookDestinationCol(), castlingRow);
        Coord rookStartCoord = Coord.get(ct.getRookStartingCol(), castlingRow);
        // move king
        Board mutated = moveFigure(board, movement.getFrom(), movement.getTo());
        // move rook
        mutated = moveFigure(mutated, rookStartCoord, rookDestCoord);
        // castling is disabled in method doMove
        return mutated;
    }

    private static Board executeCapture(Board board, Movement movement) {
        // remove target figure
        Board mutated = board.remove(movement.getTo());
        mutated = moveFigure(mutated, movement.getFrom(), movement.getTo());
        return mutated;
    }

    private static Board executeMove(Board board, Movement movement) {
        Board mutated = moveFigure(board, movement.getFrom(), movement.getTo());
        return mutated;
    }

    public static Board rollback(Board board, Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        Player boardTurn = board.getPlayerOnTurn();
        Player toUndo = board.get(movement.getTo()).getPlayer();
        Preconditions.checkArgument(boardTurn == toUndo.enemy(), "Trying to undo enemy move");

        Board mutated;

        switch (movement.getType()) {
            case MOVE:
                mutated = rollbackMove(board, movement);
                break;
            case CAPTURE:
                mutated = rollbackCapture(board, movement);
                break;
            case CASTLING:
                mutated = rollbackCastling(board, movement);
                break;
            case EN_PASSANT:
                mutated = rollbackEnPassant(board, movement);
                break;
            case PROMOTION:
                mutated = rollbackPromotion(board, movement);
                break;
            case PROMOTION_CAPTURE:
                mutated = rollbackPromotionCapture(board, movement);
                break;
            default:
                throw new IllegalStateException("wtf");
        }

        // disable en passant?
        if (movement.getMovementEffect().getAllowEnPassant() != null) {
            mutated = mutated.clearEnPassant();
        }

        // allow en passant
        if (movement.getMovementEffect().getDisableEnPassant() != null) {
            mutated = mutated.allowEnPassant(movement.getMovementEffect().getDisableEnPassant());
        }

        // repair castling rights
        CastlingRights cr = movement.getMovementEffect().getDisableCastlings().negate();
        for (CastlingType ctt : CastlingType.values()) {
            for (Player p : Player.values()) {
                if (cr.isCastlingEnabled(p, ctt)) {
                    mutated = mutated.enableCastling(p, ctt);
                }
            }
        }

        // set next player
        mutated = mutated.setOnTurn(boardTurn.enemy());
        return mutated;

    }

    private static Board rollbackPromotionCapture(Board board, Movement movement) {
        // get promoted piece
        Figure promoted = board.get(movement.getTo());
        // remove it
        Board mutated = board.remove(movement.getTo());
        // restore figure in destination
        mutated = mutated.set(movement.getTo(), Figure.get(promoted.getPlayer().enemy(), movement.getMovementEffect().getCaptured()));
        // set pawn to from
        mutated = mutated.set(movement.getFrom(), Figure.get(promoted.getPlayer(), Piece.PAWN));
        return mutated;
    }

    private static Board rollbackPromotion(Board board, Movement movement) {
        // get promoted piece
        Figure promoted = board.get(movement.getTo());
        // remove it
        Board mutated = board.remove(movement.getTo());
        // set pawn to from
        mutated = mutated.set(movement.getFrom(), Figure.get(promoted.getPlayer(), Piece.PAWN));
        return mutated;
    }

    private static Board rollbackEnPassant(Board board, Movement movement) {
        // get pawn
        Figure pawn = board.get(movement.getTo());
        // move it back
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        // put back captured en passant
        Coord captureCoord = getEnPassantCaptured(movement);
        mutated = mutated.set(captureCoord, Figure.get(pawn.getPlayer().enemy(), Piece.PAWN));
        // reallow en passant
        mutated = mutated.allowEnPassant(captureCoord);
        return mutated;
    }

    private static Board rollbackCastling(Board board, Movement movement) {
        CastlingType ct = CastlingType.fromKingDestCol(movement.getTo().getCol());
        // get player row
        Row castlingRow = board.get(movement.getTo()).getPlayer().getStartingRow();
        Coord rookDestCoord = Coord.get(ct.getRookDestinationCol(), castlingRow);
        Coord rookStartCoord = Coord.get(ct.getRookStartingCol(), castlingRow);
        // move king back
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        // move rook back
        mutated = moveFigure(mutated, rookDestCoord, rookStartCoord);
        return mutated;
    }

    private static Board rollbackCapture(Board board, Movement movement) {
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        Player capturedPieceOwner = mutated.get(movement.getFrom()).getPlayer().enemy();
        // put back captured figure
        mutated = mutated.set(movement.getTo(), Figure.get(capturedPieceOwner, movement.getMovementEffect().getCaptured()));
        return mutated;
    }

    private static Board rollbackMove(Board board, Movement movement) {
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        return mutated;
    }

    private static Coord getEnPassantCaptured(Movement m) {
        if (m.getType() != MovementType.EN_PASSANT) {
            throw new IllegalArgumentException("wtf");
        }
        return Coord.get(m.getTo().getCol(), m.getFrom().getRow());
    }

    private static Player getPlayer(Board board, Coord figureCoord) {
        return board.get(figureCoord).getPlayer();
    }

    private static Board moveFigure(Board board, Coord from, Coord to) {
        // get moved figure
        Figure moved = board.get(from);
        // remove it
        Board mutated = board.remove(from);
        // put it to original coords
        mutated = mutated.set(to, moved);
        return mutated;
    }

}
