package chess.board;

import chess.enums.*;

/**
 * Created by Tom on 26.6.2015.
 */
public class BoardFactory {

    public BoardFactory() {
    }

    public Board newGameBoard() {
        Board board = new ImmutableBoard();
        board = setUpNewGame(board);
        return board;
    }

    public Board newMutableGameBoard() {
        Board board = new MutableBoard();
        board = setUpNewGame(board);
        return board;
    }
    
    private Board setUpNewGame(Board board) {
        // whites
        // pawns
        for (Col c : Col.validValues()) {
            board = board.set(Coord.get(c, Row._2), Figure.white(Piece.PAWN));
        }
        // rooks
        board = board.set(Coord.A1, Figure.white(Piece.ROOK));
        board = board.set(Coord.H1, Figure.white(Piece.ROOK));
        // knights
        board = board.set(Coord.B1, Figure.white(Piece.KNIGHT));
        board = board.set(Coord.G1, Figure.white(Piece.KNIGHT));
        // bishops
        board = board.set(Coord.C1, Figure.white(Piece.BISHOP));
        board = board.set(Coord.F1, Figure.white(Piece.BISHOP));
        // king
        board = board.set(Coord.E1, Figure.white(Piece.KING));
        // queen
        board = board.set(Coord.D1, Figure.white(Piece.QUEEN));

        // blacks
        // pawns
        for (Col c : Col.validValues()) {
            board = board.set(Coord.get(c, Row._7), Figure.black(Piece.PAWN));
        }
        // rooks
        board = board.set(Coord.A8, Figure.black(Piece.ROOK));
        board = board.set(Coord.H8, Figure.black(Piece.ROOK));
        // knights
        board = board.set(Coord.B8, Figure.black(Piece.KNIGHT));
        board = board.set(Coord.G8, Figure.black(Piece.KNIGHT));
        // bishops
        board = board.set(Coord.C8, Figure.black(Piece.BISHOP));
        board = board.set(Coord.F8, Figure.black(Piece.BISHOP));
        // king
        board = board.set(Coord.E8, Figure.black(Piece.KING));
        // queen
        board = board.set(Coord.D8, Figure.black(Piece.QUEEN));

        return board;
    }


    public Board newEmptyBoard() {
        Board board = new ImmutableBoard();
        return board;
    }
}
