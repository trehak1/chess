package chess.movements;

import chess.board.Board;
import chess.board.BoardLoader;
import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Player;
import chess.movements.figures.CastlingMovements;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.util.List;

public class CastlingMovementsTest extends MovementsTest {

    private CastlingMovements castlingMovementsWhite = new CastlingMovements(Player.WHITE);
    private CastlingMovements castlingMovementsBlack = new CastlingMovements(Player.BLACK);

    BoardLoader boardLoader = new BoardLoader();

    @Test
    public void testCastling() {
        String fileName = "castling/castling.txt";
        testNumbersOfPossibleMovements(fileName, castlingMovementsWhite, 2);
        testNumbersOfPossibleMovements(fileName, castlingMovementsBlack, 1);
    }
    
    @Test
    public void testCastlingEnabled() {
        BoardLoader boardLoader = new BoardLoader();
        Board board = boardLoader.loadBoard("castling/castling.txt");
        MovementFactory movementFactory = MovementFactory.getFor(Player.WHITE);
        List<Movement> moves = movementFactory.getMoves(board);

        Castling castlingKingSide = new Castling(board, CastlingType.KING_SIDE, Coord.E1, Coord.G1);
        Castling castlingQueenSide = new Castling(board, CastlingType.QUEEN_SIDE, Coord.E1, Coord.C1);
        if (!Iterables.any(moves, (m) -> castlingKingSide.sameCoordsAndTypeAs(m))){
            throw new IllegalStateException("king castling move not found in moves");
        }
        if (!Iterables.any(moves, (m) -> castlingQueenSide.sameCoordsAndTypeAs(m))) {
            throw new IllegalStateException("queen castling move not found in moves");
        }
    }

}
