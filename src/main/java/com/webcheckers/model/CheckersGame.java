package com.webcheckers.model;

import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.util.Message;

import java.util.Objects;
import java.util.Stack;

/**
 * CheckersGame represents the current state of a game
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
public class CheckersGame {

    //states to be utilized in future versions
    public enum State {
        PLAY,
        SPECTATOR,
        REPLAY
    }

    private Player redPlayer;
    private Player whitePlayer;
    private Player winner;
    private Player loser;
    private State state;
    private Turn activeTurn;
    private Board board;
    private boolean gameOver;
    private boolean isResigned;

    private static int id = 0;

    private Stack<Move> lastMoves;

    public enum ActiveColor {
        RED, WHITE;
        public boolean isRed() {
            return this.equals(ActiveColor.RED);
        }
    }

    /**
     * Constructor for CheckersGame
     *
     * @param redPlayer - Player one, initiated game
     * @param whitePlayer Player two, was redirected into game
     */
    public CheckersGame(Player redPlayer, Player whitePlayer) {
        addPlayers(redPlayer, whitePlayer);
        this.state = State.PLAY;
        this.winner = null;
        this.loser = null;
        gameOver = false;
        isResigned = false;
        //Red Player starts as active Player
        board = new Board();
        //TODO - remove testing board
        //board = Board.testInabilityBoard();
        //board = Board.testAIMultiJumpBoard();
        //board = Board.testKingBoard();
        //board = Board.testMultipleJumpBoard();
        lastMoves = new Stack<>();
        this.activeTurn = new Turn(this, redPlayer, Piece.PieceColor.RED);
        id++;
    }

    /**
     * Add two players to the game
     *
     * @param redPlayer
     * @param whitePlayer
     */
    public void addPlayers(Player redPlayer, Player whitePlayer) {
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
    }

    /**
     * Add a Move to the Stack of previous Moves
     *
     * @param move
     * @param type - the type of Move it is (SINGLE, JUMP)
     */
    public void addMove(Move move, Move.Type type) {
        move.setType(type);
        lastMoves.push(move);
    }

    /**
     * Retrieve the Red Player
     *
     * @return Player
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Retrieve the White Player
     *
     * @return Player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Retrieve the loser of the game
     *
     * @return Player
     */
    public Player getLoser() {
        return loser;
    }

    /**
     * Retrieve the winner of the game
     *
     * @return Player
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Determined if the game has been reigned
     * @return boolean
     */
    public boolean isResigned() {
        return isResigned;
    }

    /**
     * Determined if the game is over
     * @return boolean
     */
    public boolean isOver() {
        return gameOver;
    }

    /**
     * Retrieve the state of the game
     *
     * @return CheckersGame.State
     */
    public State getState() {
        return state;
    }

    /**
     * Retrieve the board for a game
     *
     * @return Board board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Retrieve the active color of the game
     *
     * @return CheckersGame.ActiveColor
     */
    public ActiveColor getActiveColor() {
        if(getTurn().getPlayerColor().equals(Piece.PieceColor.RED))
            return ActiveColor.RED;
        else
            return ActiveColor.WHITE;
    }

    /**
     * Retrieve the active Player of the game
     *
     * @return Player
     */
    public Player getActivePlayer() {
        return activeTurn.getPlayer();
    }

    /**
     * Retrieve the unique id of the game
     *
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the turn of the game
     *
     * @return Turn turn - the current turn
     */
    public Turn getTurn() {
        return activeTurn;
    }

    /**
     * Retrieve the Stack containing the last moves a Player made
     * on their Turn
     *
     * @return Stack lastMoves
     */
    public Stack<Move> getLastMoves() {
        return lastMoves;
    }

    /**
     * Set the Turn to the other Player to allow the game to progress
     */
    public void changeTurn() {

        Player activePlayer = activeTurn.getPlayer();
        Piece.PieceColor activeColor = activeTurn.getPlayerColor();

        Player newPlayer;
        Piece.PieceColor newPieceColor;

        //flip the active Player/Color
        if(activePlayer.equals(redPlayer)) {
            newPlayer = whitePlayer;
            newPieceColor = Piece.PieceColor.WHITE;
        }
        else {
            newPlayer = redPlayer;
            newPieceColor = Piece.PieceColor.RED;
        }

        // If red player has no pieces, red player loses and white player wins.
        if(board.getRedPieces() == 0)
        {
            this.winner = this.whitePlayer;
            this.loser = this.redPlayer;
            this.gameOver = true;
        }
        // Otherwise, if the white player has no pieces, red player wins and white player loses.
        // Game should end.
        else if(board.getWhitePieces() == 0)
        {
            this.winner = this.redPlayer;
            this.loser = this.whitePlayer;
            this.gameOver = true;
        }

        // If the new player has no available moves, they win and the other player loses.
        if(board.getAvailableMoves(newPieceColor).isEmpty())
        {
            this.winner = activePlayer;
            this.loser = newPlayer;
            this.gameOver = true;
        }

        activeTurn = new Turn(this, newPlayer, newPieceColor);
    }

    /**
     * After a Move is made and decided, submit the Move to finalize the Turn
     *
     * @return Message - INFO: The submission was successful
     *                  ERROR: The submission was unsuccessful, this could only mean
     *                         somehow the given Player is not the active player
     */
    public Message submitTurn() {

        board = getTurn().getCurrentBoard();
        //remove all pieces that may have been jumped from the board
        board.removePieces(getTurn().positionsTakenThisTurn);

        lastMoves.clear();
        this.changeTurn();

        if(getTurn().getPlayer() instanceof AIPlayer) {
            return new Message("AI thinking...", Message.Type.INFO);
        }

        return new Message(this.getTurn().FINALIZED, Message.Type.INFO);
    }

    /**
     * Determine if a player is in the current game
     *
     * @return boolean
     */
    public boolean containsPlayer(Player player) {
        String username = player.getName();
        if (redPlayer == null && whitePlayer == null)
            return false;
        if (!redPlayer.getName().equals(username)) {
            return whitePlayer.getName().equals(username);
        }
        return true;
    }

    /**
     * Resign this game
     *
     * @param player - the Player that is resigning
     * @return boolean - true: the resignation was successful
     *                   false: the resignation was unsuccessful due to the
     *                          player not being the active player in the game
     */
    public boolean resignGame(Player player) {
        //if the Turn is in the proper state
        if(activeTurn.getState() == Turn.State.EMPTY_TURN) {
            loser = player;
            if(player.equals(getRedPlayer()))
                winner = getWhitePlayer();
            else
                winner = getRedPlayer();

            gameOver = true;
            isResigned = true;

            GetGameRoute.closedGames++;

            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckersGame game = (CheckersGame) o;
        return this.getId() == game.getId();
    }

}

