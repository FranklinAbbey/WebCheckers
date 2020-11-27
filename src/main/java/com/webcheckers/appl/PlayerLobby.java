package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerLobby represents a collection of Players
 *  currently logged into the WebCheckers game
 *
 * @authors
 *      Frank Abbey
 *      Summer DiStefano
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 2/24/2020
 *
 */
public class PlayerLobby {
    //enum to give distinct results from attempting to add a new Player
    public enum LoginResult {ILLEGAL_CHAR, TAKEN, AI_NAME, VALID}
    private List<Player> playerList = new ArrayList<>();
    private static int playersSignedIn = 0;

    /**
     * Used to check if a username is valid or not. A username must
     * satisfy the following:
     *      1. Contains at least one alphanumeric character
     *      2. Contains no characters besides spaces and alphanumerics
     *      3. Must not already exist in the playerList
     *
     * @param username - string representing the given username
     * @return LoginResult -
     *      ILLEGAL_CHAR: The character contains something other
     *                    than alphanumeric characters or is less
     *                    than one character
*           TAKEN       : Username is already in playerList
     *      VALID       : Username can be used
     */
    public synchronized LoginResult tryUsername(String username) {
        //make sure username is not taken
        for(Player current: playerList) {
            if(current.getName().equals(username))
                return LoginResult.TAKEN;
        }
        //make sure username is at least one character
        if(username.length() < 1)
            return LoginResult.ILLEGAL_CHAR;
        if(username.length() == 1) {
            if(!username.matches("[A-Za-z0-9]"))
                return LoginResult.ILLEGAL_CHAR;
        }
        //username cannot start with 'AI_'
        if(username.length() >= 3 && username.substring(0,3).equals("AI_")) {
            return LoginResult.AI_NAME;
        }
        //make sure the username only contains alphanumeric characters
        //using regular expressions
        if(!username.matches("^[A-Za-z0-9 ]*$"))
            return LoginResult.ILLEGAL_CHAR;
        else
            return LoginResult.VALID;
    }

    /**
     * Add a new player to the playerList
     *
     * @param player - Player object to be added
     */
    public void addPlayer(Player player) {
        playerList.add(player);
        playersSignedIn++;
    }

    /**
     * Remove a player from the playerList
     *
     * @param player - Player object to be removed
     */
    public void removePlayer(Player player) {
        playerList.remove(player);
        if(playersSignedIn != 0)
            playersSignedIn--;
    }

    /**
     * Retrieve the playerList
     */
    public List<Player> getLobby() {
        return playerList;
    }

    /**
     * Retrieve the amount of players currently
     * signed in
     */
    public int getPlayersSignedIn() {
        return playersSignedIn;
    }

    /**
     * Get a Player from the PlayerLobby based on their name
     *
     * @param name - the Player to be retrieved
     * @return - the Player retrieved
     */
    public Player getPlayer(String name) {
        if(name == null || name.length() == 0) {
            return null;
        }
        else {
            for(int i = 0; i < playerList.size(); i++) {
                Player current = playerList.get(i);
                if(current.getName().equals(name)) {
                    return current;
                }
            }
            return null;
        }
    }

}
