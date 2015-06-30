package chess.game;

import chess.board.BoardSerializer;
import chess.movements.Movement;
import chess.movements.transformers.CoordinateNotationTransformer;
import chess.movements.transformers.NotationTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tom on 30.6.2015.
 */
public class CmdLineAdapter {

    public static void main(String[] args) throws IOException {
        Game game = new Game(Game.randomId());
        BoardSerializer serializer = new BoardSerializer();
        while (true) {
            System.out.println(serializer.serializeIntoUtf8(game.getCurrentBoard()));
            NotationTransformer transformer = new CoordinateNotationTransformer(game.getCurrentBoard(), game.getPlayerOnTurn());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String command = br.readLine();
            System.out.println("Processing command " + command);
            Movement movement = transformer.fromNotation(command);
            if (movement == null) {
                System.err.println("Command not parsed!");
                continue;
            }
            System.out.println("Parsed as " + movement);
            game.addMovement(movement);
        }
    }

}
