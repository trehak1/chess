package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Player;
import chess.enums.CastlingType;
import chess.movements.Castling;
import chess.movements.Movement;
import chess.movements.MovementProducer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMovements implements MovementProducer {

    private final Player player;

    public CastlingMovements(Player player) {
        Preconditions.checkNotNull(player);
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> list = new ArrayList<>();
        Castling ksc = kingCastling(board, player);
        if (ksc != null) {
            list.add(ksc);
        }
        Castling qsc = queenCastling(board, player);
        if (qsc != null) {
            list.add(qsc);
        }
        return list;
    }

    private Castling kingCastling(Board board, Player player) {
        if (!board.isCastlingEnabled(player, CastlingType.KING_SIDE)) {
            return null;
        }
        List<Coord> toBeEmpty = Lists.newArrayList();
        for (Col c : CastlingType.KING_SIDE.getCols()) {
            toBeEmpty.add(Coord.get(c, player.getStartingRow()));
        }
        MoveUtils kingUtils = new MoveUtils(board, MoveUtils.locateKing(player, board));
        if (Iterables.any(toBeEmpty, (coord) -> !kingUtils.isEmpty(coord))) {
            return null;
        }
        Board resultingBoard = kingUtils.moveTo(toBeEmpty.get(1));
        MoveUtils rookUtils = new MoveUtils(resultingBoard, Coord.get(Col.H, player.getStartingRow()));
        resultingBoard = rookUtils.moveTo(toBeEmpty.get(0));
        resultingBoard = resultingBoard.disableCastling(player, CastlingType.KING_SIDE)
                .disableCastling(player, CastlingType.QUEEN_SIDE)
                .clearEnPassant();
        Castling castling = new Castling(resultingBoard, CastlingType.KING_SIDE, MoveUtils.locateKing(player, board), toBeEmpty.get(1));
        return castling;
    }

    private Castling queenCastling(Board board, Player player) {
        if (!board.isCastlingEnabled(player, CastlingType.QUEEN_SIDE)) {
            return null;
        }
        List<Coord> toBeEmpty = Lists.newArrayList();
        for (Col c : CastlingType.QUEEN_SIDE.getCols()) {
            toBeEmpty.add(Coord.get(c, player.getStartingRow()));
        }
        MoveUtils kingUtils = new MoveUtils(board, MoveUtils.locateKing(player, board));
        if (Iterables.any(toBeEmpty, (coord) -> !kingUtils.isEmpty(coord))) {
            return null;
        }
        Board resultingBoard = kingUtils.moveTo(toBeEmpty.get(1));
        MoveUtils rookUtils = new MoveUtils(resultingBoard, Coord.get(Col.A, player.getStartingRow()));
        resultingBoard = rookUtils.moveTo(toBeEmpty.get(2));
        resultingBoard = resultingBoard.disableCastling(player, CastlingType.KING_SIDE)
                .disableCastling(player, CastlingType.QUEEN_SIDE)
                .clearEnPassant();
        Castling castling = new Castling(resultingBoard, CastlingType.KING_SIDE, MoveUtils.locateKing(player, board), toBeEmpty.get(1));
        return castling;
    }
}
