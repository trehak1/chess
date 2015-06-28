package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Row;
import chess.movements.EnPassant;
import chess.movements.Move;
import chess.movements.Promotion;

/**
 * Created by Tom on 28.6.2015.
 */
class BlackPawnMovement extends PawnMovement {

    BlackPawnMovement(Col col, Row row, Board board) {
        super(board, row, col);
    }


    @Override
    public Move forwardByOne() {
        if (row == Row._7) {
            return null;
        }
        if (isEmpty(col, row.north())) {
            return new Move(Coord.get(col, row), Coord.get(col, row.north()));
        }
        return null;
    }

    @Override
    public Move forwardByTwo() {
        if (row != Row._2) {
            return null;
        }
        if (isEmpty(col, row.north())) {
            if (isEmpty(col, row.north().north())) {
                return new Move(Coord.get(col, row), Coord.get(col, row.north().north()));
            }
        }
        return null;
    }

    @Override
    public Move captures() {
        if (col.east().isValid() && row.north().isValid()) {
            if (isEnemy(col.east(), row.north())) {
                return new Move(Coord.get(col, row), Coord.get(col.east(), row.north()));
            }
        }
        if (col.west().isValid() && row.north().isValid()) {
            if (isEnemy(col.west(), row.north())) {
                return new Move(Coord.get(col, row), Coord.get(col.west(), row.north()));
            }
        }
        return null;
    }

    @Override
    public EnPassant enPassants() {
        if (col.east().isValid()) {
            if (board.isEnPassantAllowed(col.east(), row)) {
                return new EnPassant(Coord.get(col, row), Coord.get(col.east(), row));
            }
        }
        if (col.west().isValid()) {
            if (board.isEnPassantAllowed(col.west(), row)) {
                return new EnPassant(Coord.get(col, row), Coord.get(col.west(), row));
            }
        }
        return null;
    }

    @Override
    public Promotion promotion() {
        if (row == Row._7) {
            if (isEmpty(col, row.north())) {
                return new Promotion(Coord.get(col, row), Coord.get(col, row.north()));
            }
        }
        return null;
    }


    private boolean isEmpty(Col col, Row row) {
        return board.get(col, row) == Figure.NONE;
    }

    private boolean isEnemy(Col col, Row row) {
        return board.get(col, row) != Figure.NONE && board.get(col, row).getPlayer() != player;
    }

}
