package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;

public class BoardSerializer {
    
    private static final BiMap<Figure, Character> figureCharacters;

    static {
        HashMap<Figure, Character> m = Maps.newHashMap();
        m.put(Figure.WHITE_PAWN, '♙');
        m.put(Figure.WHITE_ROOK, '♖');
        m.put(Figure.WHITE_KNIGHT, '♘');
        m.put(Figure.WHITE_BISHOP, '♗');
        m.put(Figure.WHITE_QUEEN, '♕');
        m.put(Figure.WHITE_KING, '♔');
        m.put(Figure.BLACK_PAWN, '♟');
        m.put(Figure.BLACK_ROOK, '♜');
        m.put(Figure.BLACK_KNIGHT, '♞');
        m.put(Figure.BLACK_BISHOP, '♝');
        m.put(Figure.BLACK_QUEEN, '♛');
        m.put(Figure.BLACK_KING, '♚');
        m.put(Figure.NONE, '░');
        figureCharacters = ImmutableBiMap.copyOf(m);
    }
    
    public BoardSerializer() {
       
    }

    public String serializeIntoJson(Board board) {
        Gson gson = new Gson();
        return gson.toJson(board);
    }

    public Board deserializeFromJson(String board) {
        Gson gson = new Gson();
        return gson.fromJson(board, ImmutableBoard.class);
    }

    /**
     * Serializes the board into matrix 8x8 of chess pieces using UTF-8 symbols.
     * Board also has coordinates on the left side an in the bottom.
     * The A1 position is in the bottom-left corner.
     * Selializes enPassants and castlings.
     *
     * @param board
     * @return
     */
    public String serializeIntoUtf8(Board board) {
        StringBuilder sb = new StringBuilder();
        writeFiguresToUtf8(board, sb);
        writeEnPassantsToUtf8(board, sb);
        writeWhiteCastlingsToUtf8(board, sb);
        writeBlackCastlingsToUtf8(board, sb);
        writeOnTurn(board, sb);
        return sb.toString();
    }

    private void writeOnTurn(Board board, StringBuilder sb) {
        sb.append("on turn: " + board.getOnTurn().name());
        sb.append("\n");
    }

    private void writeBlackCastlingsToUtf8(Board board, StringBuilder sb) {
        // write black castlings enabled:
        sb.append("black castlings: ");
        CastlingType firstBlackCastlingType = null;
        for (CastlingType c : board.getCastlingRights().getCastlingsEnabled(Player.BLACK)) {
            if (firstBlackCastlingType == null) {
                firstBlackCastlingType = c;
            } else {
                sb.append(", ");
            }
            sb.append(c.name());
        }
        sb.append("\n");
    }

    private void writeWhiteCastlingsToUtf8(Board board, StringBuilder sb) {
        // write white castlings enabled:
        sb.append("white castlings: ");
        CastlingType firstWhiteCastlingType = null;
        for (CastlingType c : board.getCastlingRights().getCastlingsEnabled(Player.WHITE)) {
            if (firstWhiteCastlingType == null) {
                firstWhiteCastlingType = c;
            } else {
                sb.append(", ");
            }
            sb.append(c.name());
        }
        sb.append("\n");
    }

    private void writeEnPassantsToUtf8(Board board, StringBuilder sb) {
        // write en passants:
        sb.append("enpassants: ");
        if (board.getEnPassantAllowed() != null) {
            sb.append(board.getEnPassantAllowed().name());
        }
        sb.append("\n");
    }

    private void writeFiguresToUtf8(Board board, StringBuilder sb) {
        // write figures:
        Row[] reversedRows = Lists.reverse(Arrays.asList(Row.values())).toArray(new Row[9]);
        for (Row r : reversedRows) {
            if (r == Row.INVALID) {
                continue;
            }
            sb.append((r.ordinal() + 1) + " ");
            for (Col c : Col.values()) {
                if (c == Col.INVALID) {
                    continue;
                }
                Figure figure = board.get(Coord.get(c, r));
                sb.append(figureCharacters.get(figure));
            }
            sb.append('\n');
        }
        // write letters of columns:
        sb.append("  ");
        for (Col c : Col.values()) {
            if (c == Col.INVALID) {
                continue;
            }
            sb.append(c.name() + " ");
        }
        sb.append("\n");
    }

    public Board deserializeFromUtf8(String s) {
        Board board = new ImmutableBoard();
        String[] lines = s.split("\n");
        Preconditions.checkArgument(lines.length >= 12);
        board = readFiguresFromUtf8(board, lines);
        board = readEnPassantsFromUtf8(board, lines[9]);
        board = readWhiteCastlingsFromUtf8(board, lines[10]);
        board = readBlackCastlingsFromUtf8(board, lines[11]);
        if (lines.length > 12) {
            board = readOnTurn(board, lines[12]);
        }
        return board;
    }

    private Board readOnTurn(Board board, String line) {
        String playerS = line.replace("on turn: ", "").trim();
        return board.setOnTurn(Player.valueOf(playerS));
    }

    private Board readWhiteCastlingsFromUtf8(Board board, String line) {
        // read white castlings
        board = board.disableCastling(Player.WHITE, CastlingType.QUEEN_SIDE);
        board = board.disableCastling(Player.WHITE, CastlingType.KING_SIDE);
        String[] whiteCastlings = line.replace("white castlings: ", "").replaceAll("[\n\r]", "").split(", ");
        for (String wc : whiteCastlings) {
            if (wc.equals("")) {
                continue;
            }
            board = board.enableCastling(Player.WHITE, CastlingType.valueOf(wc));
        }
        return board;
    }

    private Board readBlackCastlingsFromUtf8(Board board, String line) {
        // read black castlings
        board = board.disableCastling(Player.BLACK, CastlingType.QUEEN_SIDE);
        board = board.disableCastling(Player.BLACK, CastlingType.KING_SIDE);
        String[] blackCastlings = line.replace("black castlings: ", "").split(", ");
        for (String bc : blackCastlings) {
            if (bc.equals("")) {
                continue;
            }
            board = board.enableCastling(Player.BLACK, CastlingType.valueOf(bc));
        }
        return board;
    }

    private Board readEnPassantsFromUtf8(Board board, String line) {
        // read enpassants
        String[] enpassants = line.replace("enpassants: ", "").replaceAll("[\n\r]", "").split(", ");
        for (String e : enpassants) {
            if (e.equals("")) {
                continue;
            }
            board = board.allowEnPassant(Coord.valueOf(e));
        }
        return board;
    }

    private Board readFiguresFromUtf8(Board board, String[] lines) {
        // read figures
        Row r = Row._8;
        for (int l = 0; l < 8; l++) {
            String line = lines[l];
            Preconditions.checkArgument(line.replaceAll("[\n\r]", "").length() == 10);
            Col c = Col.A;
            for (int i = 0; i < 8; i++) {
                //first 2 characters are number of row and " "
                Figure f = figureCharacters.inverse().get(line.charAt(i + 2));
                if(f!=Figure.NONE) {
                    board = board.set(Coord.get(c, r), f);
                }
                c = c.east();
            }
            r = r.south();
        }
        return board;
    }
}
