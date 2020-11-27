package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Turn;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The BackUp Route handler
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
public class PostBackupMoveRoute implements Route {

    private GameCenter gameCenter;
    private String BACKUP_SUCCESS = "Backup Success";

    /**
     * Create the Spark Route (UI controller) to handle all BackUp HTTP requests.
     *
     * @param gameCenter
     *   the shared checkers Game Center
     *
     */
    public PostBackupMoveRoute(GameCenter gameCenter) {
        this.gameCenter = gameCenter;
    }

    /**
     * Called when the Back Up button is pressed
     *
     * @param request
     * @param response
     * @return Gson - a Message object converted to Json
     */
    @Override
    public Object handle(Request request, Response response) {

        Player player = request.session().attribute(GetHomeRoute.PLAYER_KEY);
        Player activePlayer = gameCenter.getGame(player).getActivePlayer();

        Turn turn = gameCenter.getTurn(activePlayer);

        //logic is passed off to Turn object
        turn.backUpMove();

        return new Gson().toJson(new Message(BACKUP_SUCCESS, Message.Type.INFO));
    }
}
