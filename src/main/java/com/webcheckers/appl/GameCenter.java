package com.webcheckers.appl;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.Turn;

import java.util.ArrayList;
import java.util.List;

/**
 * GameCenter used to keep track of all current CheckersGames
 *
 * @authors
 *      Frank Abbey
 *      Summer DiStefano
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 3/14/2020
 *
 */
public class GameCenter {

    private List<CheckersGame> gameList;

    /**
     * Create a new GameCenter
     */
    public GameCenter() {
        this.gameList = new ArrayList<>();
    }

    /**
     * Add a game to the list of games in the GameCenter
     *
     * @param game
     */
    public void addGame(CheckersGame game) {
        gameList.add(game);
    }

    /**
     * Remove a game from the list of games in the GameCenter
     *
     * @param game
     */
    public void removeGame(CheckersGame game) {
        if(gameList.contains(game)) {
            gameList.remove(game);
        }
    }

    /**
     * Retrieve a CheckersGame in the GameCenter based on
     * a Player object
     *
     * @param player - player to be searched for
     * @return CheckersGame - the game the player is in, or NULL
     *                        if they aren't in a game
     */
    public CheckersGame getGame(Player player) {
        for(CheckersGame current: gameList) {
            if(current.containsPlayer(player))
                return current;
        }
        return null;
    }

    /**
     * Find out if a Player is in a particular game
     *
     * @param game
     * @param player
     * @return boolean - true: Player is in this game
     *                   false: Player is not in this game
     */
    public boolean playerInGame(CheckersGame game, Player player) {
        return game.containsPlayer(player);
    }

    /**
     * Determine if a Player is in any Game in the GameCenter
     *
     * @param player
     * @return boolean - true: Player is in a game
     *                   false: Player is not in a game
     */
    public boolean playerInAnyGame(Player player) {
        for(CheckersGame current: gameList) {
            if(current.containsPlayer(player))
                return true;
        }
        return false;
    }

    /**
     * Used to resign a Player from a game. Here, we need to remove
     * the game from the game list
     *
     * @param player - the player that is resigning
     * @return boolean - true: success
     *                   false: failure
     */
    public boolean resignGame(Player player) {
        CheckersGame game = getGame(player);
        return game.resignGame(player);
    }

    /**
     * Retrieve the active turn of a game in the GameCenter using
     * a Player object
     *
     * @param player - the player to be searched for
     * @return Turn - the active turn, or NULL if the player is not
     *                the game's active player
     */
    public Turn getTurn(Player player) {
        CheckersGame game = getGame(player);

        if(game.getActivePlayer().equals(player)) {
            return game.getTurn();
        }
        else
            return null;
    }
}
