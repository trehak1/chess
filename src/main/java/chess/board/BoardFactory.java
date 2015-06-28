package chess.board;

import chess.enums.Col;
import chess.enums.Figure;
import chess.enums.Piece;
import chess.enums.Row;

/**
 * Created by Tom on 26.6.2015.
 */
public class BoardFactory {

    public BoardFactory() {
    }

    public Board newGameBoard() {
        Board board = new Board();

        // whites
        // pawns
        for (Col c : Col.validValues()) {
            board = board.set(c, Row._2, Figure.white(Piece.PAWN));
        }
        // rooks
        board = board.set(Col.A, Row._1, Figure.white(Piece.ROOK));
        board = board.set(Col.H, Row._1, Figure.white(Piece.ROOK));
        // knights
        board = board.set(Col.B, Row._1, Figure.white(Piece.KNIGHT));
        board = board.set(Col.G, Row._1, Figure.white(Piece.KNIGHT));
        // bishops
        board = board.set(Col.C, Row._1, Figure.white(Piece.BISHOP));
        board = board.set(Col.F, Row._1, Figure.white(Piece.BISHOP));
        // king
        board = board.set(Col.E, Row._1, Figure.white(Piece.KING));
        // queen
        board = board.set(Col.D, Row._1, Figure.white(Piece.QUEEN));

        // blacks
        // pawns
        for (Col c : Col.validValues()) {
            board = board.set(c, Row._7, Figure.black(Piece.PAWN));
        }
        // rooks
        board = board.set(Col.A, Row._8, Figure.black(Piece.ROOK));
        board = board.set(Col.H, Row._8, Figure.black(Piece.ROOK));
        // knights
        board = board.set(Col.B, Row._8, Figure.black(Piece.KNIGHT));
        board = board.set(Col.G, Row._8, Figure.black(Piece.KNIGHT));
        // bishops
        board = board.set(Col.C, Row._8, Figure.black(Piece.BISHOP));
        board = board.set(Col.F, Row._8, Figure.black(Piece.BISHOP));
        // king
        board = board.set(Col.E, Row._8, Figure.black(Piece.KING));
        // queen
        board = board.set(Col.D, Row._8, Figure.black(Piece.QUEEN));


        return board;
    }


}
