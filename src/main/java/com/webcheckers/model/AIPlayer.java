package com.webcheckers.model;

import com.webcheckers.appl.GameCenter;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * AIPlayer class represents a WebCheckers AI player
 * and stores their username along with adding the ability for
 * an AI Player to chose a Move
 *
 * @authors
 *      Summer DiStefano
 *      Frank Abbey
 *      Raisa Hossain
 *      Stephen Bosonac
 *
 * @version 4/12/2020
 *
 */
public class AIPlayer extends Player{

    private static final Logger LOG = Logger.getLogger(com.webcheckers.model.Player.class.getName());
    private final String name;
    private static int id = 1;

    /**
     * Constructor used to create a new Player
     *
     * @param username - String representing the player's
     *                   username
     */
    public AIPlayer(final String username) {
        //static member 'id' used to give each AI player a unique name
        this.name = username + "_" + id;
        id++;
        LOG.finer(this + " created.");
    }

    /**
     * Get the username of the  AI player.
     *
     * @return name - Player's username as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Contains algorithm for AI Player's Move choice
     *
     * @param gameCenter - the current GameCenter
     * @return Move - the move to be made by the AI Player
     */
    public void takeAITurn(GameCenter gameCenter) {

        CheckersGame game = gameCenter.getGame(this);
        Turn turn = game.getTurn();
        Board board = turn.getCurrentBoard();

        //AI Player will always use White Pieces
        List<Move> movesAvailable = board.getAvailableMoves(Piece.PieceColor.WHITE);
        Move moveChoice = null;

        //if there isn't a move available, the end game scenario should catch that
        if(!movesAvailable.isEmpty()) {

            for (Move current : movesAvailable) {
                if (current.isJumpType()) {
                    moveChoice = current;
                    moveChoice.setType(Move.Type.JUMP);
                }
            }
            //if a Jump move was not found, just make first available Move
            if(moveChoice == null)
                moveChoice = movesAvailable.get(0);

        }

        //put the main process to sleep in seconds based on the number of moves before changing
        // the turn to make the AI experience flow more naturally
        try {
            Thread.sleep(800 * movesAvailable.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gameCenter.getTurn(this).makeMove(moveChoice);

        //update the board
        turn = game.getTurn();
        board = turn.getCurrentBoard();

        //if the move was a jump, determine if AI should make another jump
        if(moveChoice != null && moveChoice.isJumpType()) {

            while(board.jumpMoveAvailable(Piece.PieceColor.WHITE, moveChoice.getEnd())) {

                movesAvailable = board.getAvailableMoves(Piece.PieceColor.WHITE);

                //search for the jump move that can be taken
                if(!movesAvailable.isEmpty()) {

                    for (Move current : movesAvailable) {

                        if (current.isJumpType() && current.getStart().equals(moveChoice.getEnd())) {
                            moveChoice = current;
                            gameCenter.getTurn(this).makeMove(moveChoice);
                            //update the board
                            board = game.getTurn().getCurrentBoard();
                        }

                    }

                }
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.webcheckers.model.AIPlayer aiPlayer = (com.webcheckers.model.AIPlayer) o;
        return Objects.equals(name, aiPlayer.name);
    }

}
