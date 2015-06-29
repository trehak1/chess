package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Tom on 28.6.2015.
 */
public class PawnMovement {

    private static final EnumSet<Figure> PROMOTION_SET = EnumSet.of(Figure.WHITE_BISHOP, Figure.WHITE_KNIGHT, Figure.WHITE_QUEEN, Figure.WHITE_ROOK);

    private static final Row WHITE_START_ROW = Row._2;
    private static final Row WHITE_LAST_ROW = Row._7;
    private static final Function<Row, Row> WHITE_MOVE_DIRECTION = (r) -> r.north();
    private static final Figure WHITE_FIGURE = Figure.WHITE_PAWN;

    private static final Row BLACK_START_ROW = Row._7;
    private static final Row BLACK_LAST_ROW = Row._2;
    private static final Function<Row, Row> BLACK_MOVE_DIRECTION = (r) -> r.south();
    private static final Figure BLACK_FIGURE = Figure.BLACK_PAWN;

    private final Row startRow;
    private final Row lastRow;
    private final Function<Row, Row> directionMove;
    private final Figure myFigure;

    private final Col col;
    private final Row row;
    private final Board board;
    private final Player player;
    private final MoveUtils moveUtils;

    public static PawnMovement getFor(Board board, Row row, Col col) {
        Player player = board.get(col, row).getPlayer();
        switch (player) {
            case WHITE:
                return new PawnMovement(board, row, col, WHITE_START_ROW, WHITE_LAST_ROW, WHITE_MOVE_DIRECTION, WHITE_FIGURE);
            case BLACK:
                return new PawnMovement(board, row, col, BLACK_START_ROW, BLACK_LAST_ROW, BLACK_MOVE_DIRECTION, BLACK_FIGURE);
            default:
                throw new IllegalStateException("wtf");
        }
    }

    private PawnMovement(Board board, Row row, Col col, Row startRow, Row lastRow, Function<Row, Row> directionMove, Figure myFigure) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.player = board.get(col, row).getPlayer();
        this.startRow = startRow;
        this.lastRow = lastRow;
        this.directionMove = directionMove;
        this.myFigure = myFigure;
        this.moveUtils = new MoveUtils(board, col, row);
    }

    public Move forwardByOne() {
        if (row == lastRow) {
            return null;
        }
        Row targetRow = directionMove.apply(row);
        if (moveUtils.isEmpty(col, targetRow)) {
            return new Move(Coord.get(col, row), Coord.get(col, targetRow), moveUtils.moveTo(Coord.get(col, targetRow)));
        }
        return null;
    }

    public Move forwardByTwo() {
        if (row != startRow) {
            return null;
        }
        if (!moveUtils.isEmpty(col, directionMove.apply(row))) {
            return null;
        }
        Row targetRow = directionMove.apply(directionMove.apply(row));
        if (moveUtils.isEmpty(col, targetRow)) {
            return new Move(Coord.get(col, row), Coord.get(col, targetRow), moveUtils.moveTo(Coord.get(col, targetRow)));
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
            if (moveUtils.isEnemy(targetCol, targetRow)) {
                return new Capture(Coord.get(col, row), Coord.get(targetCol, targetRow), moveUtils.capture(Coord.get(targetCol, targetRow)));
            }
        }
        return null;
    }


    public EnPassant enPassantWest() {
        Row targetRow = directionMove.apply(row);
        Col targetCol = col.west();
        return enPassant(targetCol, targetRow);
    }


    public EnPassant enPassantEast() {
        Row targetRow = directionMove.apply(row);
        Col targetCol = col.east();
        return enPassant(targetCol, targetRow);
    }

    private EnPassant enPassant(Col targetCol, Row targetRow) {
        if (targetCol.isValid() && targetRow.isValid()) {
            if (moveUtils.isEnemy(targetCol, row)) {
                if (board.isEnPassantAllowed(targetCol, row)) {
                    Board resultingBoard = board.remove(col, row);
                    resultingBoard = resultingBoard.remove(targetCol, row);
                    resultingBoard = resultingBoard.set(targetCol, targetRow, myFigure);
                    return new EnPassant(Coord.get(col, row), Coord.get(targetCol, targetRow), resultingBoard);
                }
            }
        }
        return null;
    }

    public List<Movement> promotions() {
        if (row == lastRow) {
            Row targetRow = directionMove.apply(row);
            if (moveUtils.isEmpty(col, targetRow)) {
                List<Movement> promotions = new ArrayList<>();
                Board resultingBoard = board.remove(col, row);
                for (Figure f : PROMOTION_SET) {
                    resultingBoard.set(col, targetRow, f);
                    promotions.add(new Promotion(Coord.get(col, row), Coord.get(col, targetRow), resultingBoard));
                }
                return promotions;
            }
        }
        return null;
    }


}
