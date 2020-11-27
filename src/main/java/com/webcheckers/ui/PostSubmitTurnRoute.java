package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The "/submitTurn" Route handler
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
public class PostSubmitTurnRoute implements Route {

    private GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all POST /submitTurn HTTP requests.
     *
     * @param gameCenter - the shared checkers Game Center
     *
     */
    public PostSubmitTurnRoute(GameCenter gameCenter) {
        this.gameCenter = gameCenter;
    }

    /**
     * Process a request to submit a Turn
     *
     * @param request
     * @param response
     * @return Gson - a Message object converted to Json
     */
    @Override
    public Object handle(Request request, Response response) {

        Player player = request.session().attribute(GetHomeRoute.PLAYER_KEY);
        //Player activePlayer = gameCenter.getGame(player).getActivePlayer();
        CheckersGame game = gameCenter.getGame(player);

        //CheckersGame handles game logic
        return new Gson().toJson(game.submitTurn());
    }
}
