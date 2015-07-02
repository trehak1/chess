package chess.game;

import chess.enums.Coord;
import chess.movements.Movement;
import chess.movements.MovementFactory;
import chess.movements.transformers.CoordinateNotationTransformer;
import chess.movements.transformers.NotationTransformer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tom on 1.7.2015.
 */
@Path("chess")
public class RestApi {

    private static final URI BASE_URI = URI.create("http://localhost:3333/");
    public static final String ROOT_PATH = "chess";
    private static final Map<String, Game> games = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("Chess REST API starting");

            final ResourceConfig resourceConfig = new ResourceConfig(RestApi.class);
            resourceConfig.register((ContainerResponseFilter) (requestContext, responseContext) -> {
                MultivaluedMap<String, Object> headers = responseContext.getHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET");
            });
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);

            System.out.println(String.format("Application started.\nTry out %s%s\nHit enter to stop it...", BASE_URI, ROOT_PATH));
            System.in.read();
            server.shutdownNow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @GET
    @Path("game/get/{id}")
    @Produces("application/json")
    public Game getGame(@PathParam("id") String id) {
        Game game = games.get(id);
        return game;
    }

    @GET
    @Path("game/new")
    @Produces("application/json")
    public Game newGame() {
        String newGameId = Game.randomId();
        Game game = new Game(newGameId);
        games.put(newGameId, game);
        return game;
    }

    @GET
    @Path("game/move/{id}/{move}")
    @Produces("application/json")
    public Game moveCoordinate(@PathParam("id") String id, @PathParam("move") String move) {
        Game game = games.get(id);
        if (game == null) {
            return null;
        }
        NotationTransformer transformer = new CoordinateNotationTransformer(game.getCurrentBoard(), game.getPlayerOnTurn());
        Movement movement = transformer.fromNotation(move);
        if (movement == null) {
            throw new IllegalArgumentException("Illegal move " + move);
        }
        game.addMovement(movement);
        return game;
    }
    
    @GET
    @Path("game/moves/{id}/{coord}")
    @Produces("application/json")
    public List<String> getMoves(@PathParam("id") String id, @PathParam("coord") String coordString) {
        Game game = games.get(id);
        if (game == null) {
            return Collections.emptyList();
        }
        NotationTransformer transformer = new CoordinateNotationTransformer(game.getCurrentBoard(), game.getPlayerOnTurn());
        Coord coord = transformer.coordFromNotation(coordString);
        
        List<String> moves = new ArrayList<>();
        MovementFactory movementFactory = MovementFactory.getFor(game.getPlayerOnTurn());
        List<Movement> movements = movementFactory.getMoves(game.getCurrentBoard());
        for (Movement m : movements) {
            if (m.getFrom() == coord) {
                String s = transformer.coordToNotation(m.getTo());
                moves.add(s);
            }
        }
        return moves;
    }

}