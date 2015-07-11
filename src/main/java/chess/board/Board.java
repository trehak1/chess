package chess.board;

import chess.enums.CastlingType;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Player;

import java.util.List;
import java.util.Set;

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

    Set<Coord> locateAll(Figure figure);

    BitBoard getBitBoard();

    void checkSanity();

    boolean isPlayers(Coord coord, Player myPlayer);
    
}
