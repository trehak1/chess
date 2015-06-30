package chess.movements.figures;

import chess.board.Board;
import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Player;
import chess.enums.Row;
import chess.movements.Castling;
import chess.movements.CastlingMove;
import chess.movements.Movement;
import chess.movements.MovementProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 29.6.2015.
 */
public class CastlingMoves implements MovementProducer {

    private final Player player;

    public CastlingMoves(Player player) {
        this.player = player;
    }

    @Override
    public List<Movement> getMovements(Board board) {
        List<Movement> list = new ArrayList<>();
        CastlingMove ksc = kingCastling(board, player);
        if (ksc != null) {
            list.add(ksc);
        }
        CastlingMove qsc = queenCastling(board, player);
        if (qsc != null) {
            list.add(qsc);
        }
        return list;
    }

    private CastlingMove kingCastling(Board board, Player player) {
        if (!board.isCastlingEnabled(player, Castling.KING_SIDE)) {
            return null;
        }
        Coord[] empty = new Coord[2];
        Row row;
        switch (player) {
            case WHITE:
                empty[0] = Coord.F1;
                empty[1] = Coord.G1;
                row = Row._1;
                break;
            case BLACK:
                empty[0] = Coord.F8;
                empty[1] = Coord.G8;
                row = Row._8;
                break;
            default:
                throw new IllegalArgumentException("wtf");
        }
        MoveUtils kingUtils = new MoveUtils(board, MoveUtils.locateKing(player, board));
        if (!kingUtils.isEmpty(empty[0]) || !kingUtils.isEmpty(empty[1])) {
            return null;
        }
        Board resultingBoard = kingUtils.moveTo(empty[1]);
        MoveUtils rookUtils = new MoveUtils(resultingBoard, Coord.get(Col.H, row));
        resultingBoard = rookUtils.moveTo(empty[0]);
        resultingBoard = resultingBoard.disableCastling(player, Castling.KING_SIDE)
                .disableCastling(player, Castling.QUEEN_SIDE);
        CastlingMove castlingMove = new CastlingMove(resultingBoard, Castling.KING_SIDE);
        return castlingMove;
    }

    private CastlingMove queenCastling(Board board, Player player) {
        if (!board.isCastlingEnabled(player, Castling.QUEEN_SIDE)) {
            return null;
        }
        Coord[] empty = new Coord[3];
        Row row;
        switch (player) {
            case WHITE:
                empty[0] = Coord.B1;
                empty[1] = Coord.C1;
                empty[2] = Coord.D1;
                row = Row._1;
                break;
            case BLACK:
                empty[0] = Coord.B8;
                empty[1] = Coord.C8;
                empty[2] = Coord.D8;
                row = Row._8;
                break;
            default:
                throw new IllegalArgumentException("wtf");
        }
        MoveUtils kingUtils = new MoveUtils(board, MoveUtils.locateKing(player, board));
        if (!kingUtils.isEmpty(empty[0]) || !kingUtils.isEmpty(empty[1]) || !kingUtils.isEmpty(empty[2])) {
            return null;
        }
        Board resultingBoard = kingUtils.moveTo(empty[1]);
        MoveUtils rookUtils = new MoveUtils(resultingBoard, Coord.get(Col.A, row));
        resultingBoard = rookUtils.moveTo(empty[2]);
        resultingBoard = resultingBoard.disableCastling(player, Castling.KING_SIDE)
                .disableCastling(player, Castling.QUEEN_SIDE);
        CastlingMove castlingMove = new CastlingMove(resultingBoard, Castling.KING_SIDE);
        return castlingMove;
    }
}
