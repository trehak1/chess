package chess.movements.figures;

import chess.board.Board;
import chess.enums.Coord;
import chess.enums.Piece;
import chess.enums.Player;
import chess.movements.Movement;
import chess.movements.MovementEffect;
import chess.movements.MovementType;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Tom on 28.6.2015.
 */
class PawnMovement {

    private static final EnumSet<Piece> PROMOTION_SET = EnumSet.of(Piece.BISHOP, Piece.KNIGHT, Piece.QUEEN, Piece.ROOK);
    private static final Function<Coord, Coord> WHITE_MOVE_DIRECTION = (c) -> c.north();
    private static final Function<Coord, Coord> BLACK_MOVE_DIRECTION = (c) -> c.south();

    private final Function<Coord, Coord> directionMove;
    private final Coord myCoord;
    private final Board board;
    private final Player player;

    public static PawnMovement getFor(Board board, Coord coord) {
        Player player = board.get(coord).getPlayer();
        switch (player) {
            case WHITE:
                return new PawnMovement(board, coord, WHITE_MOVE_DIRECTION);
            case BLACK:
                return new PawnMovement(board, coord, BLACK_MOVE_DIRECTION);
            default:
                throw new IllegalStateException("wtf");
        }
    }

    private PawnMovement(Board board, Coord myCoord, Function<Coord, Coord> directionMove) {
        this.board = board;
        this.myCoord = myCoord;
        this.player = board.get(myCoord).getPlayer();
        this.directionMove = directionMove;
    }

    public Movement forwardByOne() {
        if (myCoord.apply(directionMove).getRow() == player.enemy().getStartingRow()) {
            return null;
        }
        Coord target = myCoord.apply(directionMove);
        if (board.isEmpty(target)) {
            return new Movement(MovementType.MOVE, myCoord, target, new MovementEffect().disableEnPassantIfAllowed(board));
        }
        return null;
    }

    public Movement forwardByTwo() {
        Coord startingCoord = Coord.get(myCoord.getCol(), player.getStartingRow()).apply(directionMove);
        // not on start ?
        if (myCoord != startingCoord) {
            return null;
        }
        
        Coord intermediate = myCoord.apply(directionMove);
        Coord target = intermediate.apply(directionMove);
        if (board.isEmpty(intermediate)) {
            if (board.isEmpty(target)) {
                return new Movement(MovementType.MOVE, myCoord, target, new MovementEffect().allowEnPassant(target).disableEnPassantIfAllowed(board));
            }
        }
        return null;
    }

    public List<Movement> captures() {
        List<Movement> moves = new ArrayList<>();
        moves.addAll(captureEast());
        moves.addAll(captureWest());
        return moves;
    }

    public List<Movement> captureEast() {
        return capture(myCoord.apply(directionMove).east());
    }

    public List<Movement> captureWest() {
        return capture(myCoord.apply(directionMove).west());
    }

    private List<Movement> capture(Coord target) {
        if (!target.isValid()) {
            return Lists.newArrayList();
        }
        if (board.isPlayers(target, player.enemy())) {
            Piece enemyPiece = board.get(target).getPiece();
            if (myCoord.apply(directionMove).getRow() == player.enemy().getStartingRow()) {
                List<Movement> res = Lists.newArrayList();
                for (Piece p : PROMOTION_SET) {
                    res.add(new Movement(MovementType.PROMOTION_CAPTURE, myCoord, target, new MovementEffect().promotedTo(p).captured(enemyPiece).disableEnPassantIfAllowed(board)));
                }
                return res;
            } else {
                return Lists.newArrayList(new Movement(MovementType.CAPTURE, myCoord, target, new MovementEffect().captured(enemyPiece).disableEnPassantIfAllowed(board)));
            }
        } else {
            return Lists.newArrayList();
        }
    }

    public Movement enPassantWest() {
        return enPassant(myCoord.apply(directionMove).west());
    }


    public Movement enPassantEast() {
        return enPassant(myCoord.apply(directionMove).east());
    }

    private Movement enPassant(Coord target) {
        // target would be out of board ?
        if (!target.isValid()) {
            return null;
        }
        // target is occupied
        if(!board.isEmpty(target)) {
            return null;
        }
        Coord enemyCoord = Coord.get(target.getCol(), myCoord.getRow());
        if (board.isPlayers(enemyCoord, player.enemy()) && board.getEnPassantAllowed() == enemyCoord) {
            return new Movement(MovementType.EN_PASSANT, myCoord, target, new MovementEffect().captured(Piece.PAWN).disableEnPassantIfAllowed(board));
        } else {
            return null;
        }
    }

    public List<Movement> promotions() {
        if (myCoord.apply(directionMove).getRow() == player.enemy().getStartingRow()) {
            Coord target = myCoord.apply(directionMove);
            if (board.isEmpty(target)) {
                List<Movement> promotions = new ArrayList<>();
                MovementEffect me = new MovementEffect().disableEnPassantIfAllowed(board);
                for (Piece p : PROMOTION_SET) {
                    promotions.add(new Movement(MovementType.PROMOTION, myCoord, target, me.promotedTo(p)));
                }
                return promotions;
            }
        }
        return null;
    }


}
