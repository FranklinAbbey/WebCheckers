package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * The POST "/validateMove" Route handler
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
public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all POST /validateMove HTTP requests.
     *
     * @param gameCenter - the shared checkers Game Center
     * @param gson - The Google JSON parser object used to render Ajax responses.
     */
    public PostValidateMoveRoute(GameCenter gameCenter, Gson gson) {
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Process the request to validate a move made in a CheckersGame
     *
     * @param request
     * @param response
     * @return Gson - a Message object converted to Json
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateMoveRoute is invoked.");

        Player player = request.session().attribute(GetHomeRoute.PLAYER_KEY);
        CheckersGame game = gameCenter.getGame(player);
        Player activePlayer = game.getActivePlayer();
        Move move;

        //the actionData parameter contains the Move made as a String
        String moveAsJsonString = request.queryParams("actionData");
        //build a Move using the String
        move = gson.fromJson(moveAsJsonString, Move.class);

        //Move must be oriented correctly if it was made on a flipped board
        if(activePlayer.equals(gameCenter.getGame(player).getRedPlayer())) {
            move = move.flipMove();
        }

        //game logic handed off to Turn class
        return (new Gson()).toJson(gameCenter.getTurn(activePlayer).validateMove(move));
    }

}
