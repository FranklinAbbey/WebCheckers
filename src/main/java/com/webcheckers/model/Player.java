package com.webcheckers.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Player class represents a WebCheckers player
 * and stores their username
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
public class Player {

    private static final Logger LOG = Logger.getLogger(Player.class.getName());
    private final String name;

    /**
     * Default constructor
     */
    public Player() {
        this.name = "unknown_player";
        LOG.finer(this + " created.");
    }

    /**
     * Constructor used to create a new Player
     *
     * @param username - String representing the player's
     *                   username
     */
    public Player(final String username) {
        this.name = username;
        LOG.finer(this + " created.");
    }

    /**
     * Get the username of the player.
     *
     * @return name - Player's username as a String
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

}
