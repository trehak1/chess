package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MoveUtils {

    private final Board board;
    private final Col col;
    private final Row row;
    private final Player player;

    public MoveUtils(Board board, Coord coord) {
        this(board, coord.getCol(), coord.getRow());
    }

    public MoveUtils(Board board, Col col, Row row) {
        this.board = board;
        this.col = col;
        this.row = row;
        this.player = board.get(Coord.get(col, row)).getPlayer();
    }

    public Coord myCoords() {
        return Coord.get(col, row);
    }
    
    public boolean isEnemy(Coord coord) {
        return isEnemy(coord.getCol(), coord.getRow());
    }
    
    public boolean isEnemy(Col col, Row row) {
        Preconditions.checkArgument(col.isValid());
        Preconditions.checkArgument(row.isValid());
        return board.get(Coord.get(col, row)) != Figure.NONE && board.get(Coord.get(col, row)).getPlayer() != player;
    }
    
    public boolean isMine(Col col, Row row) {
        Preconditions.checkArgument(col.isValid());
        Preconditions.checkArgument(row.isValid());
        return board.get(Coord.get(col, row)) != Figure.NONE && board.get(Coord.get(col, row)).getPlayer() == player;
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
        while (currentRow != Row.INVALID && currentCol != Col.INVALID) {
            // if enemy or ally, add field and end iteration
            if (isEnemy(currentCol, currentRow)) {
                coords.add(Coord.get(currentCol, currentRow));
                break;
            } else if (isMine(currentCol, currentRow)) {
                break;
            } else if (board.isEmpty(Coord.get(currentCol, currentRow))) {
                // if is empty, add coord and move to next one
                coords.add(Coord.get(currentCol, currentRow));
                currentRow = rowModifyFunction.apply(currentRow);
                currentCol = colModifyFunction.apply(currentCol);
            } else {
                throw new IllegalArgumentException("wtf");
            }
        }
        return coords;
    }
    
    public static Coord locateKing(Player player, Board board) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(board);
        List<Coord> coords = board.locateAll(Figure.get(player, Piece.KING));
        Preconditions.checkArgument(coords.size()==1);
        return coords.get(0);
    }

}
