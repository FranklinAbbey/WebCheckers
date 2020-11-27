package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The POST /signin route handler
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/18/2020
 *
 */
public class PostSignInRoute implements Route {
    //values used in the view-model map for rendering the view after a username is entered
    protected static final String USERNAME_PARAM = "username";
    protected static final String SIGNIN_VIEW_NAME = "signin.ftl";

    static final String INVALID_KEY = "invalidKey";

    //Message objects
    protected static final Message SUCCESS_MSG = Message.info("Successful login");
    protected static final Message FAIL_MSG = Message.error("Failed Login");
    private static final Message ILLEGAL_CHAR_MSG = Message.error("Invalid character used. Please try again.");
    private static final Message TAKEN_MSG = Message.error("Username is already taken. Please try again.");
    private static final Message AI_NAME_MSG = Message.error("Cannot use 'AI_' as name");


    private final TemplateEngine templateEngine;
    private PlayerLobby playerLobby = new PlayerLobby();

    /**
     * Constructor for the PostSigInRoute route handler.
     *
     * @param templateEngine
     *      template engine is used for rendering HTML page
     */
    public PostSignInRoute(TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * Handle POST calls to /signin
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        // retrieve the HTTP session
        final Session httpSession = request.session();

        //start building the view model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Sign In");

        //retrieve the username from form
        final String username = request.queryParams(USERNAME_PARAM);

        ModelAndView mv;
        Player player;

        //if the player lobby says the username has illegal characters
        if(playerLobby.tryUsername(username) == PlayerLobby.LoginResult.ILLEGAL_CHAR) {
            httpSession.attribute(INVALID_KEY,ILLEGAL_CHAR_MSG);
            mv = error(vm, response);
        }

        //if the player lobby says the username is already taken
        else if(playerLobby.tryUsername(username) == PlayerLobby.LoginResult.TAKEN) {
            httpSession.attribute(INVALID_KEY,TAKEN_MSG);
            mv = error(vm, response);
        }

        //if the player is trying to use 'AI' as a name
        else if(playerLobby.tryUsername(username) == PlayerLobby.LoginResult.AI_NAME) {
            httpSession.attribute(INVALID_KEY,AI_NAME_MSG);
            mv = error(vm, response);
        }

        //otherwise the username was accepted
        else {
            player = new Player(username);
            vm.put(GetHomeRoute.CURRENT_USER_ATTR, player);
            //place the user in the http session
            httpSession.attribute(GetHomeRoute.PLAYER_KEY, player);
            //add player to player lobby and player lobby to the current session
            playerLobby.addPlayer(player);
            httpSession.attribute(GetHomeRoute.PLAYER_LOBBY_KEY, playerLobby);
            mv = success(vm, response);
        }
        return templateEngine.render(mv);
    }

    /**
     * Called when there is an error with Signing a Player
     * into the WebCheckers lobby
     *
     * @param vm - the view model to be updated
     * @param response - used to redirect the user
     * @return ModelAndView to be rendered by the template engine
     */
    private ModelAndView error(final Map<String, Object> vm, Response response) {
        vm.put(GetHomeRoute.MESSAGE_ATTR, FAIL_MSG);
        //redirect to signin page
        response.redirect(WebServer.SIGNIN_URL);
        return new ModelAndView(vm, SIGNIN_VIEW_NAME);
    }

    /**
     * Called when there are no issues Signing a Player
     * into the WebCheckers lobby
     *
     * @param vm - the view model to be updated
     * @param response - used to redirect the user
     * @return ModelAndView to be rendered by the template engine
     */
    private ModelAndView success(final Map<String, Object> vm, Response response) {
        vm.put(GetHomeRoute.MESSAGE_ATTR, SUCCESS_MSG);
        //redirect to home page
        response.redirect(WebServer.HOME_URL);
        return new ModelAndView(vm, GetHomeRoute.HOME_VIEW);
    }

}
