package chess.board;

import chess.enums.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BoardSerializer {
    
    private static final BiMap<Figure, Character> figureCharacters;

    static {
        HashMap<Figure, Character> m = Maps.newHashMap();
//        m.put(Figure.WHITE_PAWN, 'P');
//        m.put(Figure.WHITE_ROOK, 'R');
//        m.put(Figure.WHITE_KNIGHT, 'N');
//        m.put(Figure.WHITE_BISHOP, 'B');
//        m.put(Figure.WHITE_QUEEN, 'Q');
//        m.put(Figure.WHITE_KING, 'K');
//        m.put(Figure.BLACK_PAWN, 'p');
//        m.put(Figure.BLACK_ROOK, 'r');
//        m.put(Figure.BLACK_KNIGHT, 'n');
//        m.put(Figure.BLACK_BISHOP, 'b');
//        m.put(Figure.BLACK_QUEEN, 'q');
//        m.put(Figure.BLACK_KING, 'k');
//        m.put(Figure.NONE, '.');
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
        sb.append("on turn: " + board.getPlayerOnTurn().name());
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
    
    public Board readFromFEN(String fen) {
        // 8/P1k5/K7/8/8/8/8/8 w - - 0 1
        Preconditions.checkNotNull(fen);
        List<String> parts = Splitter.on(' ').trimResults().splitToList(fen.trim());
        Preconditions.checkArgument(parts.size() == 6,"Expecting 6 parts of FEN, got "+parts.size());
        Board b = new BoardFactory().newEmptyBoard();
        
        // pieces
        List<String> rows = Splitter.on('/').splitToList(parts.get(0));
        Preconditions.checkArgument(rows.size() == 8,"Expecting 8 rows, got "+rows.size());
        int rn = 7;
        for(String row : rows) {
            Col col = Col.A;
            char[] chars = row.toCharArray();
            for(int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if(!Character.isDigit(c)) {
                    Figure f = figureFromChar(c);
                    b = b.set(Coord.get(col,Row.values()[rn]), f);
                    col = col.east();
                } else {
                    for(int x = 0; x < Integer.parseInt(c+""); x++) {
                        col = col.east();
                    }
                }
            }
            rn--;
        }
        
        // turn
        if(parts.get(1).trim().toLowerCase().equals("w")) {
            b = b.setOnTurn(Player.WHITE);
        } else if(parts.get(1).trim().toLowerCase().equals("b")) {
            b = b.setOnTurn(Player.BLACK);
        } else {
            throw new IllegalArgumentException("wtf turn "+parts.get(1));
        }
        
        // castling
        b = b.disableCastling(Player.BLACK, CastlingType.KING_SIDE);
        b = b.disableCastling(Player.BLACK, CastlingType.QUEEN_SIDE);
        b = b.disableCastling(Player.WHITE, CastlingType.KING_SIDE);
        b = b.disableCastling(Player.WHITE, CastlingType.QUEEN_SIDE);
        if(!parts.get(2).trim().equals("-")) {
            char[] chars = parts.get(2).toCharArray();
            for(char c : chars) {
                switch (c) {
                    case 'K':
                        b = b.enableCastling(Player.WHITE,CastlingType.KING_SIDE);
                        break;
                    case 'Q':
                        b = b.enableCastling(Player.WHITE,CastlingType.QUEEN_SIDE);
                        break;
                    case 'k':
                        b = b.enableCastling(Player.BLACK,CastlingType.KING_SIDE);
                        break;
                    case 'q':
                        b = b.enableCastling(Player.BLACK,CastlingType.QUEEN_SIDE);
                        break;
                    default:
                        throw new IllegalArgumentException("wtf castling "+c);
                }
            }
        }
        
        // en passant
        if(!parts.get(3).trim().equals("-")) {
            Coord epC = Coord.valueOf(parts.get(3).toUpperCase());
            if (epC.getRow() == Row._3) {
                epC = epC.north();
            } else if(epC.getRow() == Row._6) {
                epC = epC.south();
            } else {
                throw new IllegalArgumentException("wtf");
            }
            b = b.allowEnPassant(epC);
        }
        
        return b;
    }

    private Figure figureFromChar(char c) {
        Player player;
        if(Character.isUpperCase(c)) {
            player = Player.WHITE;
        } else {
            player =Player.BLACK;
        }
        Piece piece;
        switch (Character.toLowerCase(c)) {
            case 'p':
                piece = Piece.PAWN;
                break;
            case 'r':
                piece = Piece.ROOK;
                break;
            case 'n':
                piece = Piece.KNIGHT;
                break;
            case 'b':
                piece = Piece.BISHOP;
                break;
            case 'q':
                piece = Piece.QUEEN;
                break;
            case 'k':
                piece = Piece.KING;
                break;
            default:
                throw new IllegalArgumentException("wtf");
        }
        return Figure.get(player, piece);
    }

}
