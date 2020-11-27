package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.Message;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The GET / route handler
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
public class GetHomeRoute implements Route {
  //attributes used for HTML template variables
  static final String TITLE_ATTR = "title";
  static final String MESSAGE_ATTR = "message";
  static final String CURRENT_USER_ATTR = "currentUser";
  static final String PLAYER_LIST_ATTR = "playerList";
  public static final String PLAYERS_SIGNED_IN_ATTR = "playersSignedIn";

  //key values used for http session storage
  static final String PLAYER_KEY = "playerKey";
  static final String OPPONENT_PLAYER_KEY = "opponentPlayerKey";
  static final String PLAYER_LOBBY_KEY = "playerLobbyKey";
  static final String GAME_KEY = "gameKey";
  static final String TITLE = "Home";
  static final String HOME_VIEW = "home.ftl";

  private PlayerLobby playerLobby = new PlayerLobby();
  //private CheckersGame game;
  private GameCenter gameCenter;

  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
  public static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private static final Message SELECT_PLAYER_MSG = Message.info("Select a Player below to start a game:");
  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all GET / HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   * @param gameCenter
   *   the shared checkers Game Center
   */
  public GetHomeRoute(final TemplateEngine templateEngine, GameCenter gameCenter) {
    Objects.requireNonNull(templateEngine, "templateEngine is required");

    this.templateEngine = templateEngine;
    this.gameCenter = gameCenter;
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    // retrieve the HTTP session
    final Session httpSession = request.session();
    int playersSignedIn;

    //if this is a brand new browser (Player has not signed in)
    if(httpSession.attribute(PLAYER_KEY) == null) {
      LOG.finer("GetHomeRoute is invoked.");

      Map<String, Object> vm = new HashMap<>();
      vm.put(TITLE_ATTR, "Home");
      //Displays "Welcome to the world of online Checkers."
      vm.put(MESSAGE_ATTR, WELCOME_MSG);

      //if a player is not signed in, only show the number of players signed in
      playersSignedIn = playerLobby.getPlayersSignedIn();
      vm.put(PLAYERS_SIGNED_IN_ATTR, playersSignedIn);

      // render the View
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }

    //the player has logged in and is returning home
    else {
      LOG.finer("GetHomeRoute is invoked.");
      //update the vm accordingly
      Map<String, Object> vm = new HashMap<>();
      vm.put(TITLE_ATTR, "Home");

      //retrieve the current player from the http session
      Player player = httpSession.attribute(PLAYER_KEY);
      //if the user is being redirected here after clicking on a Player that
      //is already in a game, retrieve the Player they clicked on
      Player opponentPlayer = httpSession.attribute(OPPONENT_PLAYER_KEY);

      //used to determine if a user has clicked on a Player that is already in game
      if(opponentPlayer != null && gameCenter.playerInAnyGame(opponentPlayer)) {
        //Displays "That player is already in a game!"
        vm.put(MESSAGE_ATTR, GetGameRoute.PLAYER_IN_GAME_MSG);
      }else {
        //Displays "Select a Player below to start a game:"
        vm.put(MESSAGE_ATTR, SELECT_PLAYER_MSG);
      }

      vm.put(CURRENT_USER_ATTR, player);

      //retrieve the current player lobby from the http session
      playerLobby = httpSession.attribute(PLAYER_LOBBY_KEY);
      List<Player> playerList = playerLobby.getLobby();
      vm.put(PLAYER_LIST_ATTR, playerList);
      //if a player is signed in, show the number of players signed in along
      //with the playerList
      playersSignedIn = playerLobby.getPlayersSignedIn();
      vm.put(PLAYERS_SIGNED_IN_ATTR, playersSignedIn);

      //if there is a game going, check if this user is supposed to be in it
      if(gameCenter != null
              && gameCenter.playerInAnyGame(player)
              && !gameCenter.getGame(player).isOver()) {
        response.redirect(WebServer.GAME_URL);
      }

      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }

  }

}
