package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The POST "/resignGame" Route handler
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 4/1/2020
 *
 */
public class PostResignGameRoute implements Route {

    private GameCenter gameCenter;
    private final String RESIGNED = "Resigned";
    private final String RESIGN_FAILURE = "Resign Failure";

    /**
     * Create the Spark Route (UI controller) to handle all POST /resignGame HTTP requests.
     *
     * @param gameCenter - the shared checkers Game Center
     *
     */
    public PostResignGameRoute(GameCenter gameCenter) {
       this.gameCenter = gameCenter;
    }

    /**
     * Process the request to Resign from a CheckersGame
     *
     * @param request
     * @param response
     * @return Gson - a Message object converted to Json
     */
    @Override
    public Object handle(Request request, Response response) {
        Player player = request.session().attribute(GetHomeRoute.PLAYER_KEY);

        //game logic is handled elsewhere
        if(gameCenter.resignGame(player)) {
            return new Gson().toJson(new Message(RESIGNED, Message.Type.INFO));
        }
        else {
            return new Gson().toJson(new Message(RESIGN_FAILURE, Message.Type.ERROR));
        }
    }
}
