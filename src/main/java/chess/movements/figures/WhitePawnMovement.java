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
import java.util.function.Function;

/**
 * Created by Tom on 28.6.2015.
 */
class WhitePawnMovement extends PawnMovement {

    private static final EnumSet<Figure> PROMOTION_SET = EnumSet.of(Figure.WHITE_BISHOP, Figure.WHITE_KNIGHT, Figure.WHITE_QUEEN, Figure.WHITE_ROOK);
    private final Row startRow = Row._2;
    private final Row lastRow = Row._7;
    private final Function<Row, Row> directionMove = (r) -> r.north();
    private final Figure myFigure = Figure.WHITE_PAWN;


    WhitePawnMovement(Col col, Row row, Board board) {
        super(board, row, col);

    }


    @Override
    public Move forwardByOne() {
        if (row == lastRow) {
            return null;
        }
        Row targetRow = directionMove.apply(row);
        if (isEmpty(col, targetRow)) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.set(col, targetRow, myFigure);
            return new Move(Coord.get(col, row), Coord.get(col, targetRow), resultingBoard);
        }
        return null;
    }

    @Override
    public Move forwardByTwo() {
        if (row != startRow) {
            return null;
        }
        if (!isEmpty(col, directionMove.apply(row))) {
            return null;
        }
        Row targetRow = directionMove.apply(directionMove.apply(row));
        if (isEmpty(col, targetRow)) {
            Board resultingBoard = board.remove(col, row);
            resultingBoard = resultingBoard.set(col, targetRow, myFigure);
            resultingBoard = resultingBoard.allowEnPassant(col, targetRow);
            return new Move(Coord.get(col, row), Coord.get(col, targetRow), resultingBoard);
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
            if (directionMove.apply(m.getFrom().getRow()) == lastRow) {
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
        Row targetRow = directionMove.apply(row);
        return capture(targetCol, targetRow);
    }

    public Capture captureWest() {
        Col targetCol = col.west();
        Row targetRow = directionMove.apply(row);
        return capture(targetCol, targetRow);
    }

    private Capture capture(Col targetCol, Row targetRow) {
        if (targetCol.isValid() && targetRow.isValid()) {
            if (isEnemy(targetCol, targetRow)) {
                Board resultingBoard = board.remove(col, row);
                resultingBoard = resultingBoard.remove(targetCol, targetRow);
                resultingBoard = resultingBoard.set(targetCol, targetRow, myFigure);
                return new Capture(Coord.get(col, row), Coord.get(targetCol, targetRow), resultingBoard);
            }
        }
        return null;
    }

    @Override
    public EnPassant enPassantWest() {
        Row targetRow = directionMove.apply(row);
        Col targetCol = col.west();
        return enPassant(targetCol, targetRow);
    }


    @Override
    public EnPassant enPassantEast() {
        Row targetRow = directionMove.apply(row);
        Col targetCol = col.east();
        return enPassant(targetCol, targetRow);
    }

    private EnPassant enPassant(Col targetCol, Row targetRow) {
        if (targetCol.isValid() && targetRow.isValid()) {
            if (isEnemy(targetCol, targetRow.south())) {
                if (board.isEnPassantAllowed(targetCol, targetRow.south())) {
                    Board resultingBoard = board.remove(col, row);
                    resultingBoard = resultingBoard.remove(targetCol, targetRow.south());
                    resultingBoard = resultingBoard.set(targetCol, targetRow, myFigure);
                    return new EnPassant(Coord.get(col, row), Coord.get(targetCol, targetRow), resultingBoard);
                }
            }
        }
        return null;
    }

    @Override
    public List<Movement> promotions() {
        if (row == lastRow) {
            Row targetRow = directionMove.apply(row);
            if (isEmpty(col, row.north())) {
                List<Movement> promotions = new ArrayList<>();
                Board resultingBoard = board.remove(col, row);
                for (Figure f : PROMOTION_SET) {
                    resultingBoard.set(col, row.north(), f);
                    promotions.add(new Promotion(Coord.get(col, row), Coord.get(col, row.north()), resultingBoard));
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
