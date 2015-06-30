package chess.board;

import chess.enums.Col;
import chess.enums.Coord;
import chess.enums.Figure;
import chess.enums.Row;
import chess.movements.Castling;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BoardSerializer {
    
    BiMap<Figure, Character> figureCharacters;
    
    public BoardSerializer() {
        Map<Figure, Character> figureCharacterMap = new HashMap<>();
        figureCharacterMap.put(Figure.WHITE_PAWN, '♙');
        figureCharacterMap.put(Figure.WHITE_ROOK, '♖');
        figureCharacterMap.put(Figure.WHITE_KNIGHT, '♘');
        figureCharacterMap.put(Figure.WHITE_BISHOP, '♗');
        figureCharacterMap.put(Figure.WHITE_QUEEN, '♕');
        figureCharacterMap.put(Figure.WHITE_KING, '♔');
        figureCharacterMap.put(Figure.BLACK_PAWN, '♟');
        figureCharacterMap.put(Figure.BLACK_ROOK, '♜');
        figureCharacterMap.put(Figure.BLACK_KNIGHT, '♞');
        figureCharacterMap.put(Figure.BLACK_BISHOP, '♝');
        figureCharacterMap.put(Figure.BLACK_QUEEN, '♛');
        figureCharacterMap.put(Figure.BLACK_KING, '♚');
        figureCharacterMap.put(Figure.NONE, '░');
        figureCharacters = ImmutableBiMap.copyOf(figureCharacterMap);
    }
    
    public String serializeIntoJson(Board board) {
        Gson gson = new Gson();
        return gson.toJson(board);
    }
    
    public Board deserializeFromJson(String board) {
        Gson gson = new Gson();
        return gson.fromJson(board, Board.class);
    }

    /**
     * Serializes the board into matrix 8x8 of chess pieces using UTF-8 symbols.
     * Board also has coordinates on the left side an in the bottom.
     * The A1 position is in the bottom-left corner.
     * Selializes enPassants and castlings.
     * @param board
     * @return
     */
    public String serializeIntoUtf8(Board board) {
        StringBuilder sb = new StringBuilder();
        writeFiguresToUtf8(board, sb);
        writeEnPassantsToUtf8(board, sb);
        writeWhiteCastlingsToUtf8(board, sb);
        writeBlackCastlingsToUtf8(board, sb);
        return sb.toString();
    }

    private void writeBlackCastlingsToUtf8(Board board, StringBuilder sb) {
        // write black castlings enabled:
        sb.append("black castlings: ");
        Castling firstBlackCastling = null;
        for (Castling c : board.blackCastlingEnabled) {
            if (firstBlackCastling == null) {
                firstBlackCastling = c;
            } else {
                sb.append(", ");
            }
            sb.append(c.name());
        }
    }

    private void writeWhiteCastlingsToUtf8(Board board, StringBuilder sb) {
        // write white castlings enabled:
        sb.append("white castlings: ");
        Castling firstWhiteCastling = null;
        for (Castling c : board.whiteCastlingEnabled) {
            if (firstWhiteCastling == null) {
                firstWhiteCastling = c;
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
        Coord firstEnPassant = null;
        for (Coord c : board.enPassantAllowed) {
            if (firstEnPassant == null) {
                firstEnPassant = c;
            } else {
                sb.append(", ");
            }
            sb.append(c.name());
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
                Figure figure = board.get(c, r);
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
        Board board = new Board();
        String[] lines = s.split("\n");
        Preconditions.checkArgument(lines.length == 12);
        board = readFiguresFromUtf8(board, lines);
        board = readEnPassantsFromUtf8(board, lines[9]);
        board = readWhiteCastlingsFromUtf8(board, lines[10]);
        board = readBlackCastlingsFromUtf8(board, lines[11]);

        return board;
    }

    private Board readWhiteCastlingsFromUtf8(Board board, String line) {
        // read white castlings
        String[] whiteCastlings = line.replace("white castlings: ", "").replaceAll("[\n\r]", "").split(", ");
        for (String wc : whiteCastlings) {
            if (wc.equals("")) {
                continue;
            }
            board.whiteCastlingEnabled.add(Castling.valueOf(wc));
        }
        return board;
    }

    private Board readBlackCastlingsFromUtf8(Board board, String line) {
        // read black castlings
        String[] blackCastlings = line.replace("black castlings: ", "").split(", ");
        for (String bc : blackCastlings) {
            if (bc.equals("")) {
                continue;
            }
            board.blackCastlingEnabled.add(Castling.valueOf(bc));
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
                board = board.set(c, r, figureCharacters.inverse().get(line.charAt(i + 2)));//first 2 characters are number of row and " "
                c = c.east();
            }
            r = r.south();
        }
        return board;
    }
}
