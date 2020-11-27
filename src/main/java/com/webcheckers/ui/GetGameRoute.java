package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The GET /game route handler
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 4/1/2020
 */
public class GetGameRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    private static final String GAME_TITLE = "Game View";
    private static final String GAME_VIEW_NAME = "game.ftl";
    private static final String VIEW_MODE_ATTR = "viewMode";
    private static final String ACTIVE_COLOR_ATTR = "activeColor";
    private static final String BOARD_ATTR = "board";

    static final String PLAYER_IN_GAME_KEY = "playerInGameKey";

    private final TemplateEngine templateEngine;
    private CheckersGame game;
    private GameCenter gameCenter;
    private Board gameBoard;
    private final Map<String, Object> modeOptions;
    private Gson gson;

    public static int closedGames = 0;

    static final Message GAME_MSG = Message.info("Welcome to the Game!");
    static final Message PLAYER_IN_GAME_MSG = Message.error("That player is already in a game!");

    /**
     * Create the Spark Route (UI controller) to handle all GET /game HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     * @param gameCenter
     *   the shared checkers Game Center
     *
     */
    public GetGameRoute(TemplateEngine templateEngine, GameCenter gameCenter, Gson gson) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.gson = gson;
        modeOptions = new HashMap<>(2);
    }

    /**
     * Render the WebCheckers Game page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Game page
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        Player player = httpSession.attribute((GetHomeRoute.PLAYER_KEY));
        Player opponentPlayer;
        String opponentName;

        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Web Checkers");

        //if the Player was clicked on by another Player and is being redirected here
        //OR is in a Game that is being refreshed
        if(gameCenter.playerInAnyGame(player)) {
            game = gameCenter.getGame(player);
            gameBoard = game.getTurn().getCurrentBoard();

            //have the AI player take their Turn
            if(game.getTurn().getPlayer() instanceof AIPlayer && !game.isOver()) {

                AIPlayer activePlayer = (AIPlayer)game.getTurn().getPlayer();
                activePlayer.takeAITurn(gameCenter);

                gameBoard = game.getTurn().getCurrentBoard();
                game.submitTurn();

            }

        }
        else {
            opponentName = request.queryParams("opponentPlayer");

            //determine if the opponent selected needs to be created as an AI or Human Player
            if(opponentName.equals("AI"))
                opponentPlayer = new AIPlayer(opponentName);
            else
                opponentPlayer = new Player(request.queryParams("opponentPlayer"));

            //check if the opponentPlayer is already in a game, if so redirect to home
            if(gameCenter.playerInAnyGame(opponentPlayer)) {
                httpSession.attribute(GetHomeRoute.OPPONENT_PLAYER_KEY, opponentPlayer);
                response.redirect(WebServer.HOME_URL);
            }
            //if they weren't move forward with adding them to the game
            else {
                //game being created for both Players
                game = new CheckersGame(player, opponentPlayer);
                gameBoard = game.getTurn().getCurrentBoard();
                gameCenter.addGame(game);
            }
        }

        //page attributes to be added
        vm.put(VIEW_MODE_ATTR, game.getState());
        vm.put(ACTIVE_COLOR_ATTR, game.getActiveColor());

        vm.put(GetHomeRoute.MESSAGE_ATTR, GAME_MSG);

        //page variables to be added
        vm.put("title", GAME_TITLE);
        vm.put("currentUser", player);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());

        BoardView boardView = new BoardView(gameBoard);

        //orient the Board correctly based on Player
        if(player.equals(game.getRedPlayer())) {
            boardView.flip();
            vm.put(BOARD_ATTR, boardView.getBoard());
        }
        else {
            vm.put(BOARD_ATTR, boardView.getBoard());
        }
        // Check for Win State
        if(game.isOver()) {
            modeOptions.put("isGameOver", true);
            if(game.isResigned())
                modeOptions.put("gameOverMessage", game.getLoser().getName() + " resigned");
            else
                modeOptions.put("gameOverMessage", game.getWinner().getName() + " has Won");

            vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
            vm.put(VIEW_MODE_ATTR, game.getState());

            //'closedGames' is used to only remove the game when both players have
            //refreshed their pages
            if(game.getLoser() instanceof AIPlayer || game.getWinner() instanceof AIPlayer)
                closedGames = 2;
            else
                closedGames++;
            if(closedGames == 2) {
                gameCenter.removeGame(game);
                closedGames = 0;
            }

        }
        else {
            vm.put(VIEW_MODE_ATTR, game.getState());
        }

        return templateEngine.render(new ModelAndView(vm, GAME_VIEW_NAME));
    }

}
