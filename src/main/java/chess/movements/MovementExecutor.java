package chess.movements;

import chess.board.Board;
import chess.board.CastlingRights;
import chess.enums.*;
import com.google.common.base.Preconditions;

/**
 * Created by Tom on 3.7.2015.
 */
public class MovementExecutor {

	private final Board board;

	public MovementExecutor(Board currentBoard) {
		Preconditions.checkNotNull(currentBoard);
		this.board = currentBoard;
	}

	public Board doMove(Movement movement) {
		Preconditions.checkNotNull(movement, "Movement must not be null");
		Preconditions.checkArgument(board.get(movement.getFrom()).getPlayer() == board.getOnTurn(), "Trying to move enemy figure");

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
		mutated = mutated.setOnTurn(mutated.getOnTurn().enemy());
		return mutated;
	}

	private Board executePromotionCapture(Movement movement) {
		// get pawn
		Figure pawn = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// remove figure in destination
		mutated = mutated.remove(movement.getTo());
		// set pawn to new destination as promoted piece
		mutated = mutated.set(movement.getTo(), Figure.get(board.getOnTurn(), movement.getMovementEffect().getPromotedTo()));
		return mutated;
	}

	private Board executePromotion(Movement movement) {
		// get pawn
		Figure pawn = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// set it to new destination as promoted piece
		mutated = mutated.set(movement.getTo(), Figure.get(board.getOnTurn(), movement.getMovementEffect().getPromotedTo()));
		return mutated;
	}

	private Board executeEnPassant(Movement movement) {
		// get pawn
		Figure pawn = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// set it to new destination
		mutated = mutated.set(movement.getTo(), pawn);
		// remove captured en passant
		mutated = mutated.remove(movement.getTo().getCol(), movement.getFrom().getRow());
		return mutated;
	}

	private Board executeCastling(Movement movement) {
		CastlingType ct = CastlingType.fromKingDestCol(movement.getTo().getCol());
		// get king
		Figure king = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// move it to target
		mutated = mutated.set(movement.getTo(), king);
		// get rook
		Figure rook = mutated.get(ct.getRookStartingCol(), movement.getTo().getRow());
		// remove it
		mutated = mutated.remove(ct.getRookStartingCol(), movement.getTo().getRow());
		// move it to it's finished col
		mutated = mutated.set(ct.getRookDestinationCol(), movement.getTo().getRow(), rook);
		return mutated;
	}

	private Board executeCapture(Movement movement) {
		// get moved figure
		Figure moved = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// remove captured
		mutated = mutated.remove(movement.getTo());
		// set moved figure to new coords
		mutated = mutated.set(movement.getTo(), moved);
		return mutated;
	}

	private Board executeMove(Movement movement) {
		// get moved figure
		Figure moved = board.get(movement.getFrom());
		// remove it
		Board mutated = board.remove(movement.getFrom());
		// put it to new coords
		mutated = mutated.set(movement.getTo(), moved);
		return mutated;
	}

	public Board undoMove(Movement movement) {
		Preconditions.checkNotNull(movement, "Movement must not be null");
		Player boardTurn = board.getOnTurn();
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

		// repair castling rights
		CastlingRights cr = movement.getMovementEffect().getDisableCastlings();
		for (CastlingType ctt : CastlingType.values()) {
			if (!cr.isCastlingEnabled(board.getOnTurn().enemy(), ctt)) {
				mutated = mutated.enableCastling(board.getOnTurn().enemy(), ctt);
			}
		}

		// set next player
		mutated = mutated.setOnTurn(mutated.getOnTurn().enemy());
		return mutated;

	}

	private Board rollbackPromotionCapture(Movement movement) {
		// get promoted piece
		Figure promoted = board.get(movement.getTo());
		// remove it
		Board mutated = board.remove(movement.getFrom());
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
		Board mutated = board.remove(movement.getFrom());
		// set pawn to from
		mutated = mutated.set(movement.getFrom(), Figure.get(promoted.getPlayer(), Piece.PAWN));
		return mutated;
	}

	private Board rollbackEnPassant(Movement movement) {
		// get pawn
		Figure pawn = board.get(movement.getTo());
		// remove it
		Board mutated = board.remove(movement.getTo());
		// set it to prev destination
		mutated = mutated.set(movement.getFrom(), pawn);
		// put back captured en passant
		Coord captureCoord = Coord.get(movement.getTo().getCol(), movement.getFrom().getRow());
		mutated = mutated.set(captureCoord, Figure.get(pawn.getPlayer().enemy(), Piece.PAWN));
		mutated = mutated.allowEnPassant(captureCoord);
		return mutated;
	}

	private Board rollbackCastling(Movement movement) {
		CastlingType ct = CastlingType.fromKingDestCol(movement.getTo().getCol());
		// get king
		Figure king = board.get(movement.getTo());
		// remove it
		Board mutated = board.remove(movement.getTo());
		// get rook
		Figure rook = mutated.get(ct.getRookDestinationCol(), movement.getTo().getRow());
		// remove it
		mutated = mutated.remove(ct.getRookDestinationCol(), movement.getTo().getRow());
		// put back king, rook
		mutated = mutated.set(movement.getFrom(), king);
		mutated = mutated.set(ct.getRookStartingCol(), movement.getTo().getRow(), rook);
		return mutated;
	}

	private Board rollbackCapture(Movement movement) {
		// get moved figure
		Figure moved = board.get(movement.getTo());
		// remove it
		Board mutated = board.remove(movement.getTo());
		// put it to original coords
		mutated = mutated.set(movement.getFrom(), moved);
		// put back captured figure
		try {
			mutated = mutated.set(movement.getTo(), Figure.get(moved.getPlayer().enemy(), movement.getMovementEffect().getCaptured()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mutated;
	}

	private Board rollbackMove(Movement movement) {
		// get moved figure
		Figure moved = board.get(movement.getTo());
		// remove it
		Board mutated = board.remove(movement.getTo());
		// put it to original coords
		mutated = mutated.set(movement.getFrom(), moved);
		return mutated;
	}


}
