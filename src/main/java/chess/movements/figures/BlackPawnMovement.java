package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Row;
import chess.movements.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tom on 28.6.2015.
 */
class BlackPawnMovement extends PawnMovement {

    private static final EnumSet<Figure> PROMOTION_SET = EnumSet.of(Figure.BLACK_BISHOP, Figure.BLACK_KNIGHT, Figure.BLACK_QUEEN, Figure.BLACK_ROOK);

    BlackPawnMovement(Col col, Row row, Board board) {
        super(board, row, col);
    }


    @Override
    public Move forwardByOne() {
        if (row == Row._2) {
            return null;
        }
        Row targetRow = row.south();
        if (isEmpty(col, targetRow)) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.set(col, targetRow, Figure.BLACK_PAWN);
            return new Move(Coord.get(col, row), Coord.get(col, targetRow), resultingBoard);
        }
        return null;
    }

    @Override
    public Move forwardByTwo() {
        if (row != Row._7) {
            return null;
        }
        if (!isEmpty(col, row.south())) {
            return null;
        }
        if (isEmpty(col, row.south().south())) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.set(col, row.south().south(), Figure.BLACK_PAWN);
            resultingBoard = resultingBoard.allowEnPassant(col, row.south().south());
            return new Move(Coord.get(col, row), Coord.get(col, row.south().south()), resultingBoard);
        }
        return null;
    }

    public List<Movement> captures() {
        List<Capture> moves = new ArrayList<>();
        Capture ce = captureEast();
        if (ce != null) {
            moves.add(ce);
        }
        Capture cw = captureWest();
        if (cw != null) {
            moves.add(cw);
        }
        List<Movement> retList = new ArrayList<>(moves);
        Iterator<Capture> it = moves.iterator();
        while (it.hasNext()) {
            Capture m = it.next();
            if (m.getTo().getRow().ordinal() == 0) {
                // remove this capture
                retList.remove(m);
                // add as new with promotion
                for (Figure f : PROMOTION_SET) {
                    Board resultingBoard = board.remove(m.getTo().getCol(), m.getTo().getRow());
                    resultingBoard = resultingBoard.set(m.getTo().getCol(), m.getTo().getRow(), f);
                    Capture c = new Capture(m.getFrom(), m.getTo(), resultingBoard);
                    retList.add(c);
                }
            }
        }
        return retList;
    }

    public Capture captureEast() {
        Col targetCol = col.east();
        Row targetRow = row.south();
        return capture(targetCol, targetRow);
    }

    public Capture captureWest() {
        Col targetCol = col.west();
        Row targetRow = row.south();
        return capture(targetCol, targetRow);
    }

    private Capture capture(Col targetCol, Row targetRow) {
        if (targetCol.isValid() && targetRow.isValid()) {
            if (isEnemy(targetCol, targetRow)) {
                Board resultingBoard = board.remove(col, row);
                resultingBoard = resultingBoard.remove(targetCol, targetRow);
                resultingBoard = resultingBoard.set(targetCol, targetRow, Figure.BLACK_PAWN);
                return new Capture(Coord.get(col, row), Coord.get(targetCol, targetRow), resultingBoard);
            }
        }
        return null;
    }

    @Override
    public EnPassant enPassantWest() {
        Row targetRow = row.south();
        Col targetCol = col.west();
        return enPassant(targetCol, targetRow);
    }


    @Override
    public EnPassant enPassantEast() {
        Row targetRow = row.south();
        Col targetCol = col.east();
        return enPassant(targetCol, targetRow);
    }

    private EnPassant enPassant(Col targetCol, Row targetRow) {
        if (targetCol.isValid() && targetRow.isValid()) {
            if (isEnemy(targetCol, targetRow.north())) {
                if (board.isEnPassantAllowed(targetCol, targetRow.north())) {
                    Board resultingBoard = board.remove(col, row);
                    resultingBoard = resultingBoard.remove(targetCol, targetRow.north());
                    resultingBoard = resultingBoard.set(targetCol, targetRow, Figure.BLACK_PAWN);
                    return new EnPassant(Coord.get(col, row), Coord.get(targetCol, targetRow), resultingBoard);
                }
            }
        }
        return null;
    }

    @Override
    public List<Movement> promotions() {
        if (row == Row._2) {
            if (isEmpty(col, row.south())) {
                List<Movement> promotions = new ArrayList<>();
                Board resultingBoard = board.remove(col, row);
                for (Figure f : PROMOTION_SET) {
                    resultingBoard.set(col, row.south(), f);
                    promotions.add(new Promotion(Coord.get(col, row), Coord.get(col, row.south()), resultingBoard));
                }
                return promotions;
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
