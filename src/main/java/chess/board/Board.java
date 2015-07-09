package chess.board;

import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Player;

import java.util.List;

public interface Board {
    Player getPlayerOnTurn();

    Board allowEnPassant(Coord coord);

    Coord getEnPassantAllowed();

    CastlingRights getCastlingRights();

    Board disableCastling(Player player, CastlingType castlingType);

    Figure get(Coord coord);

    Board set(Coord coord, Figure figure);

    Board remove(Coord coord);

    boolean isEmpty(Coord coord);

    Board setOnTurn(Player player);

    Board clearEnPassant();

    Board enableCastling(Player player, CastlingType castlingType);

    List<Coord> locateAll(Figure figure);

    BitBoard getBitBoard();

    void checkSanity();
}
