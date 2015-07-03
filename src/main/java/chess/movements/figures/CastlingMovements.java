package chess.movements.figures;

import chess.board.Board;
import chess.enums.*;
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
        Castling ksc = createCastling(board, player, CastlingType.KING_SIDE);
        if (ksc != null ) {
            list.add(ksc);
        }
        Castling qsc = createCastling(board, player, CastlingType.QUEEN_SIDE);
        if (qsc != null) {
            list.add(qsc);
        }
        return list;
    }

    private boolean isAvailable(Board board, Player player, CastlingType castlingType) {
        // check if castling is enabled
        if (!board.isCastlingEnabled(player, castlingType)) {
            return false;
        }
        // check if all required fields are empty
        List<Coord> toBeEmpty = Lists.newArrayList(castlingType.getKingDestinationCoord(player), castlingType.getRookDestinationCoord(player));
        for (Col c : castlingType.getEmptyCols()) {
            toBeEmpty.add(Coord.get(c, player.getStartingRow()));
        }
        if (Iterables.any(toBeEmpty, (coord) -> board.get(coord)!= Figure.NONE)) {
            return false;
        }
        return true;
    }
    
    private Castling createCastling(Board board, Player player,CastlingType castlingType) {
        if(!isAvailable(board, player, castlingType)) {
            return null;
        }
        MoveUtils kingUtils = new MoveUtils(board, MoveUtils.locateKing(player, board));
        Board resultingBoard = kingUtils.moveTo(castlingType.getKingDestinationCoord(player));
        MoveUtils rookUtils = new MoveUtils(resultingBoard, castlingType.getRookStartingCoord(player));
        resultingBoard = rookUtils.moveTo(castlingType.getRookDestinationCoord(player));
        resultingBoard = resultingBoard.disableCastling(player, castlingType)
                .disableCastling(player, castlingType.other())
                .clearEnPassant();
        Castling castling = new Castling(resultingBoard, castlingType, MoveUtils.locateKing(player, board), castlingType.getKingDestinationCoord(player));
        return castling;
    }
    
}
