package chess.board;

import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Player;
import com.google.common.base.Preconditions;

import java.util.EnumSet;
import java.util.Set;

public class BoardSanityChecker {

    public static void check(Board board) {
        // check number of pieces
        for (Figure f : EnumSet.complementOf(EnumSet.of(Figure.NONE))) {
            Set<Coord> coords = board.locateAll(f);
            Preconditions.checkArgument(coords.size() <= 32, "too many pieces!");
        }
        // check single king
        Preconditions.checkArgument(board.locateAll(Figure.WHITE_KING).size() == 1, "Multiple white kings");
        Preconditions.checkArgument(board.locateAll(Figure.BLACK_KING).size() == 1, "Multiple black kings");
        // check castlings
        Set<Coord> whiteRooks = board.locateAll(Figure.WHITE_ROOK);
        if (!whiteRooks.contains(Coord.A1)) {
            if (board.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.QUEEN_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        if (!whiteRooks.contains(Coord.H1)) {
            if (board.getCastlingRights().isCastlingEnabled(Player.WHITE, CastlingType.KING_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        Set<Coord> blackRooks = board.locateAll(Figure.BLACK_ROOK);
        if (!blackRooks.contains(Coord.A8)) {
            if (board.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.QUEEN_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
        if (!blackRooks.contains(Coord.H8)) {
            if (board.getCastlingRights().isCastlingEnabled(Player.BLACK, CastlingType.KING_SIDE)) {
                throw new IllegalStateException("wtf");
            }
        }
    }
}
