package chess.api;

import chess.enums.Coord;
import chess.game.Game;
import chess.game.GameFactory;
import chess.game.InvalidMoveException;
import chess.game.MoveCommand;
import com.google.common.io.BaseEncoding;
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
import java.util.*;
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
        byte[] id = new byte[8];
        new Random().nextBytes(id);
        String gameId = BaseEncoding.base16().encode(id);
        Game game = GameFactory.newGame();
        games.put(gameId, game);
        return game;
    }

    @GET
    @Path("game/move/{id}/{move}")
    @Produces("application/json")
    public Game moveCoordinate(@PathParam("id") String id, @PathParam("move") String move) throws InvalidMoveException {
        Game game = games.get(id);
        if (game == null) {
            return null;
        }
        move = move.toUpperCase();
        String[] coords = move.split("-");
        Coord from = Coord.valueOf(coords[0]);
        Coord to = Coord.valueOf(coords[1]);
        Game ng = new GameFactory(game).move(new MoveCommand(from, to));
        games.put(id, ng);
        return ng;
    }

    @GET
    @Path("game/moves/{id}/{coord}")
    @Produces("application/json")
    public List<String> getMoves(@PathParam("id") String id, @PathParam("coord") String coordString) {
        Game game = games.get(id);
        if (game == null) {
            return Collections.emptyList();
        }
        GameFactory gameFactory = new GameFactory(game);
        List<MoveCommand> mc = gameFactory.getPossibleMoveCommands();
        List<String> moves = new ArrayList<>();
        mc.forEach(command -> moves.add(command.getFrom() + "-" + command.getTo()));
        return moves;
    }

}