package chess.movements;

import chess.board.Board;
import chess.enums.Player;
import chess.movements.figures.*;

import java.util.ArrayList;
import java.util.List;

public class MovementFactory {

    private final List<MovementProducer> producers = new ArrayList<>();

    public MovementFactory(Player player) {
        producers.add(new BishopMovements(player));
        producers.add(new CastlingMoves(player));
        producers.add(new KingMovements(player));
        producers.add(new KnightMovements(player));
        producers.add(new PawnMovements(player));
        producers.add(new QueenMovements(player));
        producers.add(new RookMovements(player));
    }

    public List<Movement> getMoves(Board board) {
        List<Movement> list = new ArrayList<>();
        for (MovementProducer producer : producers) {
            list.addAll(producer.getMovements(board));
        }
        return filterOutIllegalMoves(list);
    }

    private List<Movement> filterOutIllegalMoves(List<Movement> list) {
        // TODO filter out illegal moves (leading to check, castlings going through check)
        return list;
    }


}
