package chess.api;

import chess.enums.Coord;
import chess.game.Game;
import chess.game.GameFactory;
import chess.game.InvalidMoveException;
import chess.game.MoveCommand;
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
import java.util.stream.Collectors;

/**
 * Created by Tom on 1.7.2015.
 */
@Path("chess")
public class RestApi {

    private static final URI BASE_URI = URI.create("http://localhost:3333/");
    public static final String ROOT_PATH = "chess";
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

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
    public Session getGame(@PathParam("id") String id) {
        Session session = sessions.get(id);
        return session;
    }

    @GET
    @Path("game/new")
    @Produces("application/json")
    public Session newGame() {
        Session session = Session.createNew();
        sessions.put(session.getId(), session);
        return session;
    }

    @GET
    @Path("game/move/{id}/{move}")
    @Produces("application/json")
    public Session moveCoordinate(@PathParam("id") String id, @PathParam("move") String move) throws InvalidMoveException {
        Session session = sessions.get(id);
        if (session == null) {
            return null;
        }
        String[] coords = move.toUpperCase().trim().split("-");
        Coord from = Coord.valueOf(coords[0]);
        Coord to = Coord.valueOf(coords[1]);
        Game ng = new GameFactory(session.getGame()).move(new MoveCommand(from, to));
        session.setGame(ng);
        sessions.put(id, session);
        return session;
    }

    @GET
    @Path("game/moves/{id}/{coord}")
    @Produces("application/json")
    public List<MoveCommand> getMoves(@PathParam("id") String id, @PathParam("coord") String coordString) {
        Session session = sessions.get(id);
        if (session == null) {
            return Collections.emptyList();
        }
        GameFactory gameFactory = new GameFactory(session.getGame());
        List<MoveCommand> mc = gameFactory.getPossibleMoveCommands();
        Coord c = Coord.valueOf(coordString.trim().toUpperCase());
        return mc.stream().filter((comm)->comm.getFrom() == c).collect(Collectors.toList());
    }

}