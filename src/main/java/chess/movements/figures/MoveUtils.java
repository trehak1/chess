package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import chess.movements.Capture;
import chess.movements.Move;
import chess.movements.Movement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MoveUtils {

    private final Board board;
    private final Col col;
    private final Row row;
    private final Player player;

    public MoveUtils(Board board, Col col, Row row) {
        this.board = board;
        this.col = col;
        this.row = row;
        this.player = board.get(col, row).getPlayer();
    }

    public Coord myCoords() {
        return Coord.get(col, row);
    }

    public boolean isEmpty(Col col, Row row) {
        return board.get(col, row) == Figure.NONE;
    }

    public boolean isEnemy(Col col, Row row) {
        return board.get(col, row) != Figure.NONE && board.get(col, row).getPlayer() != player;
    }

    public boolean isMine(Col col, Row row) {
        return board.get(col, row) != Figure.NONE && board.get(col, row).getPlayer() == player;
    }

    public boolean isCapture(Coord coord) {
        return isEmpty(coord.getCol(), coord.getRow());
    }

    public List<Coord> getRayWest() {
        return getRay(Function.identity(), Col.WEST);
    }

    public List<Coord> getRayEast() {
        return getRay(Function.identity(), Col.EAST);
    }

    public List<Coord> getRayNorth() {
        return getRay(Row.NORTH, Function.identity());
    }

    public List<Coord> getRaySouth() {
        return getRay(Row.SOUTH, Function.identity());
    }

    public List<Coord> getRayNorthWest() {
        return getRay(Row.NORTH, Col.WEST);
    }

    public List<Coord> getRayNorthEast() {
        return getRay(Row.NORTH, Col.EAST);
    }

    public List<Coord> getRaySouthWest() {
        return getRay(Row.SOUTH, Col.WEST);
    }

    public List<Coord> getRaySouthEast() {
        return getRay(Row.SOUTH, Col.EAST);
    }


    public List<Coord> getRay(Function<Row, Row> rowModifyFunction, Function<Col, Col> colModifyFunction) {
        List<Coord> coords = new ArrayList<>();
        Row currentRow = rowModifyFunction.apply(row);
        Col currentCol = colModifyFunction.apply(col);
        // while on valid coords, go
        while (currentRow != Row.INVALID || currentCol != Col.INVALID) {
            // if enemy or ally, add field and end iteration
            if (isEnemy(currentCol, currentRow) || isMine(currentCol, currentRow)) {
                coords.add(Coord.get(currentCol, currentRow));
                break;
            } else if (isEmpty(currentCol, currentRow)) {
                // if is empty, add coord and move to next one
                coords.add(Coord.get(currentCol, currentRow));
                currentRow = rowModifyFunction.apply(row);
                currentCol = colModifyFunction.apply(col);
            } else {
                throw new IllegalArgumentException("wtf");
            }
        }
        return coords;
    }


    public Board moveTo(Coord c) {
        Figure figure = board.get(col, row);
        Board resultingBoard = board.remove(col, row);
        resultingBoard = resultingBoard.set(c.getCol(), c.getRow(), figure);
        return resultingBoard;
    }

    public Board capture(Coord c) {
        Figure figure = board.get(col, row);
        Board resultingBoard = board.remove(col, row);
        resultingBoard = resultingBoard.remove(c.getCol(), c.getRow());
        resultingBoard = resultingBoard.set(c.getCol(), c.getRow(), figure);
        return resultingBoard;
    }

   

}