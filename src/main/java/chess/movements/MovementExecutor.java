package chess.movements;

import chess.board.Board;
import chess.board.CastlingRights;
import chess.board.ImmutableBoard;
import chess.enums.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementExecutor {

    private final Board board;
    private final List<Movement> moves = Lists.newArrayList();

    public MovementExecutor(Board currentBoard) {
        Preconditions.checkNotNull(currentBoard);
        this.board = currentBoard;
    }

    public boolean canUndo() {
        return moves.size() > 0;
    }

    public Board undo() {
        if (!canUndo()) {
            throw new IllegalStateException("wtf");
        }
        int lastIndex = moves.size() - 1;
        Board undone = undoMove(moves.get(lastIndex));
        moves.remove(lastIndex);
        return undone;
    }

    public Board doMove(Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        Preconditions.checkArgument(board.get(movement.getFrom()).getPlayer() == board.getPlayerOnTurn(), "Trying to move enemy figure");

        Board mutated;

        switch (movement.getType()) {
            case MOVE:
                mutated = executeMove(movement);
                break;
            case CAPTURE:
                mutated = executeCapture(movement);
                break;
            case CASTLING:
                mutated = executeCastling(movement);
                break;
            case EN_PASSANT:
                mutated = executeEnPassant(movement);
                break;
            case PROMOTION:
                mutated = executePromotion(movement);
                break;
            case PROMOTION_CAPTURE:
                mutated = executePromotionCapture(movement);
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

        moves.add(movement);

        return mutated;
    }

    private Board executePromotionCapture(Movement movement) {
        // remove pawn
        Board mutated = board.remove(movement.getFrom());
        // remove figure in destination
        mutated = mutated.remove(movement.getTo());
        // set pawn to new destination as promoted piece
        mutated = mutated.set(movement.getTo(), Figure.get(board.getPlayerOnTurn(), movement.getMovementEffect().getPromotedTo()));
        return mutated;
    }

    private Board executePromotion(Movement movement) {
        // remove pawn
        Board mutated = board.remove(movement.getFrom());
        // set it to new destination as promoted piece
        mutated = mutated.set(movement.getTo(), Figure.get(board.getPlayerOnTurn(), movement.getMovementEffect().getPromotedTo()));
        return mutated;
    }

    private Board executeEnPassant(Movement movement) {
        // move pawn
        Board mutated = moveFigure(board, movement.getFrom(), movement.getTo());
        // remove captured en passant
        Coord captureCoord = getEnPassantCaptured(movement);
        mutated = mutated.remove(captureCoord);
        return mutated;
    }

    private Board executeCastling(Movement movement) {
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

    private Board executeCapture(Movement movement) {
        // remove target figure
        Board mutated = board.remove(movement.getTo());
        mutated = moveFigure(mutated, movement.getFrom(), movement.getTo());
        return mutated;
    }

    private Board executeMove(Movement movement) {
        Board mutated = moveFigure(board, movement.getFrom(), movement.getTo());
        return mutated;
    }

    private Board undoMove(Movement movement) {
        Preconditions.checkNotNull(movement, "Movement must not be null");
        Player boardTurn = board.getPlayerOnTurn();
        Player toUndo = board.get(movement.getTo()).getPlayer();
        Preconditions.checkArgument(boardTurn == toUndo.enemy(), "Trying to undo enemy move");

        Board mutated;

        switch (movement.getType()) {
            case MOVE:
                mutated = rollbackMove(movement);
                break;
            case CAPTURE:
                mutated = rollbackCapture(movement);
                break;
            case CASTLING:
                mutated = rollbackCastling(movement);
                break;
            case EN_PASSANT:
                mutated = rollbackEnPassant(movement);
                break;
            case PROMOTION:
                mutated = rollbackPromotion(movement);
                break;
            case PROMOTION_CAPTURE:
                mutated = rollbackPromotionCapture(movement);
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

    private Board rollbackPromotionCapture(Movement movement) {
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

    private Board rollbackPromotion(Movement movement) {
        // get promoted piece
        Figure promoted = board.get(movement.getTo());
        // remove it
        Board mutated = board.remove(movement.getTo());
        // set pawn to from
        mutated = mutated.set(movement.getFrom(), Figure.get(promoted.getPlayer(), Piece.PAWN));
        return mutated;
    }

    private Board rollbackEnPassant(Movement movement) {
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

    private Board rollbackCastling(Movement movement) {
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

    private Board rollbackCapture(Movement movement) {
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        // put back captured figure
        mutated = mutated.set(movement.getTo(), Figure.get(getPlayer(board, movement.getFrom()).enemy(), movement.getMovementEffect().getCaptured()));
        return mutated;
    }

    private Board rollbackMove(Movement movement) {
        Board mutated = moveFigure(board, movement.getTo(), movement.getFrom());
        return mutated;
    }

    private Coord getEnPassantCaptured(Movement m) {
        if (m.getType() != MovementType.EN_PASSANT) {
            throw new IllegalArgumentException("wtf");
        }
        return Coord.get(m.getTo().getCol(), m.getFrom().getRow());
    }

    private Player getPlayer(Board board, Coord figureCoord) {
        return board.get(figureCoord).getPlayer();
    }

    private Board moveFigure(Board board, Coord from, Coord to) {
        // get moved figure
        Figure moved = board.get(from);
        // remove it
        Board mutated = board.remove(from);
        // put it to original coords
        mutated = mutated.set(to, moved);
        return mutated;
    }

}
