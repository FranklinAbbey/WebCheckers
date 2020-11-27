package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The "/checkTurn" Route handler
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
public class PostCheckTurnRoute implements Route {

    private GameCenter gameCenter;
    private Gson gson;
    private final String GAME_END_MSG = "Game ended";

    /**
     * Create the Spark Route (UI controller) to handle all POST /checkTurn HTTP requests.
     *
     * @param gson - the Gson object to be utilized
     * @param gameCenter - the shared checkers Game Center
     *
     */
    public PostCheckTurnRoute(GameCenter gameCenter, Gson gson) {
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Determine if the game is over before determining the Turn
     *
     * @param request
     * @param response
     * @return Gson - a Message object converted to Json
     */
    @Override
    public Object handle(Request request, Response response) {
        Player player = request.session().attribute(GetHomeRoute.PLAYER_KEY);
        Player activePlayer = null;
        CheckersGame game = gameCenter.getGame(player);

        if(game != null)
            activePlayer = game.getActivePlayer();

        String playersTurn;

        if(game == null) {
            return gson.toJson(new Message(GAME_END_MSG, Message.Type.INFO));
        }

        if(activePlayer != null && activePlayer.equals(game.getActivePlayer())) {
            playersTurn = "true";
            return gson.toJson(new Message(playersTurn, Message.Type.INFO));
        }
        else {
            playersTurn = "false";
            return gson.toJson(new Message(playersTurn, Message.Type.INFO));
        }

    }

}
