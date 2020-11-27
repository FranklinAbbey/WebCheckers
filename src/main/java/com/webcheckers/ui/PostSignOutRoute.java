package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The UI Controller to POST the sign-out page.
 *
 * @author Raisa Hossain
 */
public class PostSignOutRoute implements Route{

    static final String TITLE = "Sign Out";
    static final String VIEW_NAME = "signout.ftl";

    private final GameCenter gameCenter;
    private PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * Create UI controller to handle all POST SignOut HTTP requests.
     *
     * @param gameCenter
     * @param templateEngine
     */
    public PostSignOutRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        this.gameCenter = gameCenter;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    /**
     * Render the WebCheckers Sign Out page.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {
        //Retrieve game object
        final Session httpSession = request.session();
        final Player player = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        playerLobby = httpSession.attribute(GetHomeRoute.PLAYER_LOBBY_KEY);
        //start building the view-model
        final Map<String, Object> vm = new HashMap<>();

        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        httpSession.attribute(GetHomeRoute.PLAYER_LOBBY_KEY, playerLobby);
        playerLobby.removePlayer(player);

        CheckersGame game = gameCenter.getGame(player);
        if(gameCenter.getGame(player) != null) {
            game.resignGame(player);
        }

        httpSession.attribute(GetHomeRoute.PLAYER_KEY, null);

        response.redirect(WebServer.HOME_URL);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

    }
}
